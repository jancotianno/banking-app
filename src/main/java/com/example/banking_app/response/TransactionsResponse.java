package com.example.banking_app.response;

import lombok.Data;

import java.util.List;

@Data
public class TransactionsResponse {
    private String status;
    private List<String> error;
    private TransactionsPayload payload;
}

