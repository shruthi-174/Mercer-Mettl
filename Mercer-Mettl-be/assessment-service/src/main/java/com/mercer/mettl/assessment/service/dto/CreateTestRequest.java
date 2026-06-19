package com.mercer.mettl.assessment.service.dto;

import lombok.Data;

@Data
public class CreateTestRequest {

    private String title;

    private String tag;

    private Integer durationMinutes;
}