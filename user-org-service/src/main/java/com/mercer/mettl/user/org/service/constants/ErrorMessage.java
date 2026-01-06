package com.mercer.mettl.user.org.service.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    USER_ALREADY_EXISTS("User with the email %s already exists."),
    USER_NOT_FOUND("User with this email %s not found."),
    ORG_ALREADY_EXISTS("Organization with the domain %s already exists."),
    ORG_NOT_FOUND("Organization with this id %s not found"),
    ROLE_NOT_FOUND("Invalid role. No user found with this role %s");

    private final String errorMessage;

    public String format(final Object[] params) {
        return String.format(errorMessage, params);
    }
}
