package com.example.banking_app.service;

import com.example.banking_app.constant.ConstantUtils;
import com.example.banking_app.request.TransactionsRequest;
import com.example.banking_app.request.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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

        log.info("Richiesta saldo per account ID: {}, URL: {}", accountId, url);

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        Map<String, Long> params = new HashMap<>();
        params.put(ConstantUtils.ACCOUNT_ID, accountId);

        try {
            log.info("Invio richiesta per saldo account ID: {}", accountId);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType, params);
            log.info("Risposta ricevuta per account ID: {} con status: {}", accountId, response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Errore durante la richiesta saldo per account ID: {}, Errore: {}", accountId, e.getMessage());
            throw e;
        }
    }

    public <T> ResponseEntity<T> executeTransfer(TransferRequest transferRequest, Class<T> responseType) {
        String url = baseUrl + operationTransferUrl.replace("{accountId}", accountId.toString());

        log.info("Esecuzione bonifico, URL: {}", url);

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransferRequest> requestEntity = new HttpEntity<>(transferRequest, headers);

        Map<String, Long> params = new HashMap<>();
        params.put(ConstantUtils.ACCOUNT_ID, accountId);

        try {
            log.info("Invio richiesta per bonifico, importo: {}, destinazione IBAN: {}",
                    transferRequest.getAmount(), transferRequest.getCreditor().getAccount().getAccountCode());

            requestEntity.getHeaders().forEach((key, value) -> log.info("{}: {}", key, value));

            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, params);
            log.info("Bonifico eseguito con successo, stato: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Errore durante l'esecuzione del bonifico, errore: {}", e.getMessage());
            throw e;
        }
    }

    public <T> ResponseEntity<T> getTransactions(Long accountId, TransactionsRequest transactionsRequest, Class<T> responseType) {
        LocalDate fromDate = transactionsRequest.getFromDate();
        LocalDate toDate = transactionsRequest.getToDate();

        String url = baseUrl + operationTransactionsUrl
                .replace("{accountId}", accountId.toString())
                .replace("{fromDate}", fromDate.toString())
                .replace("{toDate}", toDate.toString());

        log.info("Richiesta transazioni per account ID: {}, dal: {} al: {}, URL: {}",
                accountId, fromDate, toDate, url);

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            log.info("Invio richiesta per transazioni da {} a {}", fromDate, toDate);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
            log.info("Transazioni ricevute con successo per account ID: {}", accountId);
            return response;
        } catch (Exception e) {
            log.error("Errore durante la richiesta transazioni per account ID: {}, errore: {}", accountId, e.getMessage());
            throw e;
        }
    }

    HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(ConstantUtils.AUTH_SCHEMA, ConstantUtils.AUTH_SCHEMA_VALUE);
        headers.set(ConstantUtils.API_KEY, apiKey);
        return headers;
    }
}

