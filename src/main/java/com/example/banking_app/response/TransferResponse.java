package com.example.banking_app.response;

import lombok.Data;

@Data
public class TransferResponse {
    private String status;
    private String[] errors;
    private TransferPayload payload;
}

