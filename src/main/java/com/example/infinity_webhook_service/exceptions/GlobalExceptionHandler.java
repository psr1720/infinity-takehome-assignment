package com.example.infinity_webhook_service.exceptions;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MissingRequestHeaderException;
import com.example.infinity_webhook_service.mappers.TransactionMapper;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, String>> handleMissingHeader(MissingRequestHeaderException ex) {
        log.warn("Missing header: {}", ex.getHeaderName());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Missing required header: " + ex.getHeaderName()));
    }

    @ExceptionHandler(InvalidSignatureException.class)
    public ResponseEntity<Map<String, String>> handleInvalidSignature(InvalidSignatureException ex) {
        log.warn("Invalid signature detected");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ValueInstantiationException.class)
    public ResponseEntity<Map<String, String>> handleEnumDeserialization(ValueInstantiationException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidEnumValueException invalidEnumEx) {
            log.warn("Invalid enum value: {}", invalidEnumEx.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", invalidEnumEx.getMessage()));
        }

        log.error("Deserialization error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid request payload"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidBusinessRuleException.class)
    public ResponseEntity<Map<String, String>> handleBusinessRule(InvalidBusinessRuleException ex) {
        log.warn("Business rule violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateTransactionException.class)
    public ResponseEntity<Object> handleDuplicate(DuplicateTransactionException ex) {
        log.warn("Duplicate transaction detected: {}", ex.getMessage());

        if (ex.getExistingTransaction() != null) {
            var dto = TransactionMapper.toDTO(ex.getExistingTransaction());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", ex.getMessage(),
                    "existing_transaction", dto
            ));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
    }
}
