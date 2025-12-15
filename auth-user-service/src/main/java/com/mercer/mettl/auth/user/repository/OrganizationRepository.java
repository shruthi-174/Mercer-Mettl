package com.mercer.mettl.auth.user.repository;

import com.mercer.mettl.auth.user.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization,Integer> {

    Optional<Organization> findByDomain(String domain);
}
