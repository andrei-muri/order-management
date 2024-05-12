package com.muri.connection;

import java.io.IOException;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/order_management";
    private static final String USER = "root";
    private static final String PASS = "mysql";

    private static ConnectionFactory instance = new ConnectionFactory();

    private ConnectionFactory() {
        //The logging will be in connection_log.txt from logs directory
        FileHandler fh;

        try {
            fh = new FileHandler("logs/connection.log");
            LOGGER.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());

            LOGGER.info("Logger initialized. Logger name: " + LOGGER.getName());
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "Error occur in FileHandler in class " + ConnectionFactory.class.getName());
        }

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Could not open jdbc driver :: " + e.getMessage());
        }
    }

    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch(SQLException e) {
            LOGGER.log(Level.WARNING, "An error occurred while trying to connect to the database");
        }
        return connection;
    }

    public static Connection getConnection() {
        return instance.createConnection();
    }

    public static void close(Connection connection) {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the connection");
            }
        }
    }

    public static void close(Statement statement) {
        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the statement");
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the ResultSet");
            }
        }
    }

}
