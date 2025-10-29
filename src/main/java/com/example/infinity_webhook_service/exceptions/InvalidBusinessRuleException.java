package com.example.infinity_webhook_service.exceptions;

public class InvalidBusinessRuleException extends RuntimeException {
    public InvalidBusinessRuleException(String msg) {
        super(msg);
    }
}
