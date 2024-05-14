package com.muri.business;

import com.muri.business.validators.ClientValidator;
import com.muri.business.validators.Validator;
import com.muri.dao.ClientDAO;
import com.muri.model.Client;

import java.util.List;

public class ClientBL {
    static ClientDAO dao = new ClientDAO();
    static Validator<Client> validator = new ClientValidator();
    public static List<Client> findAll() {
        return dao.findAll();
    }

    public static Client insertClient(Client client) {
        if(!validator.validate(client)) {
            throw new IllegalArgumentException("Wrong name format");
        }
        return dao.insert(client);
    }

    public static void updateClient(Client client) {
        if(!validator.validate(client)) throw new IllegalArgumentException("Wrong name format");
        dao.update(client);
    }
}
