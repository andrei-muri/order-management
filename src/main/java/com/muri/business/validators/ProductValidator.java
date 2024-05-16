package com.muri.business.validators;

import com.muri.model.Product;

/**
 * <p>
 * Implements {@link com.muri.business.validators.Validator} interface, which has the abstract method
 * {@link com.muri.business.validators.Validator#validate(Object)}. Specific for the model {@link com.muri.model.Product}.
 * </p>
 * Usage:
 * <pre>
 * {@code
 *      Product product = new Product("name", price, stock);
 *      Validator<Product> validator = new ProductValidator();
 *      if(validator.validate(product)) System.out.println("Ok");
 * }
 * </pre>
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024.
 */
public class ProductValidator implements Validator<Product> {
    /**
     * This implemented version of {@link com.muri.business.validators.Validator#validate(Object)} checks if the price and stock
     * are greater than 0.
     * @param product object of type {@link com.muri.model.Product}
     * @return {@code true} if the all the conditions above are true, {@code false} otherwise.
     */
    @Override
    public boolean validate(Product product) {
        return product.getStock() >= 0 && product.getPrice() >= 0;
    }
}
