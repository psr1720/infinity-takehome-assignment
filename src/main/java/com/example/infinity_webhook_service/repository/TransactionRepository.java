package com.example.infinity_webhook_service.repository;

import com.example.infinity_webhook_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByEventId(String eventId);
    Optional<Transaction> findByTransactionId(String transactionId);
}

