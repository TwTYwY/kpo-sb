package com.gozon.payments.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositRequest {
    @NotBlank
    private String userId;

    @NotNull
    @Min(1)
    private Long amountCents;
}