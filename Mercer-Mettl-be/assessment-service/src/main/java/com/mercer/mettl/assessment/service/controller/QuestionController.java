package com.mercer.mettl.assessment.service.controller;

import com.mercer.mettl.assessment.service.dto.CreateQuestionRequest;
import com.mercer.mettl.assessment.service.dto.EvaluateRequest;
import com.mercer.mettl.assessment.service.dto.EvaluateResponse;
import com.mercer.mettl.assessment.service.dto.QuestionResponse;
import com.mercer.mettl.assessment.service.dto.*;
import com.mercer.mettl.assessment.service.entity.Question;
import com.mercer.mettl.assessment.service.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/generate")
//    @PreAuthorize("hasAuthority('RECRUITER')")
    public List<QuestionResponse> generate(
            @RequestParam String skill,
            @RequestParam int experience,
            @RequestParam(defaultValue = "5") int count) {
        log.info("Received request to generate questions for skill: {}, experience: {}, count: {}", skill, experience, count);
        return questionService.generateMCQs(skill, experience, count);
    }

    @PostMapping("/evaluate")
    public EvaluateResponse evaluate(@RequestBody EvaluateRequest req) {
        log.info("Evaluating question with ID: {} and selected option ID: {}", req.getQuestionId(), req.getSelectedOptionId());
        return questionService.evaluateAnswer(req);
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(
            @RequestBody CreateQuestionRequest request) {

        return ResponseEntity.ok(
                questionService.createQuestionManually(request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(final @PathVariable Long id) {

        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(final @PathVariable Long id) {

        return ResponseEntity.ok(
                questionService.getQuestion(id)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions() {

        return ResponseEntity.ok(
                questionService.getAllQuestions()
        );
    }

    @GetMapping()
    public ResponseEntity<List<Question>> getByTag(final @RequestParam String tag) {

        return ResponseEntity.ok(
                questionService.getQuestionsByTag(tag)
        );
    }

    @GetMapping("/random")
    public ResponseEntity<List<Question>> getRandomQuestions(
           final  @RequestParam(defaultValue = "5") int count) {

        return ResponseEntity.ok(
                questionService.getRandomQuestions(count)
        );
    }

    @GetMapping("/tags")
    public ResponseEntity<List<String>> getTags() {
        return ResponseEntity.ok(
                questionService.getTags()
        );
    }
}