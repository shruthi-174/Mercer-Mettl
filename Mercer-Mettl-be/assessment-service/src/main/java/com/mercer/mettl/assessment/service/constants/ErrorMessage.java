package com.mercer.mettl.assessment.service.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    OPTION_NOT_FOUND("Option ID not found: %s"),
    QUESTION_NOT_FOUND("Question ID not found: %s"),
    INVALID_OPTION("Option does not belong to question"),
    INVALID_OPTION_COUNT("Invalid option count"),
    INVALID_CORRECT_OPTION_COUNT("Invalid correct option count"),
    TEST_NOT_FOUND("Test ID not found: %s"),
    JSON_PARSE_ERROR("Error parsing JSON");

    private final String errorMessage;

    public String format(final Object[] params) {
        return String.format(errorMessage, params);
    }
}
