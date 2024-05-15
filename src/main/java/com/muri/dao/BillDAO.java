package com.muri.dao;

import com.muri.connection.ConnectionFactory;
import com.muri.model.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BillDAO extends AbstractDAO<Bill> {

    @Override
    public Bill insert(Bill bill) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "INSERT INTO bill (id, client_name, product_name, unit_price, quantity, total_price) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, bill.id());
            statement.setString(2, bill.client_name());
            statement.setString(3, bill.product_name());
            statement.setInt(4, bill.unit_price());
            statement.setInt(5, bill.quantity());
            statement.setInt(6, bill.total_price());
            int rows = statement.executeUpdate();

            if(rows == 0) {
                LOGGER.info("DAO::insert bill wrong");
                return null;
            } else {
                return bill;
            }

        } catch(SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    @Override
    public List<Bill> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM bill";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            List<Bill> objects = createBills(resultSet);
            if(objects.isEmpty()) LOGGER.info("DAO::findAll The SELECT * query returned no element");
            return objects;
        } catch(SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<Bill> createBills(ResultSet resultSet) {
        List<Bill> list = new ArrayList<>();
        try {
            while(resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getString("id"),
                        resultSet.getString("client_name"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("unit_price"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("total_price"));
                list.add(bill);
            }
        } catch(SQLException e) {
            LOGGER.log(Level.SEVERE, "Cannot create list of bills : " + e);
        }
        return list;
    }
}
