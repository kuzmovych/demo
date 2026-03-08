package com.example.app.shared.kernel.application;

/**
 * Application layer exceptions describe broken business rules without depending on HTTP/DB APIs.
 * Infrastructure adapters translate them into transport specific errors.
 */
public class BusinessRuleViolation extends RuntimeException {
    public BusinessRuleViolation(String message) {
        super(message);
    }
}
