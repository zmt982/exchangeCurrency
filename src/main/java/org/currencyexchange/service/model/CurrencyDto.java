package org.currencyexchange.service.model;

import java.util.Objects;

public class CurrencyDto {
    private long id;
    private String code;
    private String fullName;
    private String sign;

    public CurrencyDto() {
    }

    public CurrencyDto(long id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyDto currencyDto = (CurrencyDto) o;
        return Objects.equals(id, currencyDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, fullName, sign);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}