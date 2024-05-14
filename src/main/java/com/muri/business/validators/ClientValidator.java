package com.muri.business.validators;

import com.muri.model.Client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientValidator implements Validator<Client> {

    String regex = "^[A-Z][a-z]*\\s[A-Z][a-z]*$";
    @Override
    public boolean validate(Client client) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(client.getName());
        return matcher.find();
    }
}
