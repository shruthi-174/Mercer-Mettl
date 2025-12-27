package com.mercer.mettl.user.org.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrganizationUpdateDto {
    private String name;
    private String contactEmail;
    private Boolean isActive;
    private LocalDateTime updatedAt;
}
