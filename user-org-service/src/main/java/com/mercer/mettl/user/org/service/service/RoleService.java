package com.mercer.mettl.user.org.service.service;

import com.mercer.mettl.user.org.service.entity.Role;
import com.mercer.mettl.user.org.service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }
}
