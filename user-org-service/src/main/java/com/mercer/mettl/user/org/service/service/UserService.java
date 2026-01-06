package com.mercer.mettl.user.org.service.service;

import com.mercer.mettl.user.org.service.config.SecurityUtil;
import com.mercer.mettl.user.org.service.constants.Constants;
import com.mercer.mettl.user.org.service.constants.ErrorMessage;
import com.mercer.mettl.user.org.service.constants.Status;
import com.mercer.mettl.user.org.service.dto.ChangeRoleRequest;
import com.mercer.mettl.user.org.service.dto.MessageOutDto;
import com.mercer.mettl.user.org.service.dto.RegisterRequest;
import com.mercer.mettl.user.org.service.dto.UserProfileDto;
import com.mercer.mettl.user.org.service.entity.Organization;
import com.mercer.mettl.user.org.service.entity.Role;
import com.mercer.mettl.user.org.service.entity.User;
import com.mercer.mettl.user.org.service.exception.ResourceConflictException;
import com.mercer.mettl.user.org.service.exception.ResourceNotFoundException;
import com.mercer.mettl.user.org.service.repository.OrganizationRepository;
import com.mercer.mettl.user.org.service.repository.RoleRepository;
import com.mercer.mettl.user.org.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository orgRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUtil securityUtil;

    // REGISTER
    public MessageOutDto register(final RegisterRequest request) {
        log.info("Registering user with email : {}", request.email);

        if (userRepository.findByEmail(request.email).isPresent()) {
            log.error("User with email {} already exists", request.email);
            throw new ResourceConflictException(ErrorMessage.USER_ALREADY_EXISTS, request.email);
        }

        Organization org = orgRepository.findById(request.getOrgId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage
        .ORG_NOT_FOUND,request.getOrgId()));

        Role role = roleRepository.findByRoleName(request.role)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.ROLE_NOT_FOUND,request.getRole()));

        User user = new User();
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setFullName(request.fullName);
        user.setOrgId(org.getOrgId());
        user.setRoleId(role.getId());
        user.setIsActive(true);
        user.setIsEmailVerified(false);
        user.setCreatedAt(LocalDateTime.now());

        log.info("Persisting user with email : {}", request.email);
        userRepository.save(user);

        return new MessageOutDto(Constants.USER_SUCCESS);
    }

    public UserProfileDto getMyProfile() {
        Integer userId = securityUtil.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage
                        .USER_NOT_FOUND,userId));

        Role role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.ROLE_NOT_FOUND));

        return new UserProfileDto(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                role.getRoleName(),
                user.getOrgId()
        );
    }

    public List<User> getOrgUsers() {
        Integer adminId = securityUtil.getCurrentUserId();
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND, adminId));

        return userRepository.findByOrgIdAndStatus(admin.getOrgId(), Status.ACTIVE);
    }

    public MessageOutDto deleteUser(final Integer id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setStatus(Status.INACTIVE);
        user.setIsActive(false);
        userRepository.save(user);

        return new MessageOutDto(Constants.USER_DELETE_SUCCESS);
    }

    public MessageOutDto changeRole(final Integer id, final ChangeRoleRequest req) {
        Role role = roleRepository.findByRoleName(req.getRole())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.ROLE_NOT_FOUND));
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND, id));
        user.setRoleId(role.getId());
        userRepository.save(user);

        return new MessageOutDto(Constants.ROLE_CHANGE_SUCCESS);
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }
}
