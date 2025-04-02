package com.example.banking_app.controller;

import com.example.banking_app.dto.TransferRequest;
import org.springframework.web.bind.annotation.*;
import com.example.banking_app.service.BankingService;

@RestController
@RequestMapping("/accounts")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/{accountId}/balance")
    public String getBalance(@PathVariable Long accountId) {
        return bankingService.getBalance(accountId);
    }

    @PostMapping("/transfers")
    public String executeTransfer(
            @RequestBody TransferRequest transferRequest) {
        return bankingService.executeTransfer(transferRequest);
    }

    @GetMapping("/{accountId}/transactions")
    public String getTransactions(
            @PathVariable Long accountId,
            @RequestParam String fromDate,
            @RequestParam String toDate) {
        return bankingService.getTransactions(accountId, fromDate, toDate);
    }

}