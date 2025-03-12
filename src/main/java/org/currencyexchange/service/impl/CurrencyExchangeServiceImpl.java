package org.currencyexchange.service.impl;

import org.currencyexchange.database.entity.Currency;
import org.currencyexchange.database.entity.ExchangeRate;
import org.currencyexchange.database.repository.CurrencyDao;
import org.currencyexchange.database.repository.ExchangeRateDao;
import org.currencyexchange.database.repository.impl.CurrencyDaoImpl;
import org.currencyexchange.database.repository.impl.ExchangeRateDaoImpl;
import org.currencyexchange.service.CurrencyService;
import org.currencyexchange.service.CurrencyExchangeService;
import org.currencyexchange.service.mapper.CurrencyMapper;
import org.currencyexchange.service.model.CurrencyDto;
import org.currencyexchange.service.model.ExchangeResultDto;
import org.currencyexchange.util.CurrencyUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private final CurrencyDao currencyDao;
    private final CurrencyMapper currencyMapper;
    private final ExchangeRateDao exchangeRateDao;
    private final String USD_CURRENCY_CODE = "USD";

    public CurrencyExchangeServiceImpl(CurrencyDao currencyDao, CurrencyMapper currencyMapper,
                                       ExchangeRateDao exchangeRateDao) {
        this.currencyDao = currencyDao;
        this.currencyMapper = currencyMapper;
        this.exchangeRateDao = exchangeRateDao;
    }

    @Override
    public ExchangeResultDto exchange(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        List<Currency> currencies = CurrencyUtils
                .getValidateCurrencies(baseCurrencyCode, targetCurrencyCode, currencyDao);

        Currency baseCurrency = currencies.get(0);
        Currency targetCurrency = currencies.get(1);
        BigDecimal rate = getExchangeRate(baseCurrency, targetCurrency);

        BigDecimal convertedAmount = rate.multiply(amount).setScale(2, RoundingMode.HALF_UP);

        ExchangeResultDto result = new ExchangeResultDto();
        result.setBaseCurrency(currencyMapper.toDto(baseCurrency));
        result.setTargetCurrency(currencyMapper.toDto(targetCurrency));
        result.setRate(rate);
        result.setAmount(amount);
        result.setConvertedAmount(convertedAmount);
        return result;
    }

    private BigDecimal getExchangeRate(Currency baseCurrency, Currency targetCurrency) {
        Optional<ExchangeRate> directRate = exchangeRateDao.findByPairId(baseCurrency.getId(), targetCurrency.getId());
        if (directRate.get().getRate() != null) {
            return directRate.get().getRate();
        }

        Optional<ExchangeRate> reverseRate = exchangeRateDao.findByPairId(targetCurrency.getId(), baseCurrency.getId());
        if (reverseRate.get().getRate() != null) {
            return BigDecimal.ONE.divide(reverseRate.get().getRate(), 6, RoundingMode.HALF_UP);
        }

        Optional<Currency> optionalUsd = currencyDao.findByCode(USD_CURRENCY_CODE);
        Currency usd = optionalUsd.orElseThrow(() -> new RuntimeException("USD currency not found"));
        Optional<ExchangeRate> usdToBase = exchangeRateDao.findByPairId(usd.getId(), baseCurrency.getId());
        Optional<ExchangeRate> usdToTarget = exchangeRateDao.findByPairId(usd.getId(), targetCurrency.getId());
        if (usdToBase.isPresent() && usdToTarget.isPresent()) {
            return usdToTarget.get().getRate().divide(usdToBase.get().getRate(), 6, RoundingMode.HALF_UP);
        }
        throw new RuntimeException("Exchange rate not found for pair: " + baseCurrency.getCode() +
                targetCurrency.getCode());
    }

    public static void main(String[] args) {
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        CurrencyMapper currencyMapper = new CurrencyMapper();
        ExchangeRateDao rateDao = new ExchangeRateDaoImpl();
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeServiceImpl(currencyDao, currencyMapper, rateDao);

        String usd = "Usq";
        String eur = "eUq";
        String rub = "ruq";

        //String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount
        ExchangeResultDto exchange = currencyExchangeService.exchange(usd, eur, BigDecimal.valueOf(2));
        ExchangeResultDto exchange2 = currencyExchangeService.exchange(eur, usd, BigDecimal.valueOf(2));
        ExchangeResultDto exchange3 = currencyExchangeService.exchange(eur, rub, BigDecimal.valueOf(2));

        System.out.println(exchange);
        System.out.println(exchange2);
        System.out.println(exchange3);
    }
}