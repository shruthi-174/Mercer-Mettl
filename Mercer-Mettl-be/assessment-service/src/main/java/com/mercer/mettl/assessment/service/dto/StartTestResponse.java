package com.mercer.mettl.assessment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StartTestResponse {
    private Long attemptId;
    private Long testId;
    private Integer durationMinutes;
    private String message;
    private LocalDateTime startTime;
}