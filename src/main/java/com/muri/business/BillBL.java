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

/**
 * <p>
 * Utility class that represent the business logic of a {@link com.muri.model.Bill}. It interacts with the database access
 * objects.
 * </p>
 * <p>A bill models a real-life receipt.</p>
 * <p>The class handles the retrieving of all the bills from the database and inserting into the database by
 * interacting with the {@link com.muri.dao.BillDAO} dao.</p>
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024
 */
public class BillBL {
    static BillDAO dao = new BillDAO();
    static ClientDAO clientDao = new ClientDAO();
    static ProductDAO productDAO = new ProductDAO();

    /**
     * Utility method to retrieve all the bills from the dao.
     * @return {@code List} of bills.
     */
    public static List<Bill> findAll() {return dao.findAll();}

    /**
     * <p>
     *     Utility method create and insert bill. It also interacts with {@link ClientDAO} and {@link ProductDAO} for retrieving
     *     the names of the client and product.
     * </p>
     * @param order of type {@link com.muri.model.Order} representing the order on which the bill is based.
     * @throws IllegalArgumentException if either a client or a product with specific {@code id} retrieved from the order is not found.
     */
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
