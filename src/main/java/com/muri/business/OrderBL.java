package com.muri.business;

import com.muri.dao.OrderDAO;
import com.muri.model.Order;

import java.util.List;

public class OrderBL {
    static OrderDAO dao = new OrderDAO();

    public static List<Order> findAll() {
        return dao.findAll();
    }
}
