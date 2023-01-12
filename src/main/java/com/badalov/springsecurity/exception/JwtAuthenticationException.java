package com.badalov.springsecurity.exception;

import org.springframework.http.HttpStatus;

import javax.naming.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    private HttpStatus httpStatus;

    public JwtAuthenticationException(String explanation, HttpStatus status) {
        super(explanation);
        this.httpStatus = status;
    }

    public JwtAuthenticationException(String explanation) {
        super(explanation);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
