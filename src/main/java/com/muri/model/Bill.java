package com.muri.model;

/**
 * Models a receipt.
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024
 */
public record Bill(String id, String client_name, String product_name, int unit_price, int quantity, int total_price) {
}
