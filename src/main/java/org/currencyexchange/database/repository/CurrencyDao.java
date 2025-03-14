package org.currencyexchange.database.repository;

import org.currencyexchange.database.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDao {
    public Optional<List<Currency>> findAll();

    Optional<Currency> findById(long id);

    public Optional<Currency> findByCode(String code);

    public Optional<Currency> save(Currency currency);
}