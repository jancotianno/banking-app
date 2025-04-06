package com.example.banking_app.response;

import com.example.banking_app.request.Creditor;
import com.example.banking_app.request.Debtor;
import lombok.Data;

import java.util.List;

@Data
public class TransferPayload {
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
