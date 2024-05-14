package com.muri.business.validators;

public interface Validator<T> {
    boolean validate(T t);
}
