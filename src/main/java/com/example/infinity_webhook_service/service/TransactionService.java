package com.example.infinity_webhook_service.service;

import com.example.infinity_webhook_service.dto.TransactionDTO;
import com.example.infinity_webhook_service.entity.Transaction;
import com.example.infinity_webhook_service.enums.Status;
import com.example.infinity_webhook_service.exceptions.DuplicateTransactionException;
import com.example.infinity_webhook_service.mappers.TransactionMapper;
import com.example.infinity_webhook_service.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Transaction process(TransactionDTO dto) {
        Optional<Transaction> existingByEvent = repository.findByEventId(dto.getEventId());
        if (existingByEvent.isPresent()) {
            throw new DuplicateTransactionException("Duplicate event_id detected: " + dto.getEventId(), existingByEvent.get());
        }

        Optional<Transaction> existingByTxn = repository.findByTransactionId(dto.getData().getTransactionId());
        if (existingByTxn.isPresent()) {
            Transaction existing = existingByTxn.get();
            if (existing.getStatus() == Status.completed || existing.getStatus() == Status.failed) {
                throw new DuplicateTransactionException("Transaction already processed with status: " + existing.getStatus(), existing);
            }
            String newStatus = dto.getData().getStatus().toLowerCase();
            if (newStatus.equals("completed") || newStatus.equals("failed")) {
                existing.setStatus(Status.valueOf(newStatus));
                existing.setEventId(dto.getEventId());
                existing.setProcessedAt(dto.getTimestamp());
                return repository.save(existing);
            }
        }

        Transaction newTx = TransactionMapper.toEntity(dto);
        return repository.save(newTx);
    }
}
