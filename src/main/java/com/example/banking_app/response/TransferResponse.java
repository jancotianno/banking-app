package com.example.banking_app.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TransferResponse {
    private String status;
    private String[] errors;
    private TransferPayload payload;

    @Data
    public static class TransferPayload {
        private String moneyTransferId;
        private String cro;
        private String trn;
        private String status;
        private String uri;
        private String direction;
        private Debtor debtor;
        private Creditor creditor;
        private String feeAccountId;
        private String description;
        private String createdDatetime;
        private String accountedDatetime;
        private String debtorValueDate;
        private String creditorValueDate;
        private TransferAmount amount;
        private boolean isUrgent;
        private boolean isInstant;
        private String feeType;
        private List<Fee> fees;
        private boolean hasTaxRelief;
    }

    @Data
    public static class Debtor {
        private String name;
        private Account account;
    }

    @Data
    public static class Creditor {
        private String name;
        private Account account;
    }

    @Data
    public static class Account {
        private String accountCode;
        private String bicCode;
    }

    @Data
    public static class TransferAmount {
        private BigDecimal debtorAmount;
        private String debtorCurrency;
        private BigDecimal creditorAmount;
        private String creditorCurrency;
        private String creditorCurrencyDate;
        private BigDecimal currencyRatio;
    }

    @Data
    public static class Fee {
        private String feeCode;
        private String description;
        private BigDecimal amount;
        private String currency;
    }
}

