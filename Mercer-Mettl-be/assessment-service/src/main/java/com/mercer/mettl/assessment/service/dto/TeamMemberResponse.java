package com.mercer.mettl.assessment.service.dto;

import com.mercer.mettl.assessment.service.entity.TeamStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamMemberResponse {

    private Integer userId;

    private String name;

    private String email;

    private TeamStatus status;

}