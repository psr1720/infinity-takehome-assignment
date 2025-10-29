package com.example.infinity_webhook_service.exceptions;

import com.example.infinity_webhook_service.entity.Transaction;

public class DuplicateTransactionException extends RuntimeException {

    private final Transaction existingTransaction;

    public DuplicateTransactionException(String message, Transaction existingTransaction) {
        super(message);
        this.existingTransaction = existingTransaction;
    }

    public Transaction getExistingTransaction() {
        return existingTransaction;
    }
}
