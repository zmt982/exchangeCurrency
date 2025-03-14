package org.currencyexchange.service.mapper;

import org.currencyexchange.database.entity.Currency;
import org.currencyexchange.database.entity.ExchangeRate;
import org.currencyexchange.service.model.CurrencyDto;
import org.currencyexchange.service.model.ExchangeRateDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExchangeRateMapper {
    public ExchangeRateDto toDto(ExchangeRate exchangeRate, Currency baseCurrency, Currency targetCurrency) {
        if (exchangeRate == null || baseCurrency == null || targetCurrency == null) {
            return null;
        }

        return new ExchangeRateDto(exchangeRate.getId(),
                new CurrencyDto(baseCurrency),
                new CurrencyDto(targetCurrency),
                exchangeRate.getRate());
    }

    public ExchangeRate toRate(ExchangeRateDto exchangeRateDto) {
        if (exchangeRateDto == null) {
            return null;
        }

        final ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrencyId(exchangeRateDto.getBaseCurrency().getId());
        exchangeRate.setTargetCurrencyId(exchangeRateDto.getTargetCurrency().getId());
        exchangeRate.setRate(exchangeRateDto.getRate());

        return exchangeRate;
    }

    public List<ExchangeRateDto> toDto(
            List<ExchangeRate> rates,
            Map<Long, Currency> currencies) {
        if (rates == null || currencies == null) {
            return List.of();
        }
        return rates.stream()
                .map(rate -> toDto(
                        rate,
                        currencies.get(rate.getBaseCurrencyId()),
                        currencies.get(rate.getTargetCurrencyId())
                ))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<ExchangeRate> toRate(List<ExchangeRateDto> dtoList) {
        return dtoList.stream().map(this::toRate).filter(Objects::nonNull).collect(Collectors.toList());
    }
}