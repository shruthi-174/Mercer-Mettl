package com.mercer.mettl.assessment.service.dto;

import lombok.Data;

@Data
public class AnswerRequest {
    private Long questionId;
    private Long selectedOptionId;
}