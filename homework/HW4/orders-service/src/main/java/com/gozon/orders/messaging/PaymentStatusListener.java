package com.gozon.orders.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gozon.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStatusListener {
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @Value("${orders.kafka.payment-status-topic}")
    private String paymentStatusTopic;

    @KafkaListener(topics = "${orders.kafka.payment-status-topic}", groupId = "orders-service-group")
    public void handlePaymentStatus(ConsumerRecord<String, String> record) throws Exception {
        PaymentStatusEvent event = objectMapper.readValue(record.value(), PaymentStatusEvent.class);
        orderService.applyPaymentStatus(event.getOrderId(), event.getStatus());
    }
}