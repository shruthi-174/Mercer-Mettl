package com.mercer.mettl.assessment.service.repository;

import com.mercer.mettl.assessment.service.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {

    @Query("SELECT DISTINCT q.tag FROM QuestionTag q")
    List<String> findAllTags();
}

