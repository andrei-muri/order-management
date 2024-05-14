package com.muri.business;

import com.muri.business.validators.ProductValidator;
import com.muri.business.validators.Validator;
import com.muri.dao.ProductDAO;
import com.muri.model.Product;

import java.util.List;

public class ProductBL {
    static ProductDAO dao = new ProductDAO();
    static Validator<Product> validator = new ProductValidator();

    public static List<Product> findAll() {
        return dao.findAll();
    }

    public static Product insert(Product product) {
        if(!validator.validate(product)) throw new IllegalArgumentException("Wrong price and/or stock");
        return dao.insert(product);
    }

    public static void update(Product product) {
        if(!validator.validate(product)) throw new IllegalArgumentException("Wrong price and/or stock");
        dao.update(product);
    }
}
