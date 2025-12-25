package com.gozon.orders.messaging;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequestEvent {
    private String eventId;
    private String orderId;
    private String userId;
    private BigDecimal amount;
}