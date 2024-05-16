package com.muri.business;

import com.muri.business.validators.ClientValidator;
import com.muri.business.validators.Validator;
import com.muri.dao.ClientDAO;
import com.muri.model.Client;

import java.util.List;

/**
 * <p>
 * Utility class that represent the business logic of a {@link com.muri.model.Client}. It interacts with the database access
 * objects and validates incoming clients.
 * </p>
 * <p>A client models a real-life client of a warehouse.</p>
 * <p>The class handles the validating, manipulating, retrieving, inserting, updating, and deleting of clients by interacting with
 * the {@link com.muri.dao.ClientDAO} dao.</p>
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024
 */
public class ClientBL {
    static ClientDAO dao = new ClientDAO();
    static Validator<Client> validator = new ClientValidator();

    /**
     * <p>
     *     Retrieves all the existing clients from the dao and passes them to a view controller.
     * </p>
     * @return {@code List} of clients
     */
    public static List<Client> findAll() {
        return dao.findAll();
    }

    /**
     * <p>
     *     Validates and sends to the dao a client to be inserted into the database
     * </p>
     * @param client of type {@link com.muri.model.Client}
     * @return the inserted client if everything is ok
     * @throws IllegalArgumentException if the client's details are not appropriate (name and email)
     */
    public static Client insertClient(Client client) {
        if(!validator.validate(client)) {
            throw new IllegalArgumentException("Wrong name or email format");
        }
        return dao.insert(client);
    }

    /**
     * <p>
     *     Validates and sends to the dao new details of a client to be updated.
     * </p>
     * @param client of type {@link com.muri.model.Client}
     * @throws IllegalArgumentException if the client's details are not appropriate (name and email)
     */
    public static void updateClient(Client client) {
        if(!validator.validate(client)) throw new IllegalArgumentException("Wrong name or email format");
        dao.update(client);
    }

    /**
     * <p>
     *     Deletes a client and its associated orders.
     * </p>
     * @param client of type {@link com.muri.model.Client}
     */
    public static void deleteClient(Client client) {
        OrderBL.deleteByProductOrClientId((int)client.getId(), false);
        if(dao.delete(client) == 0) {
            System.out.println("No deletion");
        }
    }
}
