package com.example.banking_app.validator;

import com.example.banking_app.request.TransferRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class TransferRequestValidator implements ConstraintValidator<ValidTransferRequest, TransferRequest> {

    private static final Pattern IBAN_PATTERN = Pattern.compile("^IT\\d{2}[A-Z0-9]{22}$");

    @Override
    public boolean isValid(TransferRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getCreditor() == null || request.getCreditor().getAccount() == null) {
            return false;
        }

        // Validazione IBAN italiano
        String iban = request.getCreditor().getAccount().getAccountCode();
        boolean isIbanValid = iban != null && IBAN_PATTERN.matcher(iban).matches();

        // Validazione della data di esecuzione
        boolean isExecutionDateValid = false;
        if (request.getExecutionDate() != null) {
            try {
                LocalDate executionDate = LocalDate.parse(request.getExecutionDate());
                isExecutionDateValid = !executionDate.isBefore(LocalDate.now()); // Non deve essere nel passato
            } catch (DateTimeParseException e) {
                isExecutionDateValid = false;
            }
        }

        // Validazione dell'importo (deve essere positivo)
        boolean isAmountValid = request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) > 0;

        // Validazione della valuta (deve essere "EUR")
        boolean isCurrencyValid = "EUR".equals(request.getCurrency());

        return isIbanValid && isExecutionDateValid && isAmountValid && isCurrencyValid;
    }
}

