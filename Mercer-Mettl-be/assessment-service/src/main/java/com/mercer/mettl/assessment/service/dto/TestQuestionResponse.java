package com.mercer.mettl.assessment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TestQuestionResponse {

    private Long questionId;

    private String questionText;

    private Integer difficulty;

    private List<OptionResponse> options;
}