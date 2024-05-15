package com.muri.presentation.controllers;

import com.muri.business.ClientBL;
import com.muri.business.OrderBL;
import com.muri.business.ProductBL;
import com.muri.dao.ClientDAO;
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
            new ClientController().populateTableWithData(ClientBL.findAll());
        });
        view.getProductButton().addActionListener((e) ->
        {
            view.setVisible(false);
            new ProductController().populateTableWithData(ProductBL.findAll());
        });
        view.getOrderButton().addActionListener((e) -> {
            view.setVisible(false);
            new OrderController().populateTableWithData(OrderBL.findAll());
        });
    }
}
