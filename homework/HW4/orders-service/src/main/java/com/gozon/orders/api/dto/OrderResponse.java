package com.gozon.orders.api.dto;

import com.gozon.orders.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class OrderResponse {
    private UUID id;
    private String userId;
    private BigDecimal amount;
    private String description;
    private OrderStatus status;
}