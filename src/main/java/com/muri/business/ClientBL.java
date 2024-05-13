package com.muri.business;

import com.muri.dao.ClientDAO;
import com.muri.model.Client;

import java.util.List;

public class ClientBL {
    static ClientDAO dao = new ClientDAO();
    public static List<Client> findAll() {
        return dao.findAll();
    }

    public static Client insertClient(Client client) {
        return dao.insert(client);
    }
}
