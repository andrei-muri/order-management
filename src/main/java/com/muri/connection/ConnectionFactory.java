package com.muri.connection;

import java.io.IOException;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * <p>
 *     Utility class for handling the connection with the database server. It uses the singleton principle.
 * </p>
 * <p>
 *     It can create retrieve a connection and close a connection, or a statement, or a result set
 * </p>
 * @author Muresan Andrei UTCN Computer Science 30425_2
 * @source UTCN Distributed Systems Research Laboratory, Marcel Antal
 */
public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/order_management";
    private static final String USER = "root";
    private static final String PASS = "mysql";

    private static ConnectionFactory instance = new ConnectionFactory();

    private ConnectionFactory() {
        //The logging will be in events.log from logs directory
        FileHandler fh;

        try {
            fh = new FileHandler("logs/events.log");
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

    /**
     * Retrieves the connection to the database
     * @return {@link Connection} to database
     */
    public static Connection getConnection() {
        return instance.createConnection();
    }

    /**
     * Closes the connection given as parameter
     * @param connection to be closed
     */
    public static void close(Connection connection) {
        if(connection != null) {
            try {
                connection.close();
                LOGGER.info("Connection successfully closed");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the connection");
            }
        } else {
            LOGGER.log(Level.WARNING, "No connection to close");
        }
    }

    /**
     * Closes the statement give as parameter
     * @param statement to be closed
     */
    public static void close(Statement statement) {
        if(statement != null) {
            try {
                statement.close();
                LOGGER.info("Statement " + statement.toString() + " successfully closed");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the statement");
            }
        } else {
            LOGGER.log(Level.WARNING, "No statement to close");
        }
    }

    /**
     * Closes the result set given as parameters
     * @param resultSet to be closed
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
                LOGGER.info("Result set " + resultSet.toString() + " successfully closed");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the ResultSet");
            }
        } else {
            LOGGER.log(Level.WARNING, "No result set to close");
        }
    }

}
