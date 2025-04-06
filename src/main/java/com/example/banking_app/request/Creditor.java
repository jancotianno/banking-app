package com.example.banking_app.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Creditor {
    @NotBlank(message = "Il nome del creditore è obbligatorio.")
    private String name;

    @NotNull(message = "L'account è obbligatorio.")
    private Account account;
}
