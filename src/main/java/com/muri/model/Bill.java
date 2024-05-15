package com.muri.model;

public record Bill(int id, String clientName, String productName, int unitPrice, int quantity, int totalPrice) {
}
