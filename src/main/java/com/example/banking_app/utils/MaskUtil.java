package com.example.banking_app.utils;

public class MaskUtil {

    public static String maskIban(String iban) {
        if (iban == null || iban.length() < 4) return "****";
        return "****" + iban.substring(iban.length() - 4);
    }
}
