package com.muri.business;

import com.muri.dao.BillDAO;
import com.muri.dao.ClientDAO;
import com.muri.dao.ProductDAO;
import com.muri.model.Bill;
import com.muri.model.Client;
import com.muri.model.Order;
import com.muri.model.Product;

import java.util.List;
import java.util.UUID;

public class BillBL {
    static BillDAO dao = new BillDAO();
    static ClientDAO clientDao = new ClientDAO();
    static ProductDAO productDAO = new ProductDAO();

    public static List<Bill> findAll() {return dao.findAll();}

    public static void createBill(Order order) {
        Client client = clientDao.findById((int) order.getClient_id());
        Product product = productDAO.findById((int) order.getProduct_id());
        if(product == null || client == null) throw new IllegalArgumentException("Cannot found product or client with specific id");

        Bill bill = new Bill(
                UUID.randomUUID().toString(),
                client.getName(),
                product.getName(),
                product.getPrice(),
                order.getQuantity(),
                product.getPrice() * order.getQuantity());
        dao.insert(bill);
    }
}
