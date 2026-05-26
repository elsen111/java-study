package com.example.productapi.services.utils;

import com.example.productapi.exception.FieldValidationException;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ValidationUtil {

    private ValidationUtil() {
    }

    public static void requireText(Map<String, String> errors, String fieldName, String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            errors.put(fieldName, label + " is required");
        }
    }

    public static void requireValue(Map<String, String> errors, String fieldName, Object value, String label) {
        if (value == null) {
            errors.put(fieldName, label + " is required");
        }
    }

    public static void requireNotEmpty(Map<String, String> errors, String fieldName, List<?> value, String label) {
        if (value == null || value.isEmpty()) {
            errors.put(fieldName, "At least one " + label + " is required");
        }
    }

    public static void rejectBlankWhenPresent(Map<String, String> errors, String fieldName, String value, String label) {
        if (value != null && value.trim().isEmpty()) {
            errors.put(fieldName, label + " cannot be empty");
        }
    }

    public static void rejectNegativeWhenPresent(Map<String, String> errors, String fieldName, BigDecimal value, String label) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            errors.put(fieldName, label + " cannot be negative");
        }
    }

    public static void rejectNegativeWhenPresent(Map<String, String> errors, String fieldName, Integer value, String label) {
        if (value != null && value < 0) {
            errors.put(fieldName, label + " cannot be negative");
        }
    }

    public static Map<String, String> newErrorMap() {
        return new LinkedHashMap<>();
    }

    public static void throwIfNotEmpty(Map<String, String> errors) {
        if (!errors.isEmpty()) {
            throw new FieldValidationException("Please check the highlighted fields", errors);
        }
    }
}
