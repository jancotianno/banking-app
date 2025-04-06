package com.example.banking_app.request;


import lombok.Data;

@Data
public class Debtor {
    private String name;
    private Account account;
}
