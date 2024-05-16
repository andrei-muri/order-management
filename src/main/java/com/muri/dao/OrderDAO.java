package com.muri.dao;

import com.muri.connection.ConnectionFactory;
import com.muri.model.Order;

import java.sql.*;
import java.util.logging.Level;

/**
 * Data access class meant for operations done on the order table.
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024
 */
public class OrderDAO extends AbstractDAO<Order> {

    private String prepareDeleteByProductQuery() {
        return "DELETE FROM order_management.order WHERE product_id = ?";
    }
    private String prepareDeleteByClientQuery() {
        return "DELETE FROM order_management.order WHERE client_id = ?";
    }

    /**
     * Deletes order by product or client id.
     * @param id of the product/client
     * @param flag {@code true} for client, {@code false} for product
     * @return
     */
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
        } finally {
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
        }
        return 0;
    }
}
