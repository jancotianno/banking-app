package com.example.banking_app.service;

import com.example.banking_app.dto.TransferRequest;
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

    public String getBalance(Long accountId) {
        ResponseEntity<String> response = fabrickApiClient.getAccountBalance(accountId);
        return response.getBody();
    }

    public String getTransactions(Long accountId, String fromDate, String toDate) {
        return fabrickApiClient.getTransactions(accountId, fromDate, toDate);
    }

    public String executeTransfer(TransferRequest transferRequest) {
        log.info("Invio richiesta a Fabrick: {}", transferRequest);
        ResponseEntity<String> response = fabrickApiClient.executeTransfer(transferRequest);
        return response.getBody();
    }

}

