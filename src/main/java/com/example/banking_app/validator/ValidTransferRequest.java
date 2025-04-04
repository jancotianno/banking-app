package com.example.banking_app.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TransferRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTransferRequest {
    String message() default "Dati di trasferimento non validi.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

