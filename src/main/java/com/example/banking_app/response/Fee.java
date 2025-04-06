package com.example.banking_app.response;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class Fee {
    private String feeCode;
    private String description;
    private BigDecimal amount;
    private String currency;
}
