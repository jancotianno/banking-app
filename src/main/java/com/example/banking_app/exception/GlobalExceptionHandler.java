package com.example.banking_app.exception;

import com.example.banking_app.exception.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR = "ERROR";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Gestione dell'errore per argomenti non validi
        ErrorResponse errorResponse = new ErrorResponse(ERROR, List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException ex) {
        // Gestione degli errori HTTP, ad esempio 4xx o 5xx
        ErrorResponse errorResponse = new ErrorResponse(ERROR, List.of(ex.getMessage()));
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        // Gestione degli errori di runtime
        ErrorResponse errorResponse = new ErrorResponse(ERROR, List.of("Errore interno: " + ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Gestione di errori generici
        ErrorResponse errorResponse = new ErrorResponse(ERROR, List.of("Si è verificato un errore: " + ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}


