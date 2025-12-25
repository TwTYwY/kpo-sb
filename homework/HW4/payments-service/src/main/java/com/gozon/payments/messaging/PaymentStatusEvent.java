package com.gozon.payments.messaging;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentStatusEvent {
    private String eventId;
    private String orderId;
    private String userId;
    private BigDecimal amount;
    private String status;
    private String reason;
}