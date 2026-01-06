package com.mercer.mettl.user.org.service.exception;

import com.mercer.mettl.user.org.service.constants.ErrorMessage;

public class ResourceConflictException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public ResourceConflictException(final ErrorMessage errorMessage, final Object... params) {
        super(errorMessage.format(params));
        this.errorMessage = errorMessage;
    }
}