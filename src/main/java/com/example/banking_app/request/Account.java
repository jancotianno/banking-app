package com.example.banking_app.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @NotBlank(message = "L'IBAN è obbligatorio.")
    private String accountCode;
}
