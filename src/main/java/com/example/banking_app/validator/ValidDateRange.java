package com.example.banking_app.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "Intervallo di date non valido: fromDate deve essere ≤ toDate e toDate non può essere nel futuro.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
