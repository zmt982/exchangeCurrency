package org.currencyexchange.database.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class ExchangeRate {
    private long id;
    private long baseCurrencyId;
    private long targetCurrencyId;
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public ExchangeRate(long id, long baseCurrencyId, long targetCurrencyId, BigDecimal rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(long baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public long getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(long targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate exchangeRate = (ExchangeRate) o;
        return Objects.equals(id, exchangeRate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "org.currencyexchange.database.entity.ExchangeRate{" +
                "id=" + id +
                ", baseCurrencyId=" + baseCurrencyId +
                ", targetCurrencyId=" + targetCurrencyId +
                ", rate=" + rate +
                '}';
    }
}