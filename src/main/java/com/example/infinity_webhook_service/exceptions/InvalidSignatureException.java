package com.example.infinity_webhook_service.exceptions;

public class InvalidSignatureException extends RuntimeException {
    public InvalidSignatureException(String msg) {
        super(msg);
    }
}
