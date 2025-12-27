package com.mercer.mettl.auth.service.service;

import com.mercer.mettl.auth.service.config.JwtUtil;
import com.mercer.mettl.auth.service.constants.ErrorMessage;
import com.mercer.mettl.auth.service.constants.SecurityConstant;
import com.mercer.mettl.auth.service.dto.AuthRequest;
import com.mercer.mettl.auth.service.dto.AuthResponse;
import com.mercer.mettl.auth.service.dto.RefreshRequest;
import com.mercer.mettl.auth.service.entity.Role;
import com.mercer.mettl.auth.service.entity.User;
import com.mercer.mettl.auth.service.entity.UserSession;
import com.mercer.mettl.auth.service.exception.InvalidRequestException;
import com.mercer.mettl.auth.service.exception.ResourceNotFoundException;
import com.mercer.mettl.auth.service.exception.UnauthorizedAccessException;
import com.mercer.mettl.auth.service.repository.RoleRepository;
import com.mercer.mettl.auth.service.repository.UserRepository;
import com.mercer.mettl.auth.service.repository.UserSessionRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository authUserRepository;

    @Autowired
    private UserSessionRepository sessionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // LOGIN
    public AuthResponse login(final AuthRequest request) {
        log.info("Logging in user with email : {}" , request.email);

        User user = authUserRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND, request.email));

        log.info("Verifying password for user with email : {}" , request.email);
        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            log.info("Incorrect password for user with email : {}" , request.email);
            throw new InvalidRequestException(ErrorMessage.PASSWORD_INCORRECT);
        }

        if (!user.getIsActive()) {
            log.info("Account disabled for user with email : {}" , request.email);
            throw new InvalidRequestException(ErrorMessage.ACCOUNT_DISABLED);
        }


        Role role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.ROLE_NOT_FOUND, user.getRoleId().toString()));

        log.info("Generating access token for user with email : {}" , request.email);
        String accessToken = jwtUtil.generateAccessToken(
                user,
                role.getRoleName()
        );

        log.info("Generating refresh token for user with email : {}" , request.email);
        String refreshToken = jwtUtil.generateRefreshToken(
                user,
                role.getRoleName()
        );

        UserSession session = new UserSession();
        session.setUserId(user.getUserId());
        session.setRefreshToken(refreshToken);
        session.setIsActive(true);
        session.setExpiresAt(LocalDateTime.now().plusDays(30));
        session.setCreatedAt(LocalDateTime.now());

        sessionRepository.save(session);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(SecurityConstant.BEARER_PREFIX)
                .expiresIn(SecurityConstant.EXIPRY_IN)
                .build();
    }

    // REFRESH
    public AuthResponse refresh(final RefreshRequest request) {
        log.info("Refreshing access token");

        //Find refresh token in DB
        UserSession session = sessionRepository
                .findByRefreshTokenAndIsActiveTrue(request.refreshToken)
                .orElseThrow(() -> new UnauthorizedAccessException(ErrorMessage.REFRESH_TOKEN_INVALID));

        Claims claims = jwtUtil.parse(request.refreshToken);

        if (!SecurityConstant.REFRESH_TOKEN_TYPE.equals(claims.get(SecurityConstant.CLAIM_TOKEN_TYPE))) {
            throw new UnauthorizedAccessException(ErrorMessage.REFRESH_TOKEN_INVALID);
        }

        User user = authUserRepository
                .findById(session.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND));


        Role role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.ROLE_NOT_FOUND, user.getRoleId().toString()));

        String newAccessToken = jwtUtil.generateAccessToken(
                user,
                role.getRoleName()
        );

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.refreshToken)
                .tokenType(SecurityConstant.BEARER_PREFIX)
                .expiresIn(SecurityConstant.EXIPRY_IN)
                .build();
    }

    // LOGOUT
    public void logout(final String accessToken) {
        Claims claims = jwtUtil.parse(accessToken);
        Integer userId = Integer.valueOf(claims.getSubject());
        sessionRepository.deleteByUserId(userId);
    }
}
