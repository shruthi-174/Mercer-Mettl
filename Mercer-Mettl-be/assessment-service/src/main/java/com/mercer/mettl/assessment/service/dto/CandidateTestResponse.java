package com.mercer.mettl.assessment.service.dto;

import com.mercer.mettl.assessment.service.entity.TestStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateTestResponse {


    private Long testId;

    private String title;

    private String tag;

    private Integer durationMinutes;


    private TestStatus status;


    private Integer score;

    private Integer totalQuestions;


    private boolean passed;


}
