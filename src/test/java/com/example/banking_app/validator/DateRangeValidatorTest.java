package com.example.banking_app.validator;

import com.example.banking_app.request.TransactionsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DateRangeValidatorTest {

    private DateRangeValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new DateRangeValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void shouldReturnFalseWhenRequestIsNull() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void shouldReturnFalseWhenFromDateIsNull() {
        TransactionsRequest request = new TransactionsRequest();
        request.setFromDate(null);
        request.setToDate(LocalDate.now());
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenToDateIsNull() {
        TransactionsRequest request = new TransactionsRequest();
        request.setFromDate(LocalDate.now());
        request.setToDate(null);
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenFromDateIsAfterToDate() {
        TransactionsRequest request = new TransactionsRequest();
        request.setFromDate(LocalDate.of(2025, 4, 10));
        request.setToDate(LocalDate.of(2025, 4, 5));
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnFalseWhenToDateIsAfterToday() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        TransactionsRequest request = new TransactionsRequest();
        request.setFromDate(LocalDate.now().minusDays(10));
        request.setToDate(tomorrow);
        assertFalse(validator.isValid(request, context));
    }

    @Test
    void shouldReturnTrueWhenDatesAreValid() {
        TransactionsRequest request = new TransactionsRequest();
        request.setFromDate(LocalDate.now().minusDays(10));
        request.setToDate(LocalDate.now());
        assertTrue(validator.isValid(request, context));
    }
}

