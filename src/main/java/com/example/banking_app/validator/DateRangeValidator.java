package com.example.banking_app.validator;

import com.example.banking_app.request.TransactionsRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, TransactionsRequest> {

    @Override
    public boolean isValid(TransactionsRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getFromDate() == null || request.getToDate() == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        boolean isFromDateValid = !request.getFromDate().isAfter(request.getToDate());
        boolean isToDateValid = !request.getToDate().isAfter(today);

        return isFromDateValid && isToDateValid;
    }
}

