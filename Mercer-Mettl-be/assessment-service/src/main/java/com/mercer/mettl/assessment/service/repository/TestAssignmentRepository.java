package com.mercer.mettl.assessment.service.repository;

import com.mercer.mettl.assessment.service.entity.TestAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestAssignmentRepository extends JpaRepository<TestAssignment, Long> {
//    Optional<TestAssignment> findByTestIdAndUserId(Long testId, Integer userId);

    List<TestAssignment> findByUserId(Integer userId);

    Optional<TestAssignment> findTopByTestIdAndUserIdOrderByAssignedAtDesc(Long testId, Integer userId);
}
