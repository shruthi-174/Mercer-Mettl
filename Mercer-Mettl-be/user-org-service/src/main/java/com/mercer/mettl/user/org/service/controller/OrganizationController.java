package com.mercer.mettl.user.org.service.controller;

import com.mercer.mettl.user.org.service.constants.Constants;
import com.mercer.mettl.user.org.service.constants.ErrorMessage;
import com.mercer.mettl.user.org.service.dto.*;
import com.mercer.mettl.user.org.service.entity.Organization;
import com.mercer.mettl.user.org.service.exception.UnauthorizedAccessException;
import com.mercer.mettl.user.org.service.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/org")
@Slf4j
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MessageOutDto> createOrganization(
                                                           final  @RequestBody OrganizationInDto organizationDto) {
        log.info("Received request to create organization with domain: {}", organizationDto.getDomain());

        MessageOutDto response = organizationService.createOrganization(organizationDto);
        log.info("Organization created successfully with domain: {}", organizationDto.getDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{orgId}")
    @PreAuthorize("hasAuthority('ORG_ADMIN')")
    public ResponseEntity<MessageOutDto> updateOrganizationDetails(
            final @PathVariable Integer orgId,
            final @RequestBody OrganizationUpdateDto organizationDto) {
        log.info("Received request to update organization with orgId: {}", orgId);
        MessageOutDto response = organizationService.updateOrganizationDetails(orgId, organizationDto);
        log.info("Organization updated successfully with orgId: {}", orgId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{orgId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ORG_ADMIN')")
    public ResponseEntity<Organization> getOrganizationDetails(
            final @PathVariable Integer orgId) {
        log.info("Received request to get organization details for orgId: {}", orgId);
        Organization response = organizationService.getOrganizationDetails(orgId);
        log.info("Organization details retrieved successfully for orgId: {}", orgId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OrganizationOutDto>> getAllOrganizationDetails() {
        log.info("Received request to get all organization details ");
        List<OrganizationOutDto> response = organizationService.getAllOrganizationDetails();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orgId}/users")
//    @PreAuthorize("hasAnyAuthority('ADMIN','ORG_ADMIN')")
    public ResponseEntity<List<UserProfileDto>> getOrgUsers(
            @PathVariable Integer orgId) {

        return ResponseEntity.ok(
                organizationService.getUsersByOrganization(orgId)
        );
    }
}
