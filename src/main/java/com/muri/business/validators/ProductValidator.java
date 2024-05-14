package com.muri.business.validators;

import com.muri.model.Product;

public class ProductValidator implements Validator<Product> {
    @Override
    public boolean validate(Product product) {
        return product.getStock() >= 0 && product.getPrice() >= 0;
    }
}
