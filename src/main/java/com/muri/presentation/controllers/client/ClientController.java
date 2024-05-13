package com.muri.presentation.controllers.client;

import com.muri.model.Client;
import com.muri.presentation.controllers.abstractcontroller.AbstractController;
import com.muri.presentation.views.client.ClientAddView;
import com.muri.presentation.views.client.ClientEditView;
import com.muri.presentation.views.client.ClientView;

public class ClientController extends AbstractController<Client> {

    public ClientController() {
        super(new ClientView(), new ClientAddView());
    }
}
