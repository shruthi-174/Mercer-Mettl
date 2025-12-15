package com.mercer.mettl.auth.user.exception;

import com.mercer.mettl.auth.user.constants.ErrorMessage;

public class ResourceNotFoundException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public ResourceNotFoundException(final ErrorMessage errorMessage, final Object... params) {
        super(errorMessage.format(params));
        this.errorMessage = errorMessage;
    }
}