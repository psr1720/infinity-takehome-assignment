# Infinity Backend Developer Home Assignment

### Webhook Processing Service



## Project Overview

This project implements a **Webhook Processing Service** designed to receive and securely process transaction notifications from a payment service provider.

**Key features:**

- Validates incoming webhooks using an HMAC SHA256 **signature verification mechanism**.
- Ensures **idempotency** by preventing duplicate processing of the same event.
- Validates all required fields and enum values in the payload.
- Persists valid transactions in a **MySQL database**.
- Handles various error scenarios with clear and appropriate HTTP status codes:
  - `200 OK` → Successfully processed
  - `409 CONFLICT` → Duplicate transaction detected
  - `400 BAD REQUEST` → Missing/invalid fields
  - `401 UNAUTHORIZED` → Invalid or missing signature



## Tech Stack

| Layer                   | Technology                                 |
| ----------------------- | ------------------------------------------ |
| **Language**            | Java 17                                    |
| **Framework**           | Spring Boot 3.5.7                          |
| **Build Tool**          | Maven 3.9.9                                |
| **Database**            | MySQL                                      |
| **ORM**                 | Hibernate                                  |
| **Testing / Utilities** | Postman, Python (for signature generation) |


## Prerequisites

Before running the application, ensure you have the following installed:

- **Java 17** or later
- **Maven 3.9+**
- **MySQL 8.0+**
- **An IDE** like IntelliJ IDEA or VS Code (optional)


## Setup Instructions

1. **Clone the repository**

   ```
   git clone <repository_url>
   cd infinity_webhook_service
   ```

2. **Create a .env file in the root directory and add the following:**
   ```
      DB_URL=jdbc:mysql://localhost:3306/infinity_db
      DB_USER=root
      DB_PASSWORD=yourpassword
      WEBHOOK_SECRET=infinity_webhook_secret
   ```
3. **If using and IDE like Intellij click the run after adding .env in the configuration (OR).**

4. **Build the project**
   ```
   mvn clean install
   ```
5. **_Run the application_**
   ```
   mvn spring-boot:run
   ```



## How to Test the Webhook Endpoint

1. The webhook endpoint is exposed at:

   ```
   POST /webhooks/payments
   ```

2. Local URL

   ```
   http://localhost:8080/webhooks/payments
   ```

3. Request Headers

   ```
   Content-Type: application/json
   X-Signature: <HMAC_SHA256_HASH>
   ```

4. Sample Payload

   ```json
   {
     "event_id": "evt_8f7d6e5c4b3a2",
     "event_type": "transaction.completed",
     "timestamp": "2025-10-28T14:30:00Z",
     "data": {
       "transaction_id": "txn_1a2b3c4d5e6f",
       "amount": 2500.75,
       "currency": "USD",
       "sender": {
         "id": "usr_sender_12345",
         "name": "Alice Johnson",
         "email": "alice.j@example.com",
         "country": "US"
       },
       "receiver": {
         "id": "usr_receiver_67890",
         "name": "Raj Patel",
         "email": "raj.p@example.in",
         "country": "IN"
       },
       "status": "completed",
       "payment_method": "bank_transfer",
       "metadata": {
         "reference": "INV-2025-001",
         "notes": "Q4 payment"
       }
     }
   }
   ```

5. Generate a valid X-Signature (Python script)
    ```python 
      import hmac, hashlib, json

      WEBHOOK_SECRET = "infinity_webhook_secret"

      with open("body.json", "rb") as f: #ensure body.json is the payload being sent
          body = f.read()
      signature = hmac.new(WEBHOOK_SECRET.encode("utf-8"), body, hashlib.sha256).hexdigest()

      print("X-Signature:", signature)
    ```
6. Test using postman or cURL
   ```bash
      curl -X POST http://localhost:8080/webhooks/payments \
      -H "Content-Type: application/json" \
      -H "X-Signature: <your_generated_signature>" \
      -d @payload.json
   ```

## Database Setup

1. You can manually create a database before running the app:

   ```sql
   CREATE DATABASE infinity_db;
   ```
   **Spring Data JPA (spring.jpa.hibernate.ddl-auto=update) will automatically generate tables and indexes defined in your Transaction entity.**

   **Transaction.java**
   ```java
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
   ```

