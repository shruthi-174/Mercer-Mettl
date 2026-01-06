package com.mercer.mettl.user.org.service.controller;

import com.mercer.mettl.user.org.service.constants.Constants;
import com.mercer.mettl.user.org.service.constants.ErrorMessage;
import com.mercer.mettl.user.org.service.dto.ChangeRoleRequest;
import com.mercer.mettl.user.org.service.dto.MessageOutDto;
import com.mercer.mettl.user.org.service.dto.RegisterRequest;
import com.mercer.mettl.user.org.service.dto.UserProfileDto;
import com.mercer.mettl.user.org.service.entity.User;
import com.mercer.mettl.user.org.service.exception.UnauthorizedAccessException;
import com.mercer.mettl.user.org.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public ResponseEntity<MessageOutDto> register(
            final @RequestBody RegisterRequest request) {

        log.info("Received registration request for email: {}", request.getEmail());

        MessageOutDto response = userService.register(request);
        log.info("User registered successfully with email: {}", request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/create-org-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageOutDto> createOrgAdmin(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDto> getMyProfile() {
        UserProfileDto response = userService.getMyProfile();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/org")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOrgUsers());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public ResponseEntity<MessageOutDto> delete(@PathVariable Integer id) {
        MessageOutDto response = userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageOutDto> changeRole(@PathVariable Integer id,
                           @RequestBody ChangeRoleRequest req) {
        MessageOutDto response = userService.changeRole(id, req);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
