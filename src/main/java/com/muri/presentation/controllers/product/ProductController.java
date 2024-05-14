package com.muri.presentation.controllers.product;

import com.muri.business.ProductBL;
import com.muri.model.Product;
import com.muri.presentation.controllers.abstractcontroller.AbstractController;
import com.muri.presentation.views.product.ProductAddView;
import com.muri.presentation.views.product.ProductEditView;
import com.muri.presentation.views.product.ProductView;

import javax.swing.*;

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
                ProductBL.insert(product);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Wrong product data", JOptionPane.ERROR_MESSAGE);
            }
            addView.setVisible(false);
            populateTableWithData(ProductBL.findAll());
            view.setVisible(true);
        });

        editView.getEditButton().addActionListener(e -> {
            long id = (long) view.getTable().getModel().getValueAt(view.getTable().getSelectedRow(), 0);
            Product product = getInstanceFromTextFields(editView);
            product.setId(id);
            try {
                ProductBL.update(product);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Wrong product data", JOptionPane.ERROR_MESSAGE);
            }
            editView.setVisible(false);
            populateTableWithData(ProductBL.findAll());
            view.setVisible(true);
        });
    }
}
