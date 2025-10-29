package com.example.infinity_webhook_service.enums;

import com.example.infinity_webhook_service.exceptions.InvalidEnumValueException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {
    TRANSACTION_PENDING("transaction.pending"),
    TRANSACTION_COMPLETED("transaction.completed"),
    TRANSACTION_FAILED("transaction.failed");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EventType fromValue(String value) {
        for (EventType type : values()) {
            if (type.value.equalsIgnoreCase(value)) return type;
        }
        throw new InvalidEnumValueException("Invalid event_type: " + value);
    }
}

