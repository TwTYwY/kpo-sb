package com.gozon.orders.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gozon.orders.api.dto.CreateOrderRequest;
import com.gozon.orders.api.dto.OrderResponse;
import com.gozon.orders.domain.Order;
import com.gozon.orders.domain.OrderRepository;
import com.gozon.orders.domain.OrderStatus;
import com.gozon.orders.domain.OutboxEvent;
import com.gozon.orders.domain.OutboxEventRepository;
import com.gozon.orders.messaging.PaymentRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setAmount(BigDecimal.valueOf(request.getAmountCents(), 2));
        order.setDescription(request.getDescription());
        order.setStatus(OrderStatus.NEW);
        orderRepository.save(order);

        PaymentRequestEvent event = new PaymentRequestEvent();
        String eventId = UUID.randomUUID().toString();
        event.setEventId(eventId);
        event.setOrderId(order.getId().toString());
        event.setUserId(order.getUserId());
        event.setAmount(order.getAmount());

        String payload;
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventKey(eventId);
        outboxEvent.setType("PAYMENT_REQUEST");
        outboxEvent.setPayload(payload);
        outboxEvent.setAggregateId(order.getId().toString());
        outboxEvent.setCreatedAt(Instant.now());
        outboxEvent.setSent(false);
        outboxEventRepository.save(outboxEvent);

        return toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> listOrdersByUser(String userId) {
        return orderRepository.findByUserIdOrderByIdAsc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow();
        return toResponse(order);
    }

    @Transactional
    public void applyPaymentStatus(String orderId, String status) {
        UUID uuid = UUID.fromString(orderId);
        Order order = orderRepository.findById(uuid).orElse(null);
        if (order == null) {
            return;
        }
        if (order.getStatus() != OrderStatus.NEW) {
            return;
        }
        if ("SUCCESS".equalsIgnoreCase(status)) {
            order.setStatus(OrderStatus.FINISHED);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .amount(order.getAmount())
                .description(order.getDescription())
                .status(order.getStatus())
                .build();
    }
}