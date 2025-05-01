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
import java.util.Map;
import java.util.stream.Collectors;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateDao rateDao;
    private final CurrencyDao currencyDao;
    private final ExchangeRateMapper rateMapper;
    private final int FIRST_ELEMENT_INDEX = 0;
    private final int SECOND_ELEMENT_INDEX = 1;

    public ExchangeRateServiceImpl(
            ExchangeRateDao rateDao,
            CurrencyDao currencyDao,
            ExchangeRateMapper rateMapper) {
        this.rateDao = rateDao;
        this.currencyDao = currencyDao;
        this.rateMapper = rateMapper;
    }

    public List<ExchangeRateDto> getAllRates() {
        List<ExchangeRate> rates = rateDao.findAll()
                .orElseThrow(() -> new RuntimeException("No exchangeRateList found"));
        Map<Long, Currency> currencyMap = getAllCurrenciesAsMap();
        return rateMapper.toDto(rates, currencyMap);
    }

    private Map<Long, Currency> getAllCurrenciesAsMap() {
        List<Currency> currencies = currencyDao.findAll()
                .orElseThrow(() -> new RuntimeException("No currencies found"));
        Map<Long, Currency> currencyMap = currencies.stream()
                .collect(Collectors.toMap(Currency::getId, currency -> currency));
        return currencyMap;
    }

    public ExchangeRateDto getByCodePair(String pair) {
        List<Currency> currencies = getCurrenciesFromPair(pair);
        Currency baseCurrency = currencies.get(FIRST_ELEMENT_INDEX);
        Currency targetCurrency = currencies.get(SECOND_ELEMENT_INDEX);
        ExchangeRate exchangeRate = getExchangeRateByCurrencies(baseCurrency, targetCurrency);
        return rateMapper.toDto(exchangeRate, baseCurrency, targetCurrency);
    }

    public ExchangeRateDto add(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        List<Currency> currencies = getCurrenciesFromCodes(baseCurrencyCode, targetCurrencyCode);
        Currency baseCurrency = currencies.get(FIRST_ELEMENT_INDEX);
        Currency targetCurrency = currencies.get(SECOND_ELEMENT_INDEX);

        ExchangeRate rateToAdd = new ExchangeRate();
        rateToAdd.setBaseCurrencyId(baseCurrency.getId());
        rateToAdd.setTargetCurrencyId(targetCurrency.getId());
        rateToAdd.setRate(rate);

        rateToAdd = rateDao.save(rateToAdd).orElseThrow(() -> new RuntimeException("No rate added"));
        return rateMapper.toDto(rateToAdd, baseCurrency, targetCurrency);
    }

    public ExchangeRateDto updateByPair(String pair, BigDecimal rate) {
        List<Currency> currencies = getCurrenciesFromPair(pair);
        Currency baseCurrency = currencies.get(FIRST_ELEMENT_INDEX);
        Currency targetCurrency = currencies.get(SECOND_ELEMENT_INDEX);

        ExchangeRate rateToUpdate = getExchangeRateByCurrencies(baseCurrency, targetCurrency);
        rateToUpdate.setRate(rate);

        rateToUpdate = rateDao.update(rateToUpdate).orElseThrow(() -> new RuntimeException("No rate updated"));
        return rateMapper.toDto(rateToUpdate, baseCurrency, targetCurrency);
    }

    private List<Currency> getCurrenciesFromPair(String pair) {
        List<String> currencyCodes = CurrencyUtils.splitCurrencyPair(pair);
        return getCurrenciesFromCodes(currencyCodes.get(FIRST_ELEMENT_INDEX), currencyCodes.get(SECOND_ELEMENT_INDEX));
    }

    private List<Currency> getCurrenciesFromCodes(String baseCode, String targetCode) {
        return CurrencyUtils.getCurrencies(baseCode, targetCode, currencyDao);
    }

    private ExchangeRate getExchangeRateByCurrencies(Currency base, Currency target) {
        return rateDao.findByPairId(base.getId(), target.getId())
                .orElseThrow(() -> new RuntimeException("No exchangeRate found"));
    }

    public static void main(String[] args) {
        ExchangeRateDaoImpl rateDao = new ExchangeRateDaoImpl();
        CurrencyDaoImpl currencyDao = new CurrencyDaoImpl();
        ExchangeRateMapper rateMapper = new ExchangeRateMapper();
        ExchangeRateServiceImpl service = new ExchangeRateServiceImpl(rateDao, currencyDao, rateMapper);

        //String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate
//        service.add("usd", "rub", BigDecimal.valueOf(86.57));


//        service.updateByPair("usdeur", BigDecimal.valueOf(3));
        System.out.println(service.updateByPair("usdeur", BigDecimal.valueOf(3)));

//        List<ExchangeRateDto> rates = service.getAllRates();
//        System.out.println(rates);
        System.out.println(service.getByCodePair("usDeUr"));
//        System.out.println(service.getByCodePair("uSdRub"));
//        System.out.println(service.getByCodePair("eurRub"));
    }
}