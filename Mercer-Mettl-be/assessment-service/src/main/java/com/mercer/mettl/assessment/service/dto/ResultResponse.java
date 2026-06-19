package com.mercer.mettl.assessment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResultResponse {

    private Integer score;

    private Integer totalQuestions;

    private boolean passed;

    private Integer percentage;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long durationUsedMinutes;

    private boolean timeExceeded;

}