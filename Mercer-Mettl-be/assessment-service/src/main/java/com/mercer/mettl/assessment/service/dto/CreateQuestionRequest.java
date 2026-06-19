package com.mercer.mettl.assessment.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateQuestionRequest {

    private String questionText;
    private Integer difficulty;
    private List<OptionRequest> options;
    private List<String> tags;
}