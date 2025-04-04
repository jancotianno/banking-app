package com.example.banking_app.controller;

import com.example.banking_app.dto.TransferRequest;
import com.example.banking_app.request.TransactionsRequest;
import com.example.banking_app.response.BalanceResponse;
import com.example.banking_app.response.TransactionsResponse;
import com.example.banking_app.response.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.banking_app.service.BankingService;

@RestController
@RequestMapping("/accounts/{accountId}")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long accountId) {
        BalanceResponse response = bankingService.getBalance(accountId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfers")
    public ResponseEntity<TransferResponse> executeTransfer(
            @Valid @RequestBody TransferRequest transferRequest) {
        TransferResponse response = bankingService.executeTransfer(transferRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<TransactionsResponse> getTransactions(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionsRequest transactionsRequest) {
        TransactionsResponse response = bankingService.getTransactions(accountId, transactionsRequest);
        return ResponseEntity.ok(response);
    }
}