package com.example.banking_app.response;

import lombok.Data;

import java.util.List;

@Data
public class TransactionsPayload {
    private List<Transaction> list;
}
