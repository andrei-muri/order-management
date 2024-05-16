package com.muri.business;

import com.muri.business.validators.OrderValidator;
import com.muri.business.validators.Validator;
import com.muri.dao.BillDAO;
import com.muri.dao.OrderDAO;
import com.muri.dao.ProductDAO;
import com.muri.model.Order;
import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.util.List;

/**
 * <p>
 * Utility class that represent the business logic of a {@link com.muri.model.Order}. It interacts with the database access
 * objects and validates incoming orders.
 * </p>
 * <p>A order models a real-life order of a warehouse.</p>
 * <p>The class handles the validating, manipulating, retrieving, and inserting of orders by interacting with
 * the {@link com.muri.dao.OrderDAO} dao. The class also interacts with the {@link com.muri.dao.ProductDAO} for
 * stock decrementing.</p>
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024
 */
public class OrderBL {
    static OrderDAO dao = new OrderDAO();
    static ProductDAO productDAO = new ProductDAO();
    static OrderValidator validator = new OrderValidator();

    /**
     * <p>
     *     Retrieves all the orders from the database.
     * </p>
     * @return {@code List} of orders
     */
    public static List<Order> findAll() {
        return dao.findAll();
    }

    /**
     * Validates and inserts an order. Also decrements the stock of the product.
     * @param order of type {@link com.muri.model.Order}
     * @throws IllegalArgumentException if the order is invalid
     */
    public static void insertOrder(Order order) {
        if(!validator.validate(order)) throw new IllegalArgumentException("Invalid order");
        dao.insert(order);
        BillBL.createBill(order);

        productDAO.decrementStockById(order.getQuantity(), (int)order.getProduct_id());
    }

    /**
     * <p>
     *     Deletes an order
     * </p>
     * @param order of type {@link com.muri.model.Order}
     */
    public static void deleteOrder(Order order) {
        if(dao.delete(order) == 0) {
            System.out.println("No order deletion");
        }
    }

    /**
     * Method meant to be used by client and product deletion, so the appropriate order is deleted too
     * @param id of a client or product
     * @param flag {@code true} for client, {@code false} for product
     */
    public static void deleteByProductOrClientId(int id, boolean flag) {
        if(dao.deleteByProductOrClientId(id, flag) == 0) {
            System.out.println("No product or client from order deletion");
        }
    }
}
