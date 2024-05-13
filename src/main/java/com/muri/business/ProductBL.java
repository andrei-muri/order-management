package com.muri.business;

import com.muri.dao.ProductDAO;
import com.muri.model.Product;

import java.util.List;

public class ProductBL {
    static ProductDAO dao = new ProductDAO();

    public static List<Product> findAll() {
        return dao.findAll();
    }

    public static Product insert(Product product) {
        return dao.insert(product);
    }
}
