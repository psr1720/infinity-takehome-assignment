package com.example.infinity_webhook_service.mappers;

import com.example.infinity_webhook_service.dto.TransactionDTO;
import com.example.infinity_webhook_service.dto.TransactionData;
import com.example.infinity_webhook_service.dto.UserInfo;
import com.example.infinity_webhook_service.entity.Transaction;
import com.example.infinity_webhook_service.enums.EventType;
import com.example.infinity_webhook_service.enums.Status;


public class TransactionMapper {

    public static Transaction toEntity(TransactionDTO dto) {
        Transaction tx = new Transaction();

        tx.setEventId(dto.getEventId());
        tx.setTransactionId(dto.getData().getTransactionId());
        tx.setAmount(dto.getData().getAmount());
        tx.setCurrency(dto.getData().getCurrency());
        tx.setSenderId(dto.getData().getSender().getId());
        tx.setSenderName(dto.getData().getSender().getName());
        tx.setSenderCountry(dto.getData().getSender().getCountry());
        tx.setReceiverId(dto.getData().getReceiver().getId());
        tx.setReceiverName(dto.getData().getReceiver().getName());
        tx.setReceiverCountry(dto.getData().getReceiver().getCountry());
        tx.setStatus(Status.valueOf(dto.getData().getStatus().toLowerCase()));
        tx.setPaymentMethod(dto.getData().getPaymentMethod());
        tx.setProcessedAt(dto.getTimestamp());

        return tx;
    }

    public static TransactionDTO toDTO(Transaction entity) {
        TransactionDTO dto = new TransactionDTO();
        TransactionData data = new TransactionData();
        UserInfo receiver = new UserInfo();
        UserInfo sender = new UserInfo();

        EventType type = switch (entity.getStatus()) {
            case pending -> EventType.TRANSACTION_PENDING;
            case completed -> EventType.TRANSACTION_COMPLETED;
            case failed -> EventType.TRANSACTION_FAILED;
        };
        dto.setEventType(type);

        dto.setEventId(entity.getEventId());
        dto.setTimestamp(entity.getProcessedAt());
        data.setTransactionId(entity.getTransactionId());
        data.setAmount(entity.getAmount());
        data.setCurrency(entity.getCurrency());
        data.setStatus(entity.getStatus().name());
        data.setPaymentMethod(entity.getPaymentMethod());

        sender.setId(entity.getSenderId());
        sender.setName(entity.getSenderName());
        sender.setCountry(entity.getSenderCountry());
        data.setSender(sender);

        receiver.setId(entity.getReceiverId());
        receiver.setName(entity.getReceiverName());
        receiver.setCountry(entity.getReceiverCountry());
        data.setReceiver(receiver);

        dto.setData(data);
        return dto;
    }
}
