package org.currencyexchange.service.mapper;

import org.currencyexchange.database.entity.ExchangeRate;
import org.currencyexchange.service.model.ExchangeRateDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExchangeRateMapper {
    public ExchangeRateDto toDto(ExchangeRate exchangeRate) {
        if (exchangeRate == null) {
            return null;
        }

        final ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setId(exchangeRate.getId());
        exchangeRateDto.setBaseCurrencyId(exchangeRate.getBaseCurrencyId());
        exchangeRateDto.setTargetCurrencyId(exchangeRate.getTargetCurrencyId());
        exchangeRateDto.setRate(exchangeRate.getRate());

        return exchangeRateDto;
    }

    public ExchangeRate toRate(ExchangeRateDto exchangeRateDto) {
        if (exchangeRateDto == null) {
            return null;
        }

        final ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrencyId(exchangeRateDto.getBaseCurrencyId());
        exchangeRate.setTargetCurrencyId(exchangeRateDto.getTargetCurrencyId());
        exchangeRate.setRate(exchangeRateDto.getRate());

        return exchangeRate;
    }

    public List<ExchangeRateDto> toDto(List<ExchangeRate> rates) {
        return rates.stream().map(this::toDto).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<ExchangeRate> toRate(List<ExchangeRateDto> dtoList) {
        return dtoList.stream().map(this::toRate).filter(Objects::nonNull).collect(Collectors.toList());
    }
}