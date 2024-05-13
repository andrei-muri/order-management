package com.muri.model;

public class Order {
    long client_id;
    long product_id;
    int quantity;

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
