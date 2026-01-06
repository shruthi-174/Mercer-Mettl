package com.mercer.mettl.auth.service.service;

import com.mercer.mettl.auth.service.config.JwtUtil;
import com.mercer.mettl.auth.service.constants.Constants;
import com.mercer.mettl.auth.service.constants.ErrorMessage;
import com.mercer.mettl.auth.service.constants.SecurityConstant;
import com.mercer.mettl.auth.service.dto.*;
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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

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

        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
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
        String tokenType = claims.get(SecurityConstant.CLAIM_TOKEN_TYPE, String.class);
        log.info("Refresh token type from JWT: {}", tokenType);

        if (!SecurityConstant.REFRESH_TOKEN_TYPE.equals(tokenType)) {
            log.info("Account disabled for user with email : {} {}" , tokenType, SecurityConstant.REFRESH_TOKEN_TYPE);
            throw new UnauthorizedAccessException(ErrorMessage.REFRESH_TOKEN_INVALID);
        }

        User user = userRepository
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
    @Transactional
    public MessageOutDto logout(final String accessToken) {
        log.info("Logout functionality: ");
        Claims claims = jwtUtil.parse(accessToken);
        Integer userId = Integer.valueOf(claims.getSubject());
        sessionRepository.deleteByUserId(userId);
        return new MessageOutDto(Constants.USER_LOGOUT);
    }

    public MessageOutDto forgotPassword(ForgotPasswordRequest request) {

        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(request.getEmail());

        // Security: do not reveal user existence
        if (userOpt.isEmpty()) {
            return new MessageOutDto(Constants.EMAIL_SENT_SUCCESS
            );
        }

        User user = userOpt.get();

        String resetToken = jwtUtil.generateResetToken(user);

        // Email integration point
        log.info(
                "Password reset: token={}",
                resetToken
        );

        return new MessageOutDto(Constants.EMAIL_SENT_SUCCESS);
    }

    public MessageOutDto resetPassword(ResetPasswordRequest request) {

        Claims claims;
        try {
            claims = jwtUtil.parse(request.getToken());
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessage.RESET_TOKEN_INVALID.getErrorMessage());
        }

        String tokenType = claims.get(SecurityConstant.CLAIM_TOKEN_TYPE, String.class);
        if (!SecurityConstant.RESET_TOKEN_TYPE.equals(tokenType)) {
            throw new RuntimeException(ErrorMessage.RESET_TOKEN_INVALID.getErrorMessage());
        }

        Integer userId = Integer.valueOf(claims.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return new MessageOutDto(Constants.PASSWORD_RESET_SUCCESS);
    }
}
