package com.mercer.mettl.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AuthResponse {
    public String accessToken;
    public String refreshToken;
    public String tokenType;
    public long expiresIn;
}
