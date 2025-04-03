package com.example.banking_app.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TransactionsResponse {
    private String status;
    private List<String> error;
    private TransactionsPayload payload;

    @Data
    public static class TransactionsPayload {
        private List<Transaction> list;
    }

    @Data
    public static class Transaction {
        private String transactionId;
        private String operationId;
        private String accountingDate;
        private String valueDate;
        private TransactionType type;
        private BigDecimal amount;
        private String currency;
        private String description;
    }

    @Data
    public static class TransactionType {
        private String enumeration;
        private String value;
    }
}

