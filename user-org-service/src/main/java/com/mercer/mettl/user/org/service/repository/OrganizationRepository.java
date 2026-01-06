package com.mercer.mettl.user.org.service.repository;

import com.mercer.mettl.user.org.service.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization,Integer> {

    Optional<Organization> findByDomain(String domain);
}
