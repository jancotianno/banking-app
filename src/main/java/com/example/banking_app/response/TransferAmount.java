package com.example.banking_app.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferAmount {
    private BigDecimal debtorAmount;
    private String debtorCurrency;
    private BigDecimal creditorAmount;
    private String creditorCurrency;
    private String creditorCurrencyDate;
    private BigDecimal currencyRatio;
}
