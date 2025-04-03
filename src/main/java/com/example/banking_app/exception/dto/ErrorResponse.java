package com.example.banking_app.exception.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponse {
    private String status;
    private List<String> error;
    private LocalDateTime timestamp;

    public ErrorResponse(String status, List<String> error) {
        this.status = status;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}


