package com.mercer.mettl.assessment.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_assignments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long testId;

    private Integer userId;

    private TestStatus status;

    private LocalDateTime assignedAt;
}