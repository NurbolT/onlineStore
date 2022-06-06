package com.example.onlinestore.exception;

import org.springframework.security.core.AuthenticationException;

public class UserOrEmailExistsException extends AuthenticationException {

    public UserOrEmailExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserOrEmailExistsException(String msg) {
        super(msg);
    }
}
