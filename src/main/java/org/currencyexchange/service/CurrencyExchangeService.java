package org.currencyexchange.service;

import org.currencyexchange.service.model.CurrencyExchangeDto;

import java.math.BigDecimal;

public interface CurrencyExchangeService {
    public CurrencyExchangeDto exchange(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount);
}