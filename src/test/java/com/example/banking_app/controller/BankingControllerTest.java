package com.example.banking_app.controller;

import com.example.banking_app.request.TransferRequest;
import com.example.banking_app.request.TransactionsRequest;
import com.example.banking_app.response.BalanceResponse;
import com.example.banking_app.response.TransactionsResponse;
import com.example.banking_app.response.TransferResponse;
import com.example.banking_app.service.BankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BankingControllerTest {

    private BankingService bankingService;
    private BankingController controller;

    @BeforeEach
    void setUp() {
        bankingService = mock(BankingService.class);
        controller = new BankingController(bankingService);
    }

    @Test
    void getBalance_shouldReturnBalanceResponse() {
        Long accountId = 123L;
        BalanceResponse mockResponse = new BalanceResponse();
        BalanceResponse.BalancePayload payload = new BalanceResponse.BalancePayload();
        payload.setBalance(new BigDecimal("100.00"));
        payload.setAvailableBalance(new BigDecimal("100.00"));
        payload.setCurrency("EUR");
        mockResponse.setPayload(payload);

        when(bankingService.getBalance(accountId)).thenReturn(mockResponse);

        ResponseEntity<BalanceResponse> response = controller.getBalance(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void executeTransfer_shouldReturnTransferResponse() {
        TransferRequest request = new TransferRequest();
        request.setAmount(new BigDecimal("0.01"));
        request.setCurrency("EUR");
        request.setDescription("Bonifico Test");
        request.setExecutionDate("2025-04-03");

        TransferRequest.Creditor.Account account = new TransferRequest.Creditor.Account("IT60X0542811101000000123456");
        TransferRequest.Creditor creditor = new TransferRequest.Creditor("Mario Rossi", account);
        request.setCreditor(creditor);

        TransferResponse mockResponse = new TransferResponse();
        mockResponse.setStatus("EXECUTED");

        when(bankingService.executeTransfer(request)).thenReturn(mockResponse);

        ResponseEntity<TransferResponse> response = controller.executeTransfer(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void getTransactions_shouldReturnTransactionsResponse() {
        Long accountId = 123L;
        TransactionsRequest request = new TransactionsRequest(LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 31));

        // Crea una transazione mockata
        TransactionsResponse.TransactionsPayload payload = getTransactionsPayload();

        // Crea la risposta
        TransactionsResponse mockResponse = new TransactionsResponse();
        mockResponse.setStatus("OK");
        mockResponse.setError(List.of());
        mockResponse.setPayload(payload);

        // Configura il mock del service
        when(bankingService.getTransactions(accountId, request)).thenReturn(mockResponse);

        // Chiama il controller
        ResponseEntity<TransactionsResponse> response = controller.getTransactions(accountId, request);

        // Verifiche
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());

        // Ulteriori check opzionali
        TransactionsResponse actualResponse = response.getBody();
        assertNotNull(actualResponse);
        assertEquals("OK", actualResponse.getStatus());
        assertNotNull(actualResponse.getPayload());
        assertEquals(1, actualResponse.getPayload().getList().size());
        assertEquals("TX123", actualResponse.getPayload().getList().get(0).getTransactionId());
    }

    private static TransactionsResponse.TransactionsPayload getTransactionsPayload() {
        TransactionsResponse.TransactionType type = new TransactionsResponse.TransactionType();
        type.setEnumeration("GBS_TRANSACTION_TYPE");
        type.setValue("BONIFICO");

        TransactionsResponse.Transaction transaction = new TransactionsResponse.Transaction();
        transaction.setTransactionId("TX123");
        transaction.setOperationId("OP456");
        transaction.setAccountingDate("2024-04-01");
        transaction.setValueDate("2024-04-02");
        transaction.setType(type);
        transaction.setAmount(new BigDecimal("123.45"));
        transaction.setCurrency("EUR");
        transaction.setDescription("Bonifico test");

        // Crea il payload
        TransactionsResponse.TransactionsPayload payload = new TransactionsResponse.TransactionsPayload();
        payload.setList(List.of(transaction));
        return payload;
    }

}

