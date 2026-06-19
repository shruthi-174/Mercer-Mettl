package com.mercer.mettl.assessment.service.repository;

import com.mercer.mettl.assessment.service.entity.TestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestAnswerRepository extends JpaRepository<TestAnswer, Long> {
}
