package com.mercer.mettl.assessment.service.dto;

import com.mercer.mettl.assessment.service.entity.TestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResponse {

    private Long testId;

    private String title;

    private String tag;

    private Integer durationMinutes;

    private boolean published;

    private int totalQuestions;

    private DifficultyDistribution difficultyDistribution;

    private TestStatus status;
}
