package org.currencyexchange.service.mapper;

import org.currencyexchange.database.entity.Currency;
import org.currencyexchange.service.model.CurrencyDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CurrencyMapper {
    public CurrencyDto toDto(Currency currency) {
        if (currency == null) {
            return null;
        }

        return new CurrencyDto(currency);
    }

    public Currency toCurrency(CurrencyDto currencyDto) {
        if (currencyDto == null) {
            return null;
        }

        final Currency currency = new Currency();
        currency.setCode(currencyDto.getCode());
        currency.setFullName(currencyDto.getFullName());
        currency.setSign(currencyDto.getSign());

        return currency;
    }

    public List<CurrencyDto> toDto(List<Currency> currencies) {
        return currencies.stream().map(this::toDto).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Currency> toCurrency(List<CurrencyDto> dtoList) {
        return dtoList.stream().map(this::toCurrency).filter(Objects::nonNull).collect(Collectors.toList());
    }
}