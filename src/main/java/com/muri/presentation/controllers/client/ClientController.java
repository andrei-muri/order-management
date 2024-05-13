package com.muri.presentation.controllers.client;

import com.muri.business.ClientBL;
import com.muri.model.Client;
import com.muri.presentation.controllers.abstractcontroller.AbstractController;
import com.muri.presentation.views.client.ClientAddView;
import com.muri.presentation.views.client.ClientEditView;
import com.muri.presentation.views.client.ClientView;

import java.util.Map;

public class ClientController extends AbstractController<Client> {

    public ClientController() {
        super(new ClientView(), new ClientAddView());
        setActions();
    }

    private void setActions() {
        //insert
        addView.getAddButton().addActionListener((e) -> {
            Client client = getInstanceFromTextFields();
            ClientBL.insertClient(client);
            addView.setVisible(false);
            populateTableWithData(ClientBL.findAll());
            view.setVisible(true);
        });
    }
}
