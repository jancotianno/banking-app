package com.example.banking_app.validator;

import com.example.banking_app.request.Account;
import com.example.banking_app.request.Creditor;
import com.example.banking_app.request.TransferRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class TransferRequestValidatorTest {

    private TransferRequestValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new TransferRequestValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    private TransferRequest createValidRequest() {
        Account account = new Account();
        account.setAccountCode("IT60X054281110100000123456");

        Creditor creditor = new Creditor();
        creditor.setAccount(account);

        TransferRequest request = new TransferRequest();
        request.setCreditor(creditor);
        request.setExecutionDate(LocalDate.now().toString());
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("EUR");

        return request;
    }

    @Test
    void shouldReturnTrueWhenValidRequest() {
        TransferRequest request = createValidRequest();
        assertTrue(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenRequestIsNull() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void shouldReturnFalseWhenCreditorIsNull() {
        TransferRequest request = createValidRequest();
        request.setCreditor(null);
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenAccountIsNull() {
        TransferRequest request = createValidRequest();
        request.getCreditor().setAccount(null);
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenIbanIsInvalid() {
        TransferRequest request = createValidRequest();
        request.getCreditor().getAccount().setAccountCode("INVALID_IBAN");
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenExecutionDateIsInPast() {
        TransferRequest request = createValidRequest();
        request.setExecutionDate(LocalDate.now().minusDays(1).toString());
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenExecutionDateIsInvalidFormat() {
        TransferRequest request = createValidRequest();
        request.setExecutionDate("invalid-date");
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenAmountIsZero() {
        TransferRequest request = createValidRequest();
        request.setAmount(BigDecimal.ZERO);
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenAmountIsNegative() {
        TransferRequest request = createValidRequest();
        request.setAmount(BigDecimal.valueOf(-50));
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenCurrencyIsNotEUR() {
        TransferRequest request = createValidRequest();
        request.setCurrency("USD");
        assertFalse(validator.isValid(request, context));
    }
}

