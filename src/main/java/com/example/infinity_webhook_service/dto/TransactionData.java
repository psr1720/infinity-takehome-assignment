package com.example.infinity_webhook_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TransactionData {
    @NotBlank(message = "transaction_id is required")
    @JsonProperty("transaction_id")
    private String transactionId;

    @NotNull(message = "amount is required")
    private Double amount;

    @NotNull(message = "currency is required")
    private String currency;

    @NotNull(message = "sender is required")
    @Valid
    private UserInfo sender;

    @NotNull(message = "receiver is required")
    @Valid
    private UserInfo receiver;

    @NotBlank(message = "status is required")
    private String status;

    @NotBlank(message = "payment_method is required")
    @JsonProperty("payment_method")
    private String paymentMethod;

    private Metadata metadata;
}
