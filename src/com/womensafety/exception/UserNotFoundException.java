package com.womensafety.exception;

/**
 * UserNotFoundException — thrown when a user lookup fails.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) { super(message); }
    public UserNotFoundException(String message, Throwable cause) { super(message, cause); }
}
