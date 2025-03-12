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

    public static List<Currency> getValidateCurrencies(String baseCurrencyCode, String targetCurrencyCode, CurrencyDao currencyDao) {
        String upperBaseCurrencyCode = baseCurrencyCode.toUpperCase();
        String upperTargetCurrencyCode = targetCurrencyCode.toUpperCase();
        List<String> errors = new ArrayList<>();
        Map<String, Currency> currencyMap = new HashMap();

        for (String code : List.of(upperBaseCurrencyCode, upperTargetCurrencyCode)) {
            currencyDao.findByCode(code).ifPresentOrElse(
                    currency -> currencyMap.put(code, currency),
                    () -> errors.add("No currency found for code: " + code)
            );
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join("; ", errors));
        }

        return List.of(currencyMap.get(upperBaseCurrencyCode), currencyMap.get(upperTargetCurrencyCode));
    }
}