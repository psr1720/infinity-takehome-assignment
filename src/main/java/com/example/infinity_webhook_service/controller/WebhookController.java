package com.example.infinity_webhook_service.controller;

import com.example.infinity_webhook_service.dto.TransactionDTO;
import com.example.infinity_webhook_service.exceptions.InvalidSignatureException;
import com.example.infinity_webhook_service.validators.BusinessValidator;
import com.example.infinity_webhook_service.validators.SignatureValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    private final SignatureValidator signatureValidator;
    private final BusinessValidator businessValidator;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    public WebhookController(SignatureValidator signatureValidator, BusinessValidator businessValidator, ObjectMapper objectMapper, Validator validator){
        this.signatureValidator = signatureValidator;
        this.businessValidator = businessValidator;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @PostMapping("/payments")
    public ResponseEntity<?> handlePayment(@RequestHeader("X-Signature") String signature,
                                           HttpServletRequest request, @RequestBody String rawBody) throws IOException {

        if (!signatureValidator.isValid(rawBody.getBytes(StandardCharsets.UTF_8), signature)) {
            throw new InvalidSignatureException("Invalid Signature");
        }
        TransactionDTO transactionDTO = objectMapper.readValue(rawBody, TransactionDTO.class);

        Set<ConstraintViolation<TransactionDTO>> violations = validator.validate(transactionDTO);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<TransactionDTO> v : violations) {
                sb.append(v.getPropertyPath()).append(": ").append(v.getMessage()).append("; ");
            }
            throw new IllegalArgumentException("Validation failed: " + sb);
        }

        businessValidator.validate(transactionDTO);

        return ResponseEntity.ok("Received");
    }

}
