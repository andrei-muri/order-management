package com.muri.dao;

import com.muri.connection.ConnectionFactory;
import com.muri.model.Product;

import java.sql.*;
import java.util.logging.Level;

public class ProductDAO extends AbstractDAO<Product> {

    private String prepareGetStockByIdQuery() {
        return "SELECT stock FROM product WHERE id = ?";
    }
    private String prepareDecrementStockByIdQuery() {
        return "UPDATE product SET stock = stock - ? WHERE id = ?";
    }

    public int getStockById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(prepareGetStockByIdQuery());
            statement.setObject(1, id);
            resultSet = statement.executeQuery();

            if(!resultSet.next()) {
                return -1;
            }
            return Integer.parseInt(resultSet.getObject("stock").toString());
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error in ProductDAO::getStockById " + e.getMessage());
        } finally {
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(resultSet);
        }
        return -1;
    }
    public int decrementStockById(int stock, int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(prepareDecrementStockByIdQuery());
            statement.setInt(1, stock);
            statement.setObject(2, id);

            return statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error in ProductDAO::decrementStockById " + e.getMessage());
        } finally {
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
        }
        return 0;
    }
}
