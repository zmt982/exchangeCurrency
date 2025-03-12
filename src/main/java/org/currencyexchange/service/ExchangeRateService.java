package org.currencyexchange.service;

import org.currencyexchange.service.model.ExchangeRateDto;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateService {
    public List<ExchangeRateDto> getAllRates();

    public ExchangeRateDto getByCodePair(String pair);

    public ExchangeRateDto add(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);

    public ExchangeRateDto updateByPair(String pair, BigDecimal rate);
}