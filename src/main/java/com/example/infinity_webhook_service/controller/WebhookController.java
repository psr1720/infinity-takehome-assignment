package com.example.infinity_webhook_service.controller;

import com.example.infinity_webhook_service.dto.TransactionDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    @PostMapping("/payments")
    public ResponseEntity<?> handlePayment(@Valid @RequestBody TransactionDTO transactionDTO){



        return ResponseEntity.ok("Received");
    }

}
