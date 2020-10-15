package com.myretail.product.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ProductPrice {
    private double value;
    private String currencyCode;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @JsonGetter("currency_code")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonSetter("currency_code")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
