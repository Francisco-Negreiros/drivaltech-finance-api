package com.drivaltech.finance.dto;

import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.domain.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionResponse {

    private UUID id;
    private String description;
    private BigDecimal amount;
    private TransactionType type;

    public static TransactionResponse fromEntity(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.id = transaction.getId();
        response.description = transaction.getDescription();
        response.amount = transaction.getAmount();
        response.type = transaction.getType();
        return response;
    }

    public UUID getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public TransactionType getType() { return type; }

}