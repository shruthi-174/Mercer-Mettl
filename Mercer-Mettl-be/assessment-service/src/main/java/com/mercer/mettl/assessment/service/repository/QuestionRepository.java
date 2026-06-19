package com.mercer.mettl.assessment.service.repository;

import com.mercer.mettl.assessment.service.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
           SELECT q FROM Question q
           JOIN QuestionTag t ON q.id = t.questionId
           WHERE t.tag = :tag
           """)
    List<Question> findByTag(String tag);
}

