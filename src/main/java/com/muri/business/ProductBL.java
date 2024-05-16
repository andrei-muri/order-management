package com.muri.business;

import com.muri.business.validators.ProductValidator;
import com.muri.business.validators.Validator;
import com.muri.dao.ProductDAO;
import com.muri.model.Client;
import com.muri.model.Product;

import java.util.List;

/**
 * <p>
 * Utility class that represent the business logic of a {@link com.muri.model.Product}. It interacts with the database access
 * objects and validates incoming products.
 * </p>
 * <p>A product models a real-life product of a warehouse.</p>
 * <p>The class handles the validating, manipulating, retrieving, and inserting of orders by interacting with
 * the {@link com.muri.dao.ProductDAO} dao.
 * </p>
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024
 */
public class ProductBL {
    static ProductDAO dao = new ProductDAO();
    static Validator<Product> validator = new ProductValidator();

    /**
     * Retrieves all the products.
     * @return {@code List} of products
     */
    public static List<Product> findAll() {
        return dao.findAll();
    }

    /**
     * Validates and sends the product to dao to be inserted.
     * @param product of type {@link com.muri.model.Product}
     * @return the inserted product if everything ok
     * @throws IllegalArgumentException if the product details are not ok
     */
    public static Product insertProduct(Product product) {
        if(!validator.validate(product)) throw new IllegalArgumentException("Wrong price and/or stock");
        return dao.insert(product);
    }

    /**
     * Validates and sends the existing product to dao to be updated.
     * @param product of type {@link com.muri.model.Product}
     * @throws IllegalArgumentException if the product details are not ok
     */
    public static void updateProduct(Product product) {
        if(!validator.validate(product)) throw new IllegalArgumentException("Wrong price and/or stock");
        dao.update(product);
    }

    /**
     * Deletes a product and also deletes the appropriate orders.
     * @param product of type {@link com.muri.model.Product}
     */
    public static void deleteProduct(Product product) {
        OrderBL.deleteByProductOrClientId((int)product.getId(), true);
        if(dao.delete(product) == 0) {
            System.out.println("No deletion");
        }
    }
}
