package com.example.banking_app.service;

import com.example.banking_app.constant.ConstantUtils;
import com.example.banking_app.request.TransactionsRequest;
import com.example.banking_app.request.TransferRequest;
import com.example.banking_app.utils.MaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        Map<String, Long> params = new HashMap<>();
        params.put(ConstantUtils.ACCOUNT_ID, accountId);

        try {
            log.info("Invio richiesta saldo");
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType, params);
            log.info("Risposta ricevuta per il conto: {} con status: {}", accountId, response.getStatusCode());
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Errore HTTP durante richiesta saldo per il conto {}: {}, body: {}",
                    accountId, e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Errore durante la richiesta saldo per il conto: {}, Errore: {}", accountId, e.getMessage());
            throw e;
        }
    }

    public <T> ResponseEntity<T> executeTransfer(TransferRequest transferRequest, Class<T> responseType) {
        String url = baseUrl + operationTransferUrl.replace("{accountId}", accountId.toString());

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransferRequest> requestEntity = new HttpEntity<>(transferRequest, headers);

        Map<String, Long> params = new HashMap<>();
        params.put(ConstantUtils.ACCOUNT_ID, accountId);

        try {
            String maskedIban = MaskUtil.maskIban(transferRequest.getCreditor().getAccount().getAccountCode());

            log.info("Invio richiesta bonifico, importo: {}, per l'IBAN: {}",
                    transferRequest.getAmount(), maskedIban);

            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, params);
            log.info("Bonifico eseguito con successo, stato: {}", response.getStatusCode());
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Errore HTTP durante bonifico: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
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

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
            log.info("Movimenti ricevuti con successo con status: {}", response.getStatusCode());
            return response;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Errore HTTP durante richiesta movimenti il conto {}: {}, body: {}",
                    accountId, e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Errore durante la richiesta transazioni per il conto: {}, errore: {}", accountId, e.getMessage());
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