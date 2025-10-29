package com.example.infinity_webhook_service.exceptions;

public class DuplicateTransactionException extends RuntimeException {
    public DuplicateTransactionException(String msg){
        super(msg);
    }
}
