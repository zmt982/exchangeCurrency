package org.currencyexchange.database.repository;

import org.currencyexchange.database.entity.ExchangeRate;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateDao {
    public Optional<List<ExchangeRate>> findAll();

    public Optional<ExchangeRate> findByPairId(long baseCurrencyId, long targetCurrencyId);

    public Optional<ExchangeRate> save(ExchangeRate rate);

    public Optional<ExchangeRate> update(ExchangeRate rateToUpdate);
}