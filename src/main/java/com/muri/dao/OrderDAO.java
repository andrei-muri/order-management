package com.muri.dao;

import com.muri.connection.ConnectionFactory;
import com.muri.model.Order;

import java.sql.*;
import java.util.logging.Level;

public class OrderDAO extends AbstractDAO<Order> {

    private String prepareDeleteByProductQuery() {
        return "DELETE FROM order_management.order WHERE product_id = ?";
    }
    private String prepareDeleteByClientQuery() {
        return "DELETE FROM order_management.order WHERE client_id = ?";
    }
    public int deleteByProductOrClientId(int id, boolean flag) { //true product, false client
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(flag ? prepareDeleteByProductQuery() : prepareDeleteByClientQuery());
            statement.setInt(1, id);

            return statement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error in deleting order by id " + ex.getMessage());
        }
        return 0;
    }
}
