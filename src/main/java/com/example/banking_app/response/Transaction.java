package com.example.banking_app.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    private String transactionId;
    private String operationId;
    private String accountingDate;
    private String valueDate;
    private TransactionType type;
    private BigDecimal amount;
    private String currency;
    private String description;
}
