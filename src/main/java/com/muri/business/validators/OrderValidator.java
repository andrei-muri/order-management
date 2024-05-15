package com.muri.business.validators;

import com.muri.dao.ClientDAO;
import com.muri.dao.ProductDAO;
import com.muri.model.Order;

public class OrderValidator implements Validator<Order> {
    private ClientDAO clientDao = new ClientDAO();
    private ProductDAO productDAO = new ProductDAO();

    @Override
    public boolean validate(Order order) {
        if (clientDao.findById((int) order.getClient_id()) != 1 || productDAO.findById((int) order.getProduct_id()) != 1) return false;
        int productStock = productDAO.getStockById((int)order.getProduct_id());
        if(productStock == -1) return false;
        if(order.getQuantity() > productStock) return false;
        return true;
    }
}
