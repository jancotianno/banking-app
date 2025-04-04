package com.example.banking_app.service;

import com.example.banking_app.dto.TransferRequest;
import com.example.banking_app.request.TransactionsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class FabrickApiClient {

    private static final String AUTH_SCHEMA = "Auth-Schema";
    private static final String AUTH_SCHEMA_VALUE = "S2S";
    private static final String API_KEY = "Api-Key";

    @Value("${fabrick.api.base-url}")
    private String baseUrl;

    @Value("${fabrick.api.account-id}")
    private Long accountId;

    @Value("${fabrick.api.auth-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public FabrickApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public <T> ResponseEntity<T> getAccountBalance(Long accountId, Class<T> responseType) {
        String url = baseUrl + "/accounts/{accountId}/balance";

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        Map<String, Long> params = new HashMap<>();
        params.put("accountId", accountId);

        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType, params);
    }

    public <T> ResponseEntity<T> executeTransfer(TransferRequest transferRequest, Class<T> responseType) {
        String url = baseUrl + "/accounts/{accountId}/payments/money-transfers";

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransferRequest> requestEntity = new HttpEntity<>(transferRequest, headers);

        Map<String, Long> params = new HashMap<>();
        params.put("accountId", accountId);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, params);
    }

    public <T> ResponseEntity<T> getTransactions(Long accountId, TransactionsRequest transactionsRequest, Class<T> responseType) {
        LocalDate fromDate = transactionsRequest.getFromDate();
        LocalDate toDate = transactionsRequest.getToDate();

        String url = baseUrl + "/accounts/" + accountId + "/transactions?"
                + "fromAccountingDate=" + fromDate + "&toAccountingDate=" + toDate;

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_SCHEMA, AUTH_SCHEMA_VALUE);
        headers.set(API_KEY, apiKey);
        return headers;
    }
}

