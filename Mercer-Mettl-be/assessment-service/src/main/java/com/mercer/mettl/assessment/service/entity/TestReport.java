package com.mercer.mettl.assessment.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;
    private Long testId;
    
    private Integer userId;

    private String userName;
    private String email;

    private Integer score;
    private Integer totalQuestions;
    private Boolean passed;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private TestStatus status;
}
