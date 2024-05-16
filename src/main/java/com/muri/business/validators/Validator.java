package com.muri.business.validators;

/**
 * <p>
 * An interface meant for implementing by the three type of validators: {@link ClientValidator}, {@link OrderValidator},
 * and {@link ProductValidator}. It can also be used as a functional interface, implementing it with lamda function.
 * </p>
 * @author Muresan Andrei-Ioan UTCN Computer Science 30425_2 2024 <br>
 * @param <T> Can be any class, but in our case we will use on our models: {@link com.muri.model.Client},
 *            {@link com.muri.model.Product}, and {@link com.muri.model.Order}.
 */
@FunctionalInterface
public interface Validator<T> {

    /**
     * Takes in an object, checks its appropriate fields
     * @param t object of type T
     * @return {@code true} if the object complies to the specific rules, {@code false} otherwise
     */
    boolean validate(T t);
}
