package com.mercer.mettl.assessment.service.repository;

import com.mercer.mettl.assessment.service.entity.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    List<TestQuestion> findByTestId(Long testId);

    void deleteByTestIdAndQuestionId(Long testId, Long questionId);
}
