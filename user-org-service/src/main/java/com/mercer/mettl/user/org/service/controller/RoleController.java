package com.mercer.mettl.user.org.service.controller;

import com.mercer.mettl.user.org.service.entity.Role;
import com.mercer.mettl.user.org.service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> roles() {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.listRoles());
    }
}