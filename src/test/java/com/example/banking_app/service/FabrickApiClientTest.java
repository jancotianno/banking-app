package com.example.banking_app.service;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FabrickApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    private FabrickApiClient fabrickApiClient;

    @BeforeEach
    public void setUp() {
        fabrickApiClient = new FabrickApiClient();

        ReflectionTestUtils.setField(fabrickApiClient, "baseUrl", "https://sandbox.platfr.io");
        ReflectionTestUtils.setField(fabrickApiClient, "operationBalanceUrl", "/api/gbs/banking/v4.0/accounts/{accountId}/balance");
        ReflectionTestUtils.setField(fabrickApiClient, "operationTransactionsUrl", "/api/gbs/banking/v4.0/accounts/{accountId}/transactions?fromAccountingDate={fromDate}&toAccountingDate={toDate}");
        ReflectionTestUtils.setField(fabrickApiClient, "operationTransferUrl", "/api/gbs/banking/v4.0/accounts/{accountId}/payments/money-transfers");
        ReflectionTestUtils.setField(fabrickApiClient, "apiKey", "api-key");
        ReflectionTestUtils.setField(fabrickApiClient, "accountId", 123L);
    }

    @Test
    public void testGetAccountBalance() {
        // Crea la risposta simulata
        BalanceResponse balanceResponse = new BalanceResponse();
        BalanceResponse.BalancePayload payload = new BalanceResponse.BalancePayload();
        payload.setBalance(BigDecimal.valueOf(1000.00));

        // Crea una ResponseEntity con la risposta simulata
        ResponseEntity<BalanceResponse> responseEntity = new ResponseEntity<>(balanceResponse, HttpStatus.OK);

        // Imposta il comportamento del mock RestTemplate
        when(restTemplate.exchange(
                eq("https://sandbox.platfr.io/api/gbs/banking/v4.0/accounts/123/balance"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(BalanceResponse.class)
        )).thenReturn(responseEntity);

        // Esegui il test
        ResponseEntity<BalanceResponse> result = fabrickApiClient.getAccountBalance(123L, BalanceResponse.class);
        BalanceResponse response = result.getBody();

        // Verifica il risultato
        assertNotNull(result);
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(1000.00), response.getPayload().getBalance());
    }

    @Test
    void testGetAccountBalance2() {
        // Creiamo un oggetto di risposta simulato
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setStatus("SUCCESS");
        BalanceResponse.BalancePayload payload = new BalanceResponse.BalancePayload();
        payload.setBalance(BigDecimal.valueOf(1000));
        balanceResponse.setPayload(payload);

        // Simuliamo la risposta del RestTemplate
//        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(BalanceResponse.class)))
//                .thenReturn(new ResponseEntity<>(balanceResponse, HttpStatus.OK));

        ResponseEntity<BalanceResponse> responseEntity2 = new ResponseEntity<>(balanceResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://sandbox.platfr.io/api/gbs/banking/v4.0/accounts/123/balance"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(BalanceResponse.class)
        )).thenReturn(responseEntity2);

        // Chiamata al metodo da testare
        ResponseEntity<BalanceResponse> responseEntity  = fabrickApiClient.getAccountBalance(123L, BalanceResponse.class);

        BalanceResponse response = responseEntity.getBody();

        // Verifica
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(BigDecimal.valueOf(1000), response.getPayload().getBalance());
    }

    @Test
    public void testExecuteTransfer() {
        // Creiamo un oggetto di risposta simulato
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setStatus("SUCCESS");

        TransferResponse.TransferPayload payload = new TransferResponse.TransferPayload();
        payload.setMoneyTransferId("TX12345");
        transferResponse.setPayload(payload);

        // Simuliamo la risposta del RestTemplate
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(TransferResponse.class)))
                .thenReturn(new ResponseEntity<>(transferResponse, HttpStatus.OK));

        // Creiamo una richiesta di esempio
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAmount(BigDecimal.valueOf(500));
        transferRequest.setCurrency("EUR");
        transferRequest.setDescription("Pagamento esempio");

        // Chiamata al metodo da testare
        ResponseEntity<TransferResponse> responseEntity = fabrickApiClient.executeTransfer(transferRequest, TransferResponse.class);

        TransferResponse response = responseEntity.getBody();

        // Verifica
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("TX12345", response.getPayload().getMoneyTransferId());
    }

    @Test
    public void testGetTransactions() {
        // Creiamo un oggetto di risposta simulato
        TransactionsResponse transactionsResponse = new TransactionsResponse();
        transactionsResponse.setStatus("SUCCESS");
        TransactionsResponse.Transaction transaction = new TransactionsResponse.Transaction();
        transaction.setTransactionId("TX12345");
        transactionsResponse.setPayload(new TransactionsResponse.TransactionsPayload());
        transactionsResponse.getPayload().setList(Collections.singletonList(transaction));

        // Simuliamo la risposta del RestTemplate
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(TransactionsResponse.class)))
                .thenReturn(new ResponseEntity<>(transactionsResponse, HttpStatus.OK));

        // Creiamo una richiesta di esempio
        TransactionsRequest transactionsRequest = new TransactionsRequest();
        transactionsRequest.setFromDate(LocalDate.now().minusDays(30));
        transactionsRequest.setToDate(LocalDate.now());

        // Chiamata al metodo da testare
        ResponseEntity<TransactionsResponse> responseEntity = fabrickApiClient.getTransactions(123L, transactionsRequest, TransactionsResponse.class);

        TransactionsResponse response = responseEntity.getBody();

        // Verifica
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getPayload());
        assertEquals(1, response.getPayload().getList().size());
        assertEquals("TX12345", response.getPayload().getList().get(0).getTransactionId());
    }
}