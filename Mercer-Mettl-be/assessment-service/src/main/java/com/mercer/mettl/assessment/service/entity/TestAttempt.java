package com.mercer.mettl.assessment.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_attempts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long testId;

    private Integer userId;

    private Integer score;

    private Integer totalQuestions;

    private boolean passed;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
