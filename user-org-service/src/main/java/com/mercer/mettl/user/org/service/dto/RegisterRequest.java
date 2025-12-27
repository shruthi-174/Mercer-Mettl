package com.mercer.mettl.user.org.service.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    public String email;
    public String password;
    public String fullName;
    public Integer orgId;
    public String role;
}
