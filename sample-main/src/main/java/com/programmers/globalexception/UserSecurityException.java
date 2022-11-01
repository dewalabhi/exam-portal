package com.programmers.globalexception;

public class UserSecurityException extends RuntimeException {
    public UserSecurityException(String message) {
        super(message);
    }
}
