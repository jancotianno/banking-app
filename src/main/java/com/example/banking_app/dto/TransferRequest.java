package com.example.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    private Creditor creditor;
    private String executionDate;
    private BigDecimal amount;
    private String currency;
    private String description;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Creditor {
        private String name;
        private Account account;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Account {
            private String accountCode;
        }
    }
}

