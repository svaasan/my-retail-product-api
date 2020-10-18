package com.myretail.product.domain;

/**
 *  This class represents the Product data from External Product Service.
 *
 * @author Shrinivaasan Venkataramani
 */
public class Product {
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
