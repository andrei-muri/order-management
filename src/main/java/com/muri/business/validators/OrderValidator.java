package com.muri.business.validators;

import com.muri.dao.ClientDAO;
import com.muri.dao.ProductDAO;
import com.muri.model.Order;

/**
 * <p>
 * Implements {@link com.muri.business.validators.Validator} interface, which has the abstract method
 * {@link com.muri.business.validators.Validator#validate(Object)}. Specific for the model {@link com.muri.model.Order}.
 * </p>
 * Usage:
 * <pre>
 * {@code
 *      Order order = new Order(client_id, product_id, quantity);
 *      Validator<Order> validator = new OrderValidator();
 *      if(validator.validate(order)) System.out.println("Ok");
 * }
 * </pre>
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024.
 */
public class OrderValidator implements Validator<Order> {
    private final ClientDAO clientDao = new ClientDAO();
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * This implemented version of {@link com.muri.business.validators.Validator#validate(Object)} checks the following things:
     *  <ul>
     *      <li>If the client with the {@code client_id} exists</li>
     *      <li>If the product with the {@code product_id} exists</li>
     *      <li>If there are enough products in the stock</li>
     *  </ul>
     * @param order object of type {@link com.muri.model.Order}
     * @return {@code true} if the all the conditions above are true, {@code false} otherwise.
     */
    @Override
    public boolean validate(Order order) {
        if (clientDao.findById((int) order.getClient_id()) == null || productDAO.findById((int) order.getProduct_id()) == null) return false;
        int productStock = productDAO.getStockById((int)order.getProduct_id());
        if(productStock == -1) return false;
        if(order.getQuantity() > productStock) return false;
        return true;
    }
}
