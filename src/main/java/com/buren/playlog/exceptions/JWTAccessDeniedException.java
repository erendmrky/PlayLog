package com.buren.playlog.exceptions;

public class JWTAccessDeniedException extends RuntimeException {
    public JWTAccessDeniedException(String message) {
        super(message);
    }
}
