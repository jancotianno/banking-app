package com.example.banking_app.service;

import com.example.banking_app.request.TransactionsRequest;
import com.example.banking_app.request.TransferRequest;
import com.example.banking_app.response.BalanceResponse;
import com.example.banking_app.response.TransactionsResponse;
import com.example.banking_app.response.TransferResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BankingServiceTest {

    @Mock
    private FabrickApiClient fabrickApiClient;

    private BankingService bankingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bankingService = new BankingService(fabrickApiClient);
    }

    @Test
    void testGetBalance() {
        Long accountId = 123L;
        BalanceResponse mockResponse = new BalanceResponse();
        mockResponse.setStatus("OK");

        when(fabrickApiClient.getAccountBalance(accountId, BalanceResponse.class))
                .thenReturn(ResponseEntity.ok(mockResponse));

        BalanceResponse result = bankingService.getBalance(accountId);

        assertNotNull(result);
        assertEquals("OK", result.getStatus());
        verify(fabrickApiClient).getAccountBalance(accountId, BalanceResponse.class);
    }

    @Test
    void testGetTransactions() {
        Long accountId = 456L;
        TransactionsRequest request = new TransactionsRequest(LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 31));
        TransactionsResponse mockResponse = new TransactionsResponse();
        mockResponse.setStatus("OK");

        when(fabrickApiClient.getTransactions(accountId, request, TransactionsResponse.class))
                .thenReturn(ResponseEntity.ok(mockResponse));

        TransactionsResponse result = bankingService.getTransactions(accountId, request);

        assertNotNull(result);
        assertEquals("OK", result.getStatus());
        verify(fabrickApiClient).getTransactions(accountId, request, TransactionsResponse.class);
    }

    @Test
    void testExecuteTransfer() {
        TransferRequest request = new TransferRequest();
        request.setDescription("Test Transfer");
        TransferResponse mockResponse = new TransferResponse();
        mockResponse.setStatus("OK");

        when(fabrickApiClient.executeTransfer(request, TransferResponse.class))
                .thenReturn(ResponseEntity.ok(mockResponse));

        TransferResponse result = bankingService.executeTransfer(request);

        assertNotNull(result);
        assertEquals("OK", result.getStatus());
        verify(fabrickApiClient).executeTransfer(request, TransferResponse.class);
    }
}

