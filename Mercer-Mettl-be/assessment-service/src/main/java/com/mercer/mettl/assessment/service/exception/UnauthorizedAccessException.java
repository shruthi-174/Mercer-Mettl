package com.mercer.mettl.assessment.service.exception;

import com.mercer.mettl.assessment.service.constants.ErrorMessage;

public class UnauthorizedAccessException extends RuntimeException{

    private final ErrorMessage errorMessage;

    public UnauthorizedAccessException(final ErrorMessage errorMessage, final Object... params) {
        super(errorMessage.format(params));
        this.errorMessage = errorMessage;
    }}
