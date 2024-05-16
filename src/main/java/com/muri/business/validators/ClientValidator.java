package com.muri.business.validators;

import com.muri.model.Client;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Implements {@link com.muri.business.validators.Validator} interface, which has the abstract method
 * {@link com.muri.business.validators.Validator#validate(Object)}. Specific for the model {@link com.muri.model.Client}.
 * </p>
 * Usage:
 * <pre>
 * {@code
 *      Client client = new Client("name", "email", "address");
 *      Validator<Client> validator = new ClientValidator();
 *      if(validator.validate(client)) System.out.println("Ok");
 * }
 * </pre>
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024.
 */
public class ClientValidator implements Validator<Client> {

    private final String regexName = "^[A-Z][a-z]*\\s[A-Z][a-z]*$";
    private final String regexEmail = "^[A-Z0-9a-z._%+-]+@[A-Z0-9a-z.-]+\\.[A-Za-z]{2,6}$";

    /**
     * This implemented version of {@link com.muri.business.validators.Validator#validate(Object)} checks the name and
     * email field to be of specific format.
     * @param client object of type {@link com.muri.model.Client}
     * @return {@code true} if the name and email comply, {@code false} otherwise.
     */
    @Override
    public boolean validate(Client client) {
        Pattern patternName = Pattern.compile(regexName);
        Pattern patternEmail = Pattern.compile(regexEmail);
        Matcher matcherName = patternName.matcher(client.getName());
        Matcher matcherEmail = patternEmail.matcher(client.getEmail());
        return matcherName.find() && matcherEmail.find();
    }
}
