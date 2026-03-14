package com.project.muttley.valueObject;

import org.apache.commons.validator.routines.EmailValidator;

public class Email {

    private final String value;

    public Email(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid Email: " + value);
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValid(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}
