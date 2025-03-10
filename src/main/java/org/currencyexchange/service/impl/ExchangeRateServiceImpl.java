package org.currencyexchange.service.impl;

import org.currencyexchange.database.entity.Currency;
import org.currencyexchange.database.repository.impl.CurrencyDaoImpl;
import org.currencyexchange.controller.CurrencyUtils;
import org.currencyexchange.database.entity.ExchangeRate;
import org.currencyexchange.database.repository.impl.ExchangeRateDaoImpl;
import org.currencyexchange.service.ExchangeRateService;
import org.currencyexchange.service.mapper.ExchangeRateMapper;
import org.currencyexchange.service.model.ExchangeRateDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateDaoImpl rateDao;
    private final CurrencyDaoImpl currencyDao;
    private final ExchangeRateMapper rateMapper;

    public ExchangeRateServiceImpl(ExchangeRateDaoImpl rateDao, CurrencyDaoImpl currencyDao,
                                   ExchangeRateMapper rateMapper) {
        this.rateDao = rateDao;
        this.currencyDao = currencyDao;
        this.rateMapper = rateMapper;
    }

    public List<ExchangeRateDto> getAllRates() {
        Optional<List<ExchangeRate>> optionalRates = rateDao.findAll();
        List<ExchangeRate> rates = optionalRates.orElseThrow(() -> new RuntimeException("No exchangeRate found"));
        return rateMapper.toDto(rates);
    }

    private ExchangeRate findByPair(String pair) {
        List<String> codes = CurrencyUtils.splitCurrencyPair(pair.toUpperCase());
        String baseCurrencyCode = codes.get(0);
        String targetCurrencyCode = codes.get(1);

        Optional<Currency> optionalBaseCurrency = currencyDao.findByCode(baseCurrencyCode);
        Optional<Currency> optionalTargetCurrency = currencyDao.findByCode(targetCurrencyCode);

        Currency baseCurrency = optionalBaseCurrency.orElseThrow(() -> new RuntimeException("No baseCurrency found"));
        Currency targetCurrency = optionalTargetCurrency
                .orElseThrow(() -> new RuntimeException("No targetCurrency found"));

        long baseCurrencyId = baseCurrency.getId();
        long targetCurrencyId = targetCurrency.getId();

        Optional<ExchangeRate> optionalRate = rateDao.findByPairId(baseCurrencyId, targetCurrencyId);
        ExchangeRate rate = optionalRate.orElseThrow(() -> new RuntimeException("No exchangeRate found"));
        return rate;
    }

    public ExchangeRateDto getByCodePair(String pair) {
        ExchangeRate exchangeRate = findByPair(pair);
        return rateMapper.toDto(exchangeRate);
    }

    public ExchangeRateDto add(ExchangeRateDto rateDto) {
        ExchangeRate rateToAdd = rateMapper.toRate(rateDto);
        Optional<ExchangeRate> optionalRate = rateDao.save(rateToAdd);
        rateToAdd = optionalRate.orElseThrow(() -> new RuntimeException("No rate added"));
        return rateMapper.toDto(rateToAdd);
    }

    public ExchangeRateDto updateByPair(String pair, BigDecimal rate) {
        ExchangeRate rateToUpdate = findByPair(pair);
        rateToUpdate.setRate(rate);
        Optional<ExchangeRate> optionalRate = rateDao.update(rateToUpdate);
        rateToUpdate = optionalRate.orElseThrow(() -> new RuntimeException("No rate updated"));
        return rateMapper.toDto(rateToUpdate);
    }

    public static void main(String[] args) {
        ExchangeRateDaoImpl rateDao = new ExchangeRateDaoImpl();
        CurrencyDaoImpl currencyDao = new CurrencyDaoImpl();
        ExchangeRateMapper mapper = new ExchangeRateMapper();
        ExchangeRateServiceImpl service = new ExchangeRateServiceImpl(rateDao, currencyDao, mapper);

        ExchangeRateDto rateDto = new ExchangeRateDto();
        rateDto.setBaseCurrencyId(1);
        rateDto.setTargetCurrencyId(2);
        rateDto.setRate(BigDecimal.valueOf(2));
//        service.add(rateDto);


//        service.update("usdeur", BigDecimal.valueOf(4));
        System.out.println(service.updateByPair("usdeur", BigDecimal.valueOf(4)));

        List<ExchangeRateDto> rates = service.getAllRates();
        System.out.println(rates);
        System.out.println(service.getByCodePair("usdeur"));
    }
}