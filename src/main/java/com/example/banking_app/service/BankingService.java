package com.example.banking_app.service;

import com.example.banking_app.request.TransferRequest;
import com.example.banking_app.request.TransactionsRequest;
import com.example.banking_app.response.BalanceResponse;
import com.example.banking_app.response.TransactionsResponse;
import com.example.banking_app.response.TransferResponse;
import com.example.banking_app.utils.MaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BankingService {

    private final FabrickApiClient fabrickApiClient;

    public BankingService(FabrickApiClient fabrickApiClient) {
        this.fabrickApiClient = fabrickApiClient;
    }

    public BalanceResponse getBalance(Long accountId) {
        log.info("Ottenimento saldo per l'account ID: {}", accountId);
        ResponseEntity<BalanceResponse> response =
                fabrickApiClient.getAccountBalance(accountId, BalanceResponse.class);
        return response.getBody();
    }

    public TransactionsResponse getTransactions(Long accountId, TransactionsRequest transactionsRequest) {
        log.info("Ottenimento transazioni per l'account ID: {}, dalla data: {}, alla date: {}",
                accountId, transactionsRequest.getFromDate(), transactionsRequest.getToDate());
        ResponseEntity<TransactionsResponse> response =
                fabrickApiClient.getTransactions(accountId, transactionsRequest, TransactionsResponse.class);
        return response.getBody();
    }

    public TransferResponse executeTransfer(TransferRequest transferRequest) {
        String maskedIban = MaskUtil.maskIban(transferRequest.getCreditor().getAccount().getAccountCode());
        log.info("Richiesto Bonifico per l'IBAN: {}", maskedIban);
        ResponseEntity<TransferResponse> response =
                fabrickApiClient.executeTransfer(transferRequest, TransferResponse.class);
        return response.getBody();
    }
}