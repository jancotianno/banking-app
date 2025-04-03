package com.example.banking_app.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BalanceResponse {
    private String status;
    private String[] error;
    private BalancePayload payload;

    @Data
    public static class BalancePayload {
        private String date;
        private BigDecimal balance;
        private BigDecimal availableBalance;
        private String currency;
    }
}

