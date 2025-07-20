package com.graduation.realestateconsulting.exceptions.newExceptions;


import org.springframework.security.core.AuthenticationException;

public class AccountBlockedException extends AuthenticationException {
    public AccountBlockedException(String message) {
        super(message);
    }
}
