package com.gozon.orders.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateOrderRequest {
    @NotBlank
    private String userId;

    @NotNull
    @Min(1)
    private Long amountCents;

    @NotBlank
    private String description;
}