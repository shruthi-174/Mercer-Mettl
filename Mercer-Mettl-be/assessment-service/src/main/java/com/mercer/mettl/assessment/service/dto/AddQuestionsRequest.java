package com.mercer.mettl.assessment.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddQuestionsRequest {

    private List<Long> questionIds;
}
