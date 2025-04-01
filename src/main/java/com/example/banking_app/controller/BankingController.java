package com.example.banking_app.controller;

import com.example.banking_app.dto.TransferRequest;
import org.springframework.web.bind.annotation.*;
import com.example.banking_app.service.BankingService;

@RestController
@RequestMapping("/banking")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/balance/{accountId}")
    public String getBalance(@PathVariable Long accountId) {
        return bankingService.getBalance(accountId);
    }

    @PostMapping("/transfer")
    public String executeTransfer(@RequestBody TransferRequest transferRequest) {
        return bankingService.executeTransfer(transferRequest);
    }

    @GetMapping("/transactions/{accountId}")
    public String getTransactions(
            @PathVariable Long accountId,
            @RequestParam String fromDate,
            @RequestParam String toDate) {
        return bankingService.getTransactions(accountId, fromDate, toDate);
    }

}