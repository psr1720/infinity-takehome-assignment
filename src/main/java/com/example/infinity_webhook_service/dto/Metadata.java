package com.example.infinity_webhook_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Metadata {
    @NotBlank(message = "reference is required")
    private String reference;
    private String notes;
}
