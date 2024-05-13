package com.muri.presentation.controllers;

import com.muri.model.Client;
import com.muri.model.Product;
import com.muri.presentation.controllers.client.ClientController;
import com.muri.presentation.controllers.order.OrderController;
import com.muri.presentation.controllers.product.ProductController;
import com.muri.presentation.views.MenuView;

import java.util.List;

public class MenuController {
    MenuView view;

    public MenuController() {
        this.view = new MenuView();
        view.setVisible(true);
        setActionListeners();
    }

    private void setActionListeners() {
        view.getClientButton().addActionListener((e) ->
        {
            view.setVisible(false);
            new ClientController().populateTableWithData(List.of(new Client(5, "andrei", "asds", "Sibiu")));
        });
        view.getProductButton().addActionListener((e) ->
        {
            view.setVisible(false);
            new ProductController().populateTableWithData(List.of(new Product(1, "Shoes", 2, 12)));
        });
        view.getOrderButton().addActionListener((e) -> {
            view.setVisible(false);
            new OrderController();
        });
    }
}
