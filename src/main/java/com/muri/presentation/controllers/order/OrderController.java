package com.muri.presentation.controllers.order;

import com.muri.business.OrderBL;
import com.muri.business.ProductBL;
import com.muri.model.Order;
import com.muri.model.Product;
import com.muri.presentation.controllers.abstractcontroller.AbstractController;
import com.muri.presentation.views.order.OrderAddView;
import com.muri.presentation.views.order.OrderEditView;
import com.muri.presentation.views.order.OrderView;

import javax.swing.*;
import javax.swing.text.View;

public class OrderController extends AbstractController<Order> {
    public OrderController() {
        super(new OrderView(), new OrderAddView(), new OrderEditView());
        setActions();
    }

    private void setActions() {
        //insert
        addView.getAddButton().addActionListener((e) -> {
            Order order = getInstanceFromTextFields(addView);
            try {
                OrderBL.insertOrder(order);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Wrong order details", JOptionPane.ERROR_MESSAGE);
            }
            addView.setVisible(false);
            populateTableWithData(OrderBL.findAll());
            view.setVisible(true);
        });
        //delete
        view.getDeleteButton().addActionListener(e -> {
            int selectedRow = view.getTable().getSelectedRow();
            if(selectedRow >= 0) {
                Order instance = getInstanceFromRow(selectedRow);
                OrderBL.deleteOrder(instance);
                populateTableWithData(OrderBL.findAll());
            } else {
                JOptionPane.showMessageDialog(view, "Please select a row to delete.");
            }
        });
    }
}
