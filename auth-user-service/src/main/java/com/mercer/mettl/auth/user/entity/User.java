package com.mercer.mettl.auth.user.entity;

import com.mercer.mettl.auth.user.constants.Role;
import com.mercer.mettl.auth.user.constants.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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
    private Role role;
    private Status status;
    private Boolean isEmailVerified;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
