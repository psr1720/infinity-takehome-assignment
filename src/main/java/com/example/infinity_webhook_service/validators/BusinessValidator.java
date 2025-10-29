package com.example.infinity_webhook_service.validators;

import com.example.infinity_webhook_service.dto.TransactionDTO;
import com.example.infinity_webhook_service.dto.TransactionData;
import com.example.infinity_webhook_service.exceptions.InvalidBusinessRuleException;
import org.springframework.stereotype.Component;
import java.util.Currency;

@Component
public class BusinessValidator {

    public void validate(TransactionDTO dto) {
        TransactionData data = dto.getData();

        Double amount = data.getAmount();
        String currency = data.getCurrency();
        if (amount == null || amount <= 0 || amount.isNaN() || amount.isInfinite()) {
            throw new InvalidBusinessRuleException("Amount must be a positive number.");
        }
        try {
            Currency.getInstance(currency);
        } catch (IllegalArgumentException e) {
            throw new InvalidBusinessRuleException("Invalid currency code: " + currency);
        }
    }
}