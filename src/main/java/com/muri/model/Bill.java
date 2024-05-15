package com.muri.model;

public record Bill(String id, String client_name, String product_name, int unit_price, int quantity, int total_price) {
}
