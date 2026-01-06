package com.mercer.mettl.auth.service.repository;

import com.mercer.mettl.auth.service.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
    Optional<UserSession> findByRefreshTokenAndIsActiveTrue(String refreshToken);

    void deleteByUserId(Integer userId);
}
