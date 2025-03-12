package org.currencyexchange.service.impl;

import org.currencyexchange.database.entity.Currency;
import org.currencyexchange.database.repository.CurrencyDao;
import org.currencyexchange.database.repository.ExchangeRateDao;
import org.currencyexchange.database.repository.impl.CurrencyDaoImpl;
import org.currencyexchange.util.CurrencyUtils;
import org.currencyexchange.database.entity.ExchangeRate;
import org.currencyexchange.database.repository.impl.ExchangeRateDaoImpl;
import org.currencyexchange.service.ExchangeRateService;
import org.currencyexchange.service.mapper.ExchangeRateMapper;
import org.currencyexchange.service.model.ExchangeRateDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateDao rateDao;
    private final CurrencyDao currencyDao;
    private final ExchangeRateMapper rateMapper;

    public ExchangeRateServiceImpl(ExchangeRateDao rateDao, CurrencyDao currencyDao, ExchangeRateMapper rateMapper) {
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
        List<String> codes = CurrencyUtils.splitCurrencyPair(pair);
        String baseCurrencyCode = codes.get(0);
        String targetCurrencyCode = codes.get(1);

        List<Currency> currencies = CurrencyUtils
                .getValidateCurrencies(baseCurrencyCode, targetCurrencyCode, currencyDao);
        Currency baseCurrency = currencies.get(0);
        Currency targetCurrency = currencies.get(1);
        long baseCurrencyId = baseCurrency.getId();
        long targetCurrencyId = targetCurrency.getId();

        Optional<ExchangeRate> optionalRate = rateDao.findByPairId(baseCurrencyId, targetCurrencyId);
        return optionalRate.orElseThrow(() -> new RuntimeException("No exchangeRate found"));
    }

    public ExchangeRateDto getByCodePair(String pair) {
        ExchangeRate exchangeRate = findByPair(pair);
        return rateMapper.toDto(exchangeRate);
    }

    public ExchangeRateDto add(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        List<Currency> currencies = CurrencyUtils
                .getValidateCurrencies(baseCurrencyCode, targetCurrencyCode, currencyDao);
        Currency baseCurrency = currencies.get(0);
        Currency targetCurrency = currencies.get(1);

        ExchangeRate rateToAdd = new ExchangeRate();
        rateToAdd.setBaseCurrencyId(baseCurrency.getId());
        rateToAdd.setTargetCurrencyId(targetCurrency.getId());
        rateToAdd.setRate(rate);

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

        //String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate
        service.add("usd", "rub", BigDecimal.valueOf(86.57));


//        service.updateByPair("usdeur", BigDecimal.valueOf(3));
//        System.out.println(service.updateByPair("usdeur", BigDecimal.valueOf(0.92)));

        List<ExchangeRateDto> rates = service.getAllRates();
        System.out.println(rates);
        System.out.println(service.getByCodePair("usdeur"));
    }
}