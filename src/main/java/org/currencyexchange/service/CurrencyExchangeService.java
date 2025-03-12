package org.currencyexchange.service;

import org.currencyexchange.service.model.ExchangeResultDto;

import java.math.BigDecimal;

public interface CurrencyExchangeService {
    public ExchangeResultDto exchange(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount);
}