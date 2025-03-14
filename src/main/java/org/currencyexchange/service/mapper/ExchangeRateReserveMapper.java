package org.currencyexchange.service.mapper;

import org.currencyexchange.database.entity.ExchangeRate;
import org.currencyexchange.service.model.ExchangeRateReserveDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExchangeRateReserveMapper {
    public ExchangeRateReserveDto toDto(ExchangeRate exchangeRate) {
        if (exchangeRate == null) {
            return null;
        }

        final ExchangeRateReserveDto exchangeRateReserveDto = new ExchangeRateReserveDto();
        exchangeRateReserveDto.setId(exchangeRate.getId());
        exchangeRateReserveDto.setBaseCurrencyId(exchangeRate.getBaseCurrencyId());
        exchangeRateReserveDto.setTargetCurrencyId(exchangeRate.getTargetCurrencyId());
        exchangeRateReserveDto.setRate(exchangeRate.getRate());

        return exchangeRateReserveDto;
    }

    public ExchangeRate toRate(ExchangeRateReserveDto exchangeRateReserveDto) {
        if (exchangeRateReserveDto == null) {
            return null;
        }

        final ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrencyId(exchangeRateReserveDto.getBaseCurrencyId());
        exchangeRate.setTargetCurrencyId(exchangeRateReserveDto.getTargetCurrencyId());
        exchangeRate.setRate(exchangeRateReserveDto.getRate());

        return exchangeRate;
    }

    public List<ExchangeRateReserveDto> toDto(List<ExchangeRate> rates) {
        return rates.stream().map(this::toDto).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<ExchangeRate> toRate(List<ExchangeRateReserveDto> dtoList) {
        return dtoList.stream().map(this::toRate).filter(Objects::nonNull).collect(Collectors.toList());
    }
}