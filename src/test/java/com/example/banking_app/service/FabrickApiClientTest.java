package com.example.banking_app.service;

import com.example.banking_app.constant.ConstantUtils;
import com.example.banking_app.request.Account;
import com.example.banking_app.request.Creditor;
import com.example.banking_app.request.TransactionsRequest;
import com.example.banking_app.request.TransferRequest;
import com.example.banking_app.response.BalanceResponse;
import com.example.banking_app.response.TransactionsResponse;
import com.example.banking_app.response.TransferResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FabrickApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    private FabrickApiClient fabrickApiClient;

    @BeforeEach
    public void setUp() {
        fabrickApiClient = new FabrickApiClient();
        ReflectionTestUtils.setField(fabrickApiClient, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(fabrickApiClient, "baseUrl", "https://sandbox.platfr.io");
        ReflectionTestUtils.setField(fabrickApiClient, "operationBalanceUrl", "/api/gbs/banking/v4.0/accounts/{accountId}/balance");
        ReflectionTestUtils.setField(fabrickApiClient, "operationTransactionsUrl", "/api/gbs/banking/v4.0/accounts/{accountId}/transactions?fromAccountingDate={fromDate}&toAccountingDate={toDate}");
        ReflectionTestUtils.setField(fabrickApiClient, "operationTransferUrl", "/api/gbs/banking/v4.0/accounts/{accountId}/payments/money-transfers");
        ReflectionTestUtils.setField(fabrickApiClient, "apiKey", "api-key");
        ReflectionTestUtils.setField(fabrickApiClient, "accountId", 123L);
    }

    @Test
    void testGetAccountBalance() {
        // given
        Long accountId = 123L;
        String expectedUrl = "https://sandbox.platfr.io/api/gbs/banking/v4.0/accounts/123/balance";

        BalanceResponse.BalancePayload payload = new BalanceResponse.BalancePayload();
        payload.setBalance(BigDecimal.valueOf(1000));

        BalanceResponse response = new BalanceResponse();
        response.setPayload(payload);

        ResponseEntity<BalanceResponse> mockResponse = new ResponseEntity<>(response, HttpStatus.OK);

        // headers come costruiti dal metodo reale
        HttpHeaders headers = new HttpHeaders();
        headers.set(ConstantUtils.AUTH_SCHEMA, ConstantUtils.AUTH_SCHEMA_VALUE);
        headers.set(ConstantUtils.API_KEY, "api-key");
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        // when
        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                eq(httpEntity),
                eq(BalanceResponse.class),
                eq(Map.of("accountId", 123L))
        )).thenReturn(mockResponse);

        // when
        ResponseEntity<BalanceResponse> result = fabrickApiClient.getAccountBalance(accountId, BalanceResponse.class);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals(BigDecimal.valueOf(1000), result.getBody().getPayload().getBalance());
    }

    @Test
    void testExecuteTransfer() {
        // given
        TransferRequest request = new TransferRequest();
        Account account = new Account();
        account.setAccountCode("IT60X0542811101000000123456");
        Creditor creditor = new Creditor();
        creditor.setName("Mario Rossi");
        creditor.setAccount(account);
        request.setDescription("Bonifico test");
        request.setCurrency("EUR");
        request.setAmount(BigDecimal.valueOf(250.00));
        request.setCreditor(creditor);

        String expectedUrl = "https://sandbox.platfr.io/api/gbs/banking/v4.0/accounts/123/payments/money-transfers";

        TransferResponse response = new TransferResponse();
        response.setStatus("OK");

        ResponseEntity<TransferResponse> mockResponse = new ResponseEntity<>(response, HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.set(ConstantUtils.AUTH_SCHEMA, ConstantUtils.AUTH_SCHEMA_VALUE);
        headers.set(ConstantUtils.API_KEY, "api-key");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransferRequest> httpEntity = new HttpEntity<>(request, headers);

        // when
        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.POST),
                eq(httpEntity),
                eq(TransferResponse.class),
                eq(Map.of(ConstantUtils.ACCOUNT_ID, 123L))
        )).thenReturn(mockResponse);

        // when
        ResponseEntity<TransferResponse> result = fabrickApiClient.executeTransfer(request, TransferResponse.class);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals("OK", result.getBody().getStatus());
    }

    @Test
    void testGetTransactions() {
        // given
        TransactionsRequest request = new TransactionsRequest();
        request.setFromDate(LocalDate.of(2024, 1, 1));
        request.setToDate(LocalDate.of(2024, 1, 31));

        Long accountId = 123L;
        String expectedUrl = "https://sandbox.platfr.io/api/gbs/banking/v4.0/accounts/123/transactions?fromAccountingDate=2024-01-01&toAccountingDate=2024-01-31";

        TransactionsResponse response = new TransactionsResponse();
        response.setStatus("OK");

        ResponseEntity<TransactionsResponse> mockResponse = new ResponseEntity<>(response, HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.set(ConstantUtils.AUTH_SCHEMA, ConstantUtils.AUTH_SCHEMA_VALUE);
        headers.set(ConstantUtils.API_KEY, "api-key");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        // when
        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                eq(httpEntity),
                eq(TransactionsResponse.class)
        )).thenReturn(mockResponse);

        // when
        ResponseEntity<TransactionsResponse> result = fabrickApiClient.getTransactions(accountId, request, TransactionsResponse.class);

        // then
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals("OK", result.getBody().getStatus());
    }

}