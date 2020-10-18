package com.myretail.product.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * This class represents the price of a product.
 *
 * @author Shrinivaasan Venkataramani
 */
public class ProductPrice {
    private Double value;
    private String currencyCode;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
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
