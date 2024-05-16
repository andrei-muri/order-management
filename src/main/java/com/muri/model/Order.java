package com.muri.model;

/**
 * Models a warehouse order
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024
 */
public class Order {
    long id;
    long client_id;
    long product_id;
    int quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getClient_id() {
        return client_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }
}
