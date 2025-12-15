package com.mercer.mettl.auth.user.exception;

import com.mercer.mettl.auth.user.constants.ErrorMessage;

public class InvalidRequestException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public InvalidRequestException(final ErrorMessage errorMessage, final Object... params) {
        super(errorMessage.format(params));
        this.errorMessage = errorMessage;
    }
}
