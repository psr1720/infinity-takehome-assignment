package com.example.infinity_webhook_service.dto;

import com.example.infinity_webhook_service.enums.EventType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class TransactionDTO {

    @NotBlank(message = "event_id is required")
    @JsonProperty("event_id")
    public String eventId;

    @NotNull(message = "event_type is required")
    @JsonProperty("event_type")
    public EventType eventType;

    @NotNull(message = "timestamp is required")
    public Instant timestamp;

    @NotNull(message = "data is required")
    @Valid
    public TransactionData data;
}
