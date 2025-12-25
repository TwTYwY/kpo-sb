package com.gozon.payments.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BalanceResponse {
    private String userId;
    private BigDecimal balance;
}