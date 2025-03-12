package org.currencyexchange.service.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ExchangeRateDto {
    private long id;
    private long baseCurrencyId;
    private long targetCurrencyId;
    private BigDecimal rate;

    public ExchangeRateDto() {
    }

    public ExchangeRateDto(long id, long baseCurrencyId, long targetCurrencyId, BigDecimal rate) {
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
        ExchangeRateDto rateDto = (ExchangeRateDto) o;
        return Objects.equals(baseCurrencyId, rateDto.baseCurrencyId)
                && Objects.equals(targetCurrencyId, rateDto.targetCurrencyId)
                && Objects.equals(rate, rateDto.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrencyId, targetCurrencyId, rate);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", baseCurrencyId=" + baseCurrencyId +
                ", targetCurrencyId=" + targetCurrencyId +
                ", rate=" + rate +
                '}';
    }
}
