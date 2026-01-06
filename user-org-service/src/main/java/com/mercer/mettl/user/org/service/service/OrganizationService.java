package com.mercer.mettl.user.org.service.service;

import com.mercer.mettl.user.org.service.constants.Constants;
import com.mercer.mettl.user.org.service.constants.ErrorMessage;
import com.mercer.mettl.user.org.service.dto.MessageOutDto;
import com.mercer.mettl.user.org.service.dto.OrganizationInDto;
import com.mercer.mettl.user.org.service.dto.OrganizationOutDto;
import com.mercer.mettl.user.org.service.dto.OrganizationUpdateDto;
import com.mercer.mettl.user.org.service.entity.Organization;
import com.mercer.mettl.user.org.service.exception.ResourceConflictException;
import com.mercer.mettl.user.org.service.exception.ResourceNotFoundException;
import com.mercer.mettl.user.org.service.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    //create organization
    public MessageOutDto createOrganization(final OrganizationInDto organizationDto) {
        log.info("Creating organization with domain : {}" , organizationDto.getDomain());
        Optional<Organization> organizationExists = organizationRepository.
                findByDomain(organizationDto.getDomain());

        if(organizationExists.isPresent()){
            log.error("Organization with domain {} already exists" , organizationDto.getDomain());
            throw new ResourceConflictException(ErrorMessage.ORG_ALREADY_EXISTS, organizationDto.getDomain());
        }

        Organization org = new Organization();
        org.setName(organizationDto.getName());
        org.setDomain(organizationDto.getDomain());
        org.setContactEmail(organizationDto.getContactEmail());
        org.setIsActive(organizationDto.getIsActive());
        org.setCreatedAt(organizationDto.getCreatedAt());
        org.setUpdatedAt(organizationDto.getCreatedAt());
        organizationRepository.save(org);
        log.info("Persisting in organization table : {}", org);
        return new MessageOutDto(Constants.ORG_SUCESS);
    }

    //update organization
    public MessageOutDto updateOrganizationDetails(final Integer orgId,
                                                   final OrganizationUpdateDto organizationDto) {
        log.info("Updating organization details for orgId : {}" , orgId);
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorMessage.ORG_NOT_FOUND, orgId));

        org.setName(organizationDto.getName());
        org.setContactEmail(organizationDto.getContactEmail());
        org.setIsActive(organizationDto.getIsActive());
        org.setUpdatedAt(organizationDto.getUpdatedAt());
        organizationRepository.save(org);
        log.info("Persisting organization details for orgId : {}" , orgId);
        return new MessageOutDto(Constants.ORG_UPDATE_SUCCESS);
    }

    //get org Details
    public Organization getOrganizationDetails(final Integer orgId) {
        log.info("Fetching organization details for orgId : {}" , orgId);
        return organizationRepository.findById(orgId)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorMessage.ORG_NOT_FOUND, orgId));
    }

    //get All org Details
    public List<OrganizationOutDto> getAllOrganizationDetails() {
        log.info("Fetching all organization details");
        List<Organization> orgList = organizationRepository.findAll();
        return orgList.stream().map(org -> new OrganizationOutDto(
                org.getOrgId(),
                org.getName(),
                org.getDomain(),
                org.getContactEmail(),
                org.getIsActive(),
                org.getCreatedAt(),
                org.getUpdatedAt()
        )).collect(Collectors.toList());
    }
}
