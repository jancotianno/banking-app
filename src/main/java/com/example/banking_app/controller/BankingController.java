package com.example.banking_app.controller;

import com.example.banking_app.request.TransactionsRequest;
import com.example.banking_app.request.TransferRequest;
import com.example.banking_app.response.BalanceResponse;
import com.example.banking_app.response.TransactionsResponse;
import com.example.banking_app.response.TransferResponse;
import com.example.banking_app.service.BankingService;
import com.example.banking_app.utils.MaskUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/accounts/{accountId}")
public class BankingController {

    private final BankingService bankingService;

    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long accountId) {
        log.info("Richiesta saldo per il conto {}", accountId);
        BalanceResponse response = bankingService.getBalance(accountId);
        log.debug("Balance retrieved for accountId={}: {}", accountId, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfers")
    public ResponseEntity<TransferResponse> executeTransfer(
            @Valid @RequestBody TransferRequest transferRequest) {
        log.info("Richiesta bonifico per l'IBAN={}, importo={}",
                MaskUtil.maskIban(transferRequest.getCreditor().getAccount().getAccountCode()),
                transferRequest.getAmount());
        TransferResponse response = bankingService.executeTransfer(transferRequest);
        log.debug("Transfer response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionsResponse> getTransactions(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionsRequest transactionsRequest) {
        log.info("Richiesta movimenti dal={} al={} per il conto={}",
                transactionsRequest.getFromDate(),
                transactionsRequest.getToDate(),
                accountId);
        TransactionsResponse response = bankingService.getTransactions(accountId, transactionsRequest);
        log.debug("Transactions retrieved: {}", response);
        return ResponseEntity.ok(response);
    }
}