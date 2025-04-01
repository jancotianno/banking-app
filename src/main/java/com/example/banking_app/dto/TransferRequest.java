package com.example.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private String receiverName;
    private String description;
    private String currency;
    private String amount;
    private String executionDate;
}

