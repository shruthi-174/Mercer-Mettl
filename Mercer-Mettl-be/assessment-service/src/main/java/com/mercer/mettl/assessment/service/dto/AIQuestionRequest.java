package com.mercer.mettl.assessment.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class AIQuestionRequest {
    private String questionText;
    private List<OptionRequest> options;
    private List<String> tags;
}

