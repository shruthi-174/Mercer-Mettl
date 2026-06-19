package com.mercer.mettl.assessment.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercer.mettl.assessment.service.constants.Constants;
import com.mercer.mettl.assessment.service.constants.ErrorMessage;
import com.mercer.mettl.assessment.service.dto.*;
import com.mercer.mettl.assessment.service.exception.InvalidRequestException;
import com.mercer.mettl.assessment.service.exception.ResourceNotFoundException;
import com.mercer.mettl.assessment.service.constants.QuestionType;
import com.mercer.mettl.assessment.service.dto.*;
import com.mercer.mettl.assessment.service.entity.MCQOption;
import com.mercer.mettl.assessment.service.entity.Question;
import com.mercer.mettl.assessment.service.entity.QuestionTag;
import com.mercer.mettl.assessment.service.helper.ServiceHelper;
import com.mercer.mettl.assessment.service.repository.MCQOptionRepository;
import com.mercer.mettl.assessment.service.repository.QuestionRepository;
import com.mercer.mettl.assessment.service.repository.QuestionTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QuestionService {

    @Autowired
    private  QuestionRepository questionRepository;
    @Autowired
    private  MCQOptionRepository optionRepository;
    @Autowired
    private  QuestionTagRepository tagRepository;
    @Autowired
    private  QuestionAIProcessor aiProcessor;
    @Autowired
    private  ObjectMapper objectMapper;
    @Autowired
    private ServiceHelper serviceHelper;

    public List<QuestionResponse> generateMCQs(final String skill, final int experience, final int count) {

        log.info("Generating {} questions for skill: {}, experience: {}", count, skill, experience);

        String raw = aiProcessor.generateMCQs(skill, experience, count);

        // STEP 1: sanitize raw AI output
        String json = serviceHelper.sanitize(raw);

        // STEP 2: try safe parsing with fallback repair
        AIQuestionResponse aiResponse;

        try {
            log.info("AI Response JSON (sanitized): {}", json);

            aiResponse = objectMapper.readValue(json, AIQuestionResponse.class);

        } catch (Exception e1) {

            log.warn("First JSON parse failed, attempting repair strategy...");

            try {
                // 🔥 FIX: escape unescaped quotes inside text fields (common Ollama issue)
                String repaired = repairJson(json);

                log.info("Repaired JSON: {}", repaired);

                aiResponse = objectMapper.readValue(repaired, AIQuestionResponse.class);

            } catch (Exception e2) {
                log.error("Failed to parse AI response even after repair", e2);
                throw new InvalidRequestException(ErrorMessage.JSON_PARSE_ERROR);
            }
        }

        List<QuestionResponse> response = new ArrayList<>();

        for (AIQuestionRequest aiQ : aiResponse.getQuestions()) {

            serviceHelper.validate(aiQ);

            Question q = questionRepository.save(
                    new Question(
                            null,
                            QuestionType.MCQ,
                            aiQ.getQuestionText(),
                            serviceHelper.mapDifficulty(experience),
                            "AI",
                            LocalDateTime.now()
                    )
            );

            List<OptionResponse> options = new ArrayList<>();

            for (OptionRequest o : aiQ.getOptions()) {

                MCQOption saved = optionRepository.save(
                        new MCQOption(null, q.getId(), o.getText(), o.isCorrect())
                );

                options.add(new OptionResponse(saved.getId(), saved.getOptionText()));
            }

            if (aiQ.getTags() != null) {
                aiQ.getTags().forEach(tag ->
                        tagRepository.save(new QuestionTag(null, q.getId(), tag))
                );
            }

            response.add(new QuestionResponse(
                    q.getId(),
                    q.getQuestionText(),
                    q.getDifficulty(),
                    options
            ));
        }

        return response;
    }

    private String repairJson(String json) {

        if (json == null) return null;

        // Fix common Ollama issue: unescaped quotes inside strings
        json = json.replaceAll("(?<!\\\\)\\\"(?=[^:,}\\]]*\\\")", "\\\\\"");

        // Remove trailing commas before closing braces/brackets
        json = json.replaceAll(",\\s*}", "}");
        json = json.replaceAll(",\\s*]", "]");

        return json;
    }

    public Question createQuestionManually(final CreateQuestionRequest request) {
        log.info("Creating question manually with text: {}", request.getQuestionText());
        Question question = new Question();
        question.setQuestionText(request.getQuestionText());
        question.setDifficulty(request.getDifficulty());
        question.setType(QuestionType.MCQ);
        question.setCreatedBy("ADMIN");

        log.info("Persisting question: {}", question.getQuestionText());
        Question saved = questionRepository.save(question);

        // save options
        log.info("Persisting options for question ID {}: {}", saved.getId(), request.getOptions());
        request.getOptions().forEach(opt -> {
            MCQOption option = new MCQOption();
            option.setQuestionId(saved.getId());
            option.setOptionText(opt.getText());
            option.setIsCorrect(opt.isCorrect());

            optionRepository.save(option);
        });

        // save tags
        log.info("Persisting tags for question ID {}: {}", saved.getId(), request.getTags());
        if (request.getTags() != null) {
            request.getTags().forEach(tag -> {
                tagRepository.save(new QuestionTag(null, saved.getId(), tag));
            });
        }
        return saved;
    }

    public MessageOutDto deleteQuestion(Long id) {
        log.info("Deleting question with ID {}", id);
        questionRepository.deleteById(id);
        return new MessageOutDto(Constants.QUESTION_DELETE_SUCCESS);
    }

    public QuestionResponse getQuestion(Long id) {
    log.info("Fetching question with ID {}", id);
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.QUESTION_NOT_FOUND, id));

        List<OptionResponse> options =
                optionRepository.findByQuestionId(id)
                        .stream()
                        .map(o -> new OptionResponse(o.getId(), o.getOptionText()))
                        .toList();

        return new QuestionResponse(
                q.getId(),
                q.getQuestionText(),
                q.getDifficulty(),
                options
        );
    }

    public List<Question> getAllQuestions() {
        log.info("Fetching all questions");
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByTag(String tag) {
        log.info("Fetching questions with tag: {}", tag);
        return questionRepository.findByTag(tag);
    }

    public List<Question> getRandomQuestions(int count) {
        log.info("Fetching {} random questions", count);
        List<Question> all = questionRepository.findAll();
        Collections.shuffle(all);
        return all.stream().limit(count).toList();
    }

    public EvaluateResponse evaluateAnswer(final EvaluateRequest request) {
        log.info("Evaluating answer for question ID {}, selected option ID {}", request.getQuestionId(), request.getSelectedOptionId());
        MCQOption option = optionRepository.findById(request.getSelectedOptionId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.OPTION_NOT_FOUND, request.getSelectedOptionId()));

        if (!option.getQuestionId().equals(request.getQuestionId())) {
            throw new InvalidRequestException(ErrorMessage.INVALID_OPTION, request.getSelectedOptionId(), request.getQuestionId());
        }
        return new EvaluateResponse(option.getIsCorrect());
    }

    public List<String> getTags() {
        log.info("Fetching all tags");
        return tagRepository.findAllTags();
    }
}
