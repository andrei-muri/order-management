package com.muri.business;

import com.muri.business.validators.OrderValidator;
import com.muri.business.validators.Validator;
import com.muri.dao.OrderDAO;
import com.muri.dao.ProductDAO;
import com.muri.model.Order;
import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.util.List;

public class OrderBL {
    static OrderDAO dao = new OrderDAO();
    static ProductDAO productDAO = new ProductDAO();
    static OrderValidator validator = new OrderValidator();

    public static List<Order> findAll() {
        return dao.findAll();
    }
    public static void insertOrder(Order order) {
        if(!validator.validate(order)) throw new IllegalArgumentException("Invalid order");
        dao.insert(order);
        productDAO.decrementStockById(order.getQuantity(), (int)order.getProduct_id());
    }

    public static void deleteOrder(Order order) {
        if(dao.delete(order) == 0) {
            System.out.println("No order deletion");
        }
    }

    public static void deleteByProductOrClientId(int id, boolean flag) {
        if(dao.deleteByProductOrClientId(id, flag) == 0) {
            System.out.println("No product or client from order deletion");
        }
    }
}
