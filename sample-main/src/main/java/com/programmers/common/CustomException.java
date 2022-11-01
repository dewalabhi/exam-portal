package com.programmers.common;

/**
 * Unilog custom exception class.
 */
public class CustomException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor with error message.
     *
     * @param message error message
     */
    public CustomException(String message) {
        super(message);
    }

    /**
     * Constructor with cause.
     *
     * @param cause error cause
     */
    public CustomException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor with error message and cause.
     *
     * @param message error message
     * @param cause   error cause
     */
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
