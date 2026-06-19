package com.mercer.mettl.user.org.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Integer id;
    private String email;
    private String fullName;
    private String role;
    private Integer orgId;

}
