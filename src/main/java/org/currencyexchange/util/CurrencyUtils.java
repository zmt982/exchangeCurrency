package org.currencyexchange.util;

import org.currencyexchange.database.entity.Currency;
import org.currencyexchange.database.repository.CurrencyDao;

import java.util.*;

public class CurrencyUtils {
    public static List<String> splitCurrencyPair(String pair) {
        if (pair == null || pair.length() != 6 || !pair.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Invalid currency pair format " + pair);
        }

        String baseCurrency = pair.substring(0, 3);
        String targetCurrency = pair.substring(3, 6);
        return List.of(baseCurrency, targetCurrency);
    }

    public static List<Currency> getCurrencies(String baseCurrencyCode,
                                               String targetCurrencyCode,
                                               CurrencyDao currencyDao) {
        String upperBaseCurrencyCode = baseCurrencyCode.toUpperCase();
        String upperTargetCurrencyCode = targetCurrencyCode.toUpperCase();
        List<String> errors = new ArrayList<>();

        Currency baseCurrency = currencyDao.findByCode(upperBaseCurrencyCode).
                orElseGet(() -> {
                    errors.add("No currency found for code: " + upperBaseCurrencyCode);
                    return null;
                });

        Currency targetCurrency = currencyDao.findByCode(upperTargetCurrencyCode).
                orElseGet(() -> {
                    errors.add("No currency found for code: " + upperTargetCurrencyCode);
                    return null;
                });

        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join("; ", errors));
        }

        return List.of(baseCurrency, targetCurrency);
    }
}