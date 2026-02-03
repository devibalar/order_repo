package com.order.management.exception;

public class InvalidOrderStatusTransitionException extends RuntimeException {
    public InvalidOrderStatusTransitionException(String message) {
        super(message);
    }

    public InvalidOrderStatusTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
