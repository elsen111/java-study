package com.example.productapi.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class FieldValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public FieldValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = new LinkedHashMap<>(fieldErrors);
    }

    public Map<String, String> getFieldErrors() {
        return Collections.unmodifiableMap(fieldErrors);
    }
}
