package com.example.banking_app.request;

import com.example.banking_app.validator.ValidDateRange;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@ValidDateRange
public class TransactionsRequest {

    @NotNull(message = "La data di inizio è obbligatoria.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromDate;

    @NotNull(message = "La data di fine è obbligatoria.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toDate;

}

