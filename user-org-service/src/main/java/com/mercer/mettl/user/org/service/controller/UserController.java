package com.mercer.mettl.user.org.service.controller;

import com.mercer.mettl.user.org.service.constants.Constants;
import com.mercer.mettl.user.org.service.constants.ErrorMessage;
import com.mercer.mettl.user.org.service.dto.MessageOutDto;
import com.mercer.mettl.user.org.service.dto.RegisterRequest;
import com.mercer.mettl.user.org.service.exception.UnauthorizedAccessException;
import com.mercer.mettl.user.org.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<MessageOutDto> register(
           final @RequestHeader("X-ROLE") String role,
            final @RequestHeader("X-USER-ID") Integer requesterId,
            final @RequestBody RegisterRequest request) {

        log.info("Received registration request for email: {}", request.getEmail());

        if (!(Constants.ORG_ADMIN).equals(role)) {
            log.error("Unauthorized access attempt to register user by role: {}", role);
            throw new UnauthorizedAccessException(ErrorMessage.ORG_ADMIN_ACCESS);
        }

        MessageOutDto response = userService.register(request);
        log.info("User registered successfully with email: {}", request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/create-org-admin")
    public ResponseEntity<MessageOutDto> createOrgAdmin(
            @RequestHeader("X-ROLE") String role,
            @RequestBody RegisterRequest request) {

        if (!(Constants.ADMIN).equals(role)) {
            log.error("Unauthorized access attempt to create org admin by role: {}", role);
            throw new UnauthorizedAccessException(ErrorMessage.ADMIN_ACCESS);
        }

        request.setRole(Constants.ORG_ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

}
