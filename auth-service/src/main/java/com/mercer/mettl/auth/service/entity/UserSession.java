package com.mercer.mettl.auth.service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userId;

    @Column(unique = true, nullable = false)
    private String refreshToken;

    private LocalDateTime expiresAt;
    private Boolean isActive;

    private LocalDateTime createdAt;
}
