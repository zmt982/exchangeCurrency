package org.currencyexchange.service.impl;

import org.currencyexchange.database.entity.Currency;
import org.currencyexchange.database.repository.CurrencyDao;
import org.currencyexchange.database.repository.impl.CurrencyDaoImpl;
import org.currencyexchange.service.CurrencyService;
import org.currencyexchange.service.mapper.CurrencyMapper;
import org.currencyexchange.service.model.CurrencyDto;

import java.util.List;
import java.util.Optional;

public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyDao currencyDao;
    private final CurrencyMapper currencyMapper;

    public CurrencyServiceImpl(CurrencyDao currencyDao, CurrencyMapper currencyMapper) {
        this.currencyDao = currencyDao;
        this.currencyMapper = currencyMapper;
    }

    public List<CurrencyDto> getAllCurrencies() {
        Optional<List<Currency>> optionalCurrencies = currencyDao.findAll();
        List<Currency> currencies = optionalCurrencies.orElseThrow(() -> new RuntimeException("No currencies found"));
        return currencyMapper.toDto(currencies);
    }

    public CurrencyDto getCurrencyByCode(String code) {
        Currency currency = currencyDao.findByCode(code.toUpperCase())
                .orElseThrow(() -> new RuntimeException("No currency found"));
        return currencyMapper.toDto(currency);
    }

    public CurrencyDto add(String name, String code, String sign) {
        Currency currencyToAdd = new Currency();
        currencyToAdd.setCode(code.toUpperCase());
        currencyToAdd.setFullName(name);
        currencyToAdd.setSign(sign);
        currencyToAdd = currencyDao.save(currencyToAdd)
                .orElseThrow(() -> new RuntimeException("No currency added"));
        return currencyMapper.toDto(currencyToAdd);
    }

    public static void main(String[] args) {
        CurrencyDao dao = new CurrencyDaoImpl();
        CurrencyMapper mapper = new CurrencyMapper();
        CurrencyServiceImpl service = new CurrencyServiceImpl(dao, mapper);

        List<CurrencyDto> list = service.getAllCurrencies();
        Currency curr = dao.findById(3).orElseThrow(() -> new RuntimeException("No test currency found"));
        System.out.println(curr);
        System.out.println(service.getCurrencyByCode("uSd"));
        System.out.println(service.getCurrencyByCode("euR"));
        System.out.println(service.getCurrencyByCode("RUb"));
        System.out.println(list);
    }
}