package com.mercer.mettl.auth.service.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    USER_ALREADY_EXISTS("User with the email %s already exists."),
    USER_NOT_FOUND("User with this email %s not found."),
    PASSWORD_INCORRECT("Incorrect password, please try again."),
    ACCOUNT_DISABLED("Access denied. This account is currently inactive."),
    REFRESH_TOKEN_INVALID("Invalid or expired refresh token."),
    ROLE_NOT_FOUND("Role with the name %s not found."),
    RESET_TOKEN_INVALID("Invalid or expired reset token");

    private final String errorMessage;

    public String format(final Object[] params) {
        return String.format(errorMessage, params);
    }
}
