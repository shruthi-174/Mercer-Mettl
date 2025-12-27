package com.mercer.mettl.user.org.service.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    USER_ALREADY_EXISTS("User with the email %s already exists."),
    USER_NOT_FOUND("User with this email %s not found."),
    ADMIN_ACCESS("Only SYSTEM ADMIN can create organizations"),
    ORG_ADMIN_ACCESS("Only ORG ADMIN can perform this action"),
    ORG_ALREADY_EXISTS("Organization with the domain %s already exists."),
    ORG_NOT_FOUND("Organization with this id %s not found");

    private final String errorMessage;

    public String format(final Object[] params) {
        return String.format(errorMessage, params);
    }
}
