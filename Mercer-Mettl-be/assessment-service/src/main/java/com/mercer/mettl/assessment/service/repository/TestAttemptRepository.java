package com.mercer.mettl.assessment.service.repository;

import com.mercer.mettl.assessment.service.entity.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
//    List<TestAttempt> findByTestIdAndUserId(Long testId, Integer userId);

    Optional<TestAttempt> findTopByTestIdAndUserIdOrderByStartTimeDesc(Long testId, Integer userId);

    List<TestAttempt> findByTestId(Long testId);

    List<TestAttempt> findByUserId(Integer userId);

//        Collection<Object> findByTotalQuestions(Long id);
}
