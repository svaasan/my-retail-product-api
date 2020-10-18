package com.myretail.product.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.myretail.product.validation.ProductDetailConstraint;

/**
 * This class is used to as RequestBody for updating the Product Details.
 *
 * @author Shrinivaasan Venkataramani
 */
@ProductDetailConstraint
public class ProductDetailRequest {
    private int id;

    private ProductPrice productPrice;

    @JsonGetter("id")
    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonGetter("current_price")
    public ProductPrice getProductPrice() {

        return productPrice;
    }

    @JsonSetter("current_price")
    public void setProductPrice(ProductPrice productPrice) {

        this.productPrice = productPrice;
    }
}
