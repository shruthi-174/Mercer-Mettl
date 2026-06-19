package com.mercer.mettl.assessment.service.repository;

import com.mercer.mettl.assessment.service.entity.MCQOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MCQOptionRepository extends JpaRepository<MCQOption, Long> {
    List<MCQOption> findByQuestionId(Long questionId);
}
