package com.myretail.product.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Objects;

/**
 * This domain represents the final response of product detail.
 *
 * @author Shrinivaasan Venkataramani
 */
public class ProductDetailResponse {

    private int id;
    private String name;
    private ProductPrice productPrice;
    @JsonIgnore
    private boolean created;

    @JsonGetter("id")
    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {

        this.name = name;
    }

    @JsonGetter("current_price")
    public ProductPrice getProductPrice() {

        return productPrice;
    }

    @JsonSetter("current_price")
    public void setProductPrice(ProductPrice productPrice) {

        this.productPrice = productPrice;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

}
