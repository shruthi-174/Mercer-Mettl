package com.mercer.mettl.auth.service.controller;

import com.mercer.mettl.auth.service.config.JwtUtil;
import com.mercer.mettl.auth.service.dto.*;
import com.mercer.mettl.auth.service.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request) {

        log.info("Received login request for email: {}", request.getEmail());
        AuthResponse response =  authService.login(request);
        log.info("User logged in successfully with email: {}", request.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            final @RequestBody RefreshRequest request) {

        log.info("Received token refresh request");
        AuthResponse response = authService.refresh(request);
        log.info("Token refreshed successfully for user");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageOutDto> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        log.info("Received logout request");

        String token = authHeader.substring(7); // remove "Bearer "
        MessageOutDto response = authService.logout(token);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<MessageOutDto> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageOutDto> resetPassword(
            @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.resetPassword(request));
    }
}

