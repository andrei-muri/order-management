package com.muri.presentation.controllers.product;

import com.muri.business.OrderBL;
import com.muri.business.ProductBL;
import com.muri.model.Product;
import com.muri.presentation.controllers.abstractcontroller.AbstractController;
import com.muri.presentation.views.product.ProductAddView;
import com.muri.presentation.views.product.ProductEditView;
import com.muri.presentation.views.product.ProductView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductController extends AbstractController<Product> {
    public ProductController() {
        super(new ProductView(), new ProductAddView(), new ProductEditView());
        setActions();
    }

    private void setActions() {
        //insert
        addView.getAddButton().addActionListener(e -> {
            Product product = getInstanceFromTextFields(addView);
            try {
                ProductBL.insertProduct(product);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Wrong product data", JOptionPane.ERROR_MESSAGE);
            }
            addView.setVisible(false);
            populateTableWithData(ProductBL.findAll());
            view.setVisible(true);
        });
        //update
        editView.getEditButton().addActionListener(e -> {
            long id = (long) view.getTable().getModel().getValueAt(view.getTable().getSelectedRow(), 0);
            Product product = getInstanceFromTextFields(editView);
            product.setId(id);
            try {
                ProductBL.updateProduct(product);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Wrong product data", JOptionPane.ERROR_MESSAGE);
            }
            editView.setVisible(false);
            populateTableWithData(ProductBL.findAll());
            view.setVisible(true);
        });
        //delete
        view.getDeleteButton().addActionListener(e -> {
            int selectedRow = view.getTable().getSelectedRow();
            if(selectedRow >= 0) {
                Product instance = (Product) getInstanceFromRow(selectedRow);

                ProductBL.deleteProduct(instance);
                populateTableWithData(ProductBL.findAll());
            } else {
                JOptionPane.showMessageDialog(view, "Please select a row to delete.");
            }
        });
    }
}
