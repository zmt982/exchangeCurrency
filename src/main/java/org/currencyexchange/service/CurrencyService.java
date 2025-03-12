package org.currencyexchange.service;

import org.currencyexchange.service.model.CurrencyDto;

import java.util.List;

public interface CurrencyService {
    public List<CurrencyDto> getAllCurrencies();

    public CurrencyDto getCurrencyByCode(String code);

    public CurrencyDto add(String name, String code, String sign);
}