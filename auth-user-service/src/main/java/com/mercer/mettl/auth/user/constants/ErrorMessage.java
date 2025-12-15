package com.mercer.mettl.auth.user.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    ORG_ALREADY_EXISTS("Organization with the domain %s already exists."),
    ORG_NOT_FOUND("Organization with this id %s not found");

    private final String errorMessage;

    public String format(final Object[] params) {
        return String.format(errorMessage, params);
    }
}
