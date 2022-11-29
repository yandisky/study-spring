package com.example.springsecurity.security.exception;


import org.springframework.security.core.AuthenticationException;

public class LoginFailedException extends AuthenticationException {
    public LoginFailedException(String msg) {
        super(msg);
    }
}
