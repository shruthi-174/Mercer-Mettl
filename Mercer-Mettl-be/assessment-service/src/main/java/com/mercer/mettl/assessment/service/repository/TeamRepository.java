package com.mercer.mettl.assessment.service.repository;

import com.mercer.mettl.assessment.service.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByOrgId(Integer orgId);
}
