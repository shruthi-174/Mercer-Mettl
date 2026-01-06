package com.mercer.mettl.auth.service.entity;

import com.mercer.mettl.auth.service.constants.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private Integer orgId;
    private String email;
    private String fullName;
    private String password;
    private Integer roleId;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Boolean isEmailVerified;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
