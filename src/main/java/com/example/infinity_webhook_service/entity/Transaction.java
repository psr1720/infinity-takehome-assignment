package com.example.infinity_webhook_service.entity;

import com.example.infinity_webhook_service.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "transactions", indexes = {@Index(name = "idx_event_id", columnList = "event_id"),
        @Index(name = "idx_transaction_id", columnList = "transaction_id")}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String currency;

    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @Column(name = "sender_country", nullable = false)
    private String senderCountry;

    @Column(name = "receiver_id", nullable = false)
    private String receiverId;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "receiver_country", nullable = false)
    private String receiverCountry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
