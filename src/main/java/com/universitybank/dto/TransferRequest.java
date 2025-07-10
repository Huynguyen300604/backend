package com.universitybank.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotBlank
    private String fromAccount;

    @NotBlank
    private String toAccount;

    @NotNull
    private BigDecimal amount;
}
