package com.mercer.mettl.auth.user.exception;

import com.mercer.mettl.auth.user.constants.ErrorMessage;

public class UnauthorizedAccessException extends RuntimeException{

    private final ErrorMessage errorMessage;

    public UnauthorizedAccessException(final ErrorMessage errorMessage, final Object... params) {
        super(errorMessage.format(params));
        this.errorMessage = errorMessage;
    }}
