package com.mercer.mettl.auth.service.dto;

import lombok.Data;

@Data
public class AuthRequest {

    public String email;
    public String password;
}
