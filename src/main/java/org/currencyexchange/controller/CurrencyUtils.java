package org.currencyexchange.controller;

import java.util.List;

public class CurrencyUtils {
    public static List<String> splitCurrencyPair(String pair) {
        if (pair == null || pair.length() != 6) {
            throw new IllegalArgumentException("Invalid currency pair format " + pair);
        }
        pair.toUpperCase();
        String baseCurrency = pair.substring(0, 3);
        String targetCurrency = pair.substring(3, 6);
        return List.of(baseCurrency, targetCurrency);
    }
}