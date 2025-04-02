package com.example.banking_app.service;

import com.example.banking_app.dto.TransferRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class FabrickApiClient {

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

    public ResponseEntity<String> getAccountBalance(Long accountId) {
        String url = baseUrl + "/accounts/{accountId}/balance";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth-Schema", "S2S");
        headers.set("Api-Key", apiKey);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        Map<String, Long> params = new HashMap<>();
        params.put("accountId", accountId);

        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, params);
    }

    public ResponseEntity<String> executeTransfer(TransferRequest transferRequest) {
        String url = baseUrl + "/accounts/{accountId}/payments/money-transfers";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth-Schema", "S2S");
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransferRequest> requestEntity = new HttpEntity<>(transferRequest, headers);

        Map<String, Long> params = new HashMap<>();
        params.put("accountId", accountId);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, params);
    }

    public String getTransactions(Long accountId, String fromDate, String toDate) {
        String url = baseUrl + "/accounts/" + accountId + "/transactions?"
                + "fromAccountingDate=" + fromDate + "&toAccountingDate=" + toDate;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth-Schema", "S2S");
        headers.set("Api-Key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        return response.getBody();
    }

}

