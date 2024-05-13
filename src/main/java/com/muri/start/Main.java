package com.muri.start;

import com.muri.connection.ConnectionFactory;
import com.muri.presentation.controllers.MenuController;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection c = ConnectionFactory.getConnection();
        ConnectionFactory.close(c);
        new MenuController();
    }
}