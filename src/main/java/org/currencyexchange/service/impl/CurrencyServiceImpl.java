package org.currencyexchange.service.impl;

import org.currencyexchange.database.entity.Currency;
import org.currencyexchange.database.repository.impl.CurrencyDaoImpl;
import org.currencyexchange.service.CurrencyService;
import org.currencyexchange.service.mapper.CurrencyMapper;
import org.currencyexchange.service.model.CurrencyDto;

import java.util.List;
import java.util.Optional;

public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyDaoImpl currencyDao;
    private final CurrencyMapper currencyMapper;

    public CurrencyServiceImpl(CurrencyDaoImpl currencyDao, CurrencyMapper currencyMapper) {
        this.currencyDao = currencyDao;
        this.currencyMapper = currencyMapper;
    }

    public List<CurrencyDto> getAllCurrencies() {
        Optional<List<Currency>> optionalCurrencies = currencyDao.findAll();
        List<Currency> currencies = optionalCurrencies.orElseThrow(() -> new RuntimeException("No currencies found"));
        return currencyMapper.toDto(currencies);
    }

    public CurrencyDto getCurrencyByCode(String code) {
        Optional<Currency> optionalCurrencyToFind = currencyDao.findByCode(code.toUpperCase());
        Currency currency = optionalCurrencyToFind.orElseThrow(() -> new RuntimeException("No currency found"));
        return currencyMapper.toDto(currency);
    }

    public CurrencyDto add(CurrencyDto addDto) {
        Currency currencyToAdd = currencyMapper.toCurrency(addDto);
        Optional<Currency> optionalCurrency = currencyDao.save(currencyToAdd);
        currencyToAdd = optionalCurrency.orElseThrow(() -> new RuntimeException("No currency added"));
        return currencyMapper.toDto(currencyToAdd);
    }

    public static void main(String[] args) {
        CurrencyDaoImpl dao = new CurrencyDaoImpl();
        CurrencyMapper mapper = new CurrencyMapper();
        CurrencyServiceImpl service = new CurrencyServiceImpl(dao, mapper);

//        CurrencyDto dto = new CurrencyDto();
//        dto.setCode("EUR");
//        dto.setFullName("Euro");
//        dto.setSign("�");
//
//        service.add(dto);
        List<CurrencyDto> list = service.getAllCurrencies();
        System.out.println(service.getCurrencyByCode("eUr"));
        System.out.println(list);
    }
}