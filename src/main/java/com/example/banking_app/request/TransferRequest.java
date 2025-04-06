package com.example.banking_app.request;

import com.example.banking_app.validator.ValidTransferRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidTransferRequest
public class TransferRequest {

    @NotNull(message = "Il creditore è obbligatorio.")
    private Creditor creditor;

    @NotBlank(message = "La data di esecuzione è obbligatoria.")
    private String executionDate;

    @NotNull(message = "L'importo è obbligatorio.")
    @Positive(message = "L'importo deve essere maggiore di zero.")
    private BigDecimal amount;

    @NotBlank(message = "La valuta è obbligatoria.")
    private String currency;

    @NotBlank(message = "La descrizione è obbligatoria.")
    private String description;
}

