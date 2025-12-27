package com.mercer.mettl.user.org.service.service;

import com.mercer.mettl.user.org.service.constants.Constants;
import com.mercer.mettl.user.org.service.constants.ErrorMessage;
import com.mercer.mettl.user.org.service.dto.MessageOutDto;
import com.mercer.mettl.user.org.service.dto.RegisterRequest;
import com.mercer.mettl.user.org.service.entity.Organization;
import com.mercer.mettl.user.org.service.entity.Role;
import com.mercer.mettl.user.org.service.entity.User;
import com.mercer.mettl.user.org.service.exception.ResourceConflictException;
import com.mercer.mettl.user.org.service.repository.OrganizationRepository;
import com.mercer.mettl.user.org.service.repository.RoleRepository;
import com.mercer.mettl.user.org.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    // REGISTER
    public MessageOutDto register(final RegisterRequest request) {
        log.info("Registering user with email : {}", request.email);

        if (userRepository.findByEmail(request.email).isPresent()) {
            log.error("User with email {} already exists", request.email);
            throw new ResourceConflictException(ErrorMessage.USER_ALREADY_EXISTS, request.email);
        }

        Organization org = orgRepository.findById(request.getOrgId())
                .orElseThrow(() -> new RuntimeException("Invalid orgId"));

        Role role = roleRepository.findByRoleName(request.role)
                .orElseThrow(() -> new RuntimeException("Invalid role"));

        User user = new User();
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setFullName(request.fullName);
        user.setOrgId(request.getOrgId());
        user.setRoleId(role.getId());
        user.setIsActive(true);
        user.setIsEmailVerified(false);
        user.setCreatedAt(LocalDateTime.now());

        log.info("Persisting user with email : {}", request.email);
        userRepository.save(user);

        return new MessageOutDto(Constants.USER_SUCCESS);
    }
}
