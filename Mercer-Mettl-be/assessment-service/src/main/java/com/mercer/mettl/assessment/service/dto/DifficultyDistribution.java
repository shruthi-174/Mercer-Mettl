package com.mercer.mettl.assessment.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DifficultyDistribution {

    private long easy;
    private long medium;
    private long hard;
}
