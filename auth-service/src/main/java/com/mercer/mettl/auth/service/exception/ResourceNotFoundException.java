package com.mercer.mettl.auth.service.exception;

import com.mercer.mettl.auth.service.constants.ErrorMessage;

public class ResourceNotFoundException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public ResourceNotFoundException(final ErrorMessage errorMessage, final Object... params) {
        super(errorMessage.format(params));
        this.errorMessage = errorMessage;
    }
}