package com.muri.model;

/**
 * Models a warehouse product
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024
 */
public class Product {
    long id;
    String name;
    int price;
    int stock;

    public Product() {
    }

    public Product(long id, String name, int price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
