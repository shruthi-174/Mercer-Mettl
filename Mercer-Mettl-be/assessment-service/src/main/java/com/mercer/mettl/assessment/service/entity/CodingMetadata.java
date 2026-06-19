package com.mercer.mettl.assessment.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CodingMetadata {

    @Id
    private Long questionId;

    private String language;

    @Column(columnDefinition = "TEXT")
    private String starterCode;

    @Column(columnDefinition = "TEXT")
    private String solutionCode;

    private Integer timeLimitSeconds;
}
