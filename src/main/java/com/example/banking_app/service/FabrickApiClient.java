package com.example.banking_app.service;

import com.example.banking_app.constant.ConstantUtils;
import com.example.banking_app.request.TransactionsRequest;
import com.example.banking_app.request.TransferRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class FabrickApiClient {

    @Value("${fabrick.api.base-url}")
    private String baseUrl;

    @Value("${op.account.balance.url}")
    private String operationBalanceUrl;

    @Value("${op.account.transactions.url}")
    private String operationTransactionsUrl;

    @Value("${op.account.transfer.url}")
    private String operationTransferUrl;

    @Value("${fabrick.api.account-id}")
    private Long accountId;

    @Value("${fabrick.api.auth-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public FabrickApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public <T> ResponseEntity<T> getAccountBalance(Long accountId, Class<T> responseType) {
        String url = baseUrl + operationBalanceUrl.replace("{accountId}", accountId.toString());

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        Map<String, Long> params = new HashMap<>();
        params.put(ConstantUtils.ACCOUNT_ID, accountId);

        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType, params);
    }

    public <T> ResponseEntity<T> executeTransfer(TransferRequest transferRequest, Class<T> responseType) {
        String url = baseUrl + operationTransferUrl.replace("{accountId}", accountId.toString());

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransferRequest> requestEntity = new HttpEntity<>(transferRequest, headers);

        Map<String, Long> params = new HashMap<>();
        params.put(ConstantUtils.ACCOUNT_ID, accountId);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, params);
    }

    public <T> ResponseEntity<T> getTransactions(Long accountId, TransactionsRequest transactionsRequest, Class<T> responseType) {
        LocalDate fromDate = transactionsRequest.getFromDate();
        LocalDate toDate = transactionsRequest.getToDate();

        String url = baseUrl + operationTransactionsUrl
                .replace("{accountId}", accountId.toString())
                .replace("{fromDate}", fromDate.toString())
                .replace("{toDate}", toDate.toString());

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
    }

    HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ConstantUtils.AUTH_SCHEMA, ConstantUtils.AUTH_SCHEMA_VALUE);
        headers.set(ConstantUtils.API_KEY, apiKey);
        return headers;
    }
}

