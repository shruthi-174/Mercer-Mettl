package com.mercer.mettl.user.org.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDto {
    private Integer userId;
    private String email;
    private String fullName;
    private String role;
    private Integer orgId;
}

