package com.muri.presentation.controllers.client;

import com.muri.business.ClientBL;
import com.muri.model.Client;
import com.muri.presentation.controllers.abstractcontroller.AbstractController;
import com.muri.presentation.views.abstractview.AbstractEditView;
import com.muri.presentation.views.client.ClientAddView;
import com.muri.presentation.views.client.ClientEditView;
import com.muri.presentation.views.client.ClientView;

import javax.swing.*;
import java.util.Map;

public class ClientController extends AbstractController<Client> {

    public ClientController() {
        super(new ClientView(), new ClientAddView(), new ClientEditView());
        setActions();
    }

    private void setActions() {
        //insert
        addView.getAddButton().addActionListener((e) -> {
            Client client = getInstanceFromTextFields(addView);
            try {
                ClientBL.insertClient(client);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Wrong client details", JOptionPane.ERROR_MESSAGE);
            }
            addView.setVisible(false);
            populateTableWithData(ClientBL.findAll());
            view.setVisible(true);
        });

        editView.getEditButton().addActionListener(e -> {
            long id = (long) view.getTable().getModel().getValueAt(view.getTable().getSelectedRow(), 0);
            Client client = getInstanceFromTextFields(editView);
            client.setId(id);
            try {
                ClientBL.updateClient(client);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Wrong client details", JOptionPane.ERROR_MESSAGE);
            }
            editView.setVisible(false);
            populateTableWithData(ClientBL.findAll());
            view.setVisible(true);
        });
    }
}
