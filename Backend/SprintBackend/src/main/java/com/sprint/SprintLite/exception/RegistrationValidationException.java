package com.sprint.SprintLite.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class RegistrationValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public RegistrationValidationException(Map<String, String> errors) {
        super("Registration validation failed");
        this.errors = errors;
    }

}
