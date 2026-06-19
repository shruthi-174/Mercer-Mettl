package com.mercer.mettl.assessment.service.service;

import com.mercer.mettl.assessment.service.constants.Constants;
import com.mercer.mettl.assessment.service.constants.ErrorMessage;
import com.mercer.mettl.assessment.service.dto.*;
import com.mercer.mettl.assessment.service.entity.*;
import com.mercer.mettl.assessment.service.exception.InvalidRequestException;
import com.mercer.mettl.assessment.service.exception.ResourceNotFoundException;
import com.mercer.mettl.assessment.service.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestQuestionRepository testQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MCQOptionRepository optionRepository;

    @Autowired
    private TestAttemptRepository testAttemptRepository;

    @Autowired
    private TestAnswerRepository testAnswerRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TestAssignmentRepository assignmentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public TestResponse createTest(final CreateTestRequest request) {
        log.info("Creating test with title : {}", request.getTitle());
        Test test = new Test(
                null,
                request.getTitle(),
                request.getTag(),
                request.getDurationMinutes(),
                false,
                LocalDateTime.now(),
                TestStatus.CREATED
        );

        log.info("Persisting test with title : {}", request.getTitle());
        Test saved = testRepository.save(test);

        return buildResponse(saved);
    }

    public MessageOutDto addQuestions(final Long testId, final AddQuestionsRequest request) {
        log.info("Adding questions to test with id : {}", testId);
        for (Long qId : request.getQuestionIds()) {

            testQuestionRepository.save(
                    new TestQuestion(null, testId, qId)
            );
        }
        return new MessageOutDto(Constants.QUESTION_ADDED_SUCCESS);
    }

    public MessageOutDto removeQuestion(Long testId, Long questionId) {
        log.info("Removing question with id : {} from test with id : {}", questionId, testId);
        testQuestionRepository.deleteByTestIdAndQuestionId(testId, questionId);
        return new MessageOutDto(Constants.QUESTION_DELETE_SUCCESS);
    }

    public TestResponse getTest(Long id) {
        log.info("Fetching test with id : {}", id);
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.TEST_NOT_FOUND, id));

        return buildResponse(test);
    }

    public List<TestResponse> getAllTests() {
        log.info("Fetching all tests");
        return testRepository.findAll()
                .stream()
                .map(this::buildResponse)
                .toList();
    }

    public MessageOutDto publishTest(Long testId) {
        log.info("Publishing test with id : {}", testId);
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.TEST_NOT_FOUND, testId));

        test.setPublished(true);

        log.info("Persisting published test with id : {}", testId);
        testRepository.save(test);
        return new MessageOutDto(Constants.TEST_PUBLISHED_SUCCESS);
    }

    public MessageOutDto deleteTest(Long id) {
        log.info("Deleting test with id : {}", id);
        testRepository.deleteById(id);
        return new MessageOutDto(Constants.TEST_DELETE_SUCCESS);
    }

    public List<TestQuestionResponse> getQuestionsOfTest(Long testId) {
        log.info("Fetching questions for test with id : {}", testId);
        List<TestQuestion> mappings =
                testQuestionRepository.findByTestId(testId);

        List<TestQuestionResponse> response = new ArrayList<>();

        for (TestQuestion mapping : mappings) {

            Question q = questionRepository.findById(mapping.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.QUESTION_NOT_FOUND, mapping.getQuestionId()));

            List<OptionResponse> options =
                    optionRepository.findByQuestionId(q.getId())
                            .stream()
                            .map(o -> new OptionResponse(
                                    o.getId(),
                                    o.getOptionText()
                            ))
                            .toList();

            response.add(new TestQuestionResponse(
                    q.getId(),
                    q.getQuestionText(),
                    q.getDifficulty(),
                    options
            ));
        }

        return response;
    }

    private TestResponse buildResponse(Test test) {
        log.info("Building response for test with id : {}", test.getId());
        List<TestQuestion> mappings =
                testQuestionRepository.findByTestId(test.getId());

        List<Question> questions =
                questionRepository.findAllById(
                        mappings.stream()
                                .map(TestQuestion::getQuestionId)
                                .toList()
                );

        long easy = questions.stream().filter(q -> q.getDifficulty() == 1).count();
        long medium = questions.stream().filter(q -> q.getDifficulty() == 2).count();
        long hard = questions.stream().filter(q -> q.getDifficulty() == 3).count();

        DifficultyDistribution distribution =
                new DifficultyDistribution(easy, medium, hard);
        log.info("Test with id : {} has {} questions. Distribution - Easy: {}, Medium: {}, Hard: {}", test.getId(), questions.size(), easy, medium, hard);
        return new TestResponse(
                test.getId(),
                test.getTitle(),
                test.getTag(),
                test.getDurationMinutes(),
                test.isPublished(),
                questions.size(),
                distribution,
                TestStatus.CREATED
        );
    }
    public StartTestResponse startTest(Long testId, Integer userId) {

        Test test = testRepository.findById(testId)
                .orElseThrow();

        Optional<TestAttempt> existing =
                testAttemptRepository.findTopByTestIdAndUserIdOrderByStartTimeDesc(testId, userId);

        // if active attempt exists
        if (existing.isPresent() && existing.get().getEndTime() == null) {
            TestAttempt attempt = existing.get();

            return new StartTestResponse(
                    attempt.getId(),
                    testId,
                    test.getDurationMinutes(),
                    "Attempt already started",
                    attempt.getStartTime()
            );
        }

        // create new attempt
        TestAttempt attempt = new TestAttempt();
        attempt.setTestId(testId);
        attempt.setUserId(userId);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setScore(0);
        attempt.setTotalQuestions(0);
        attempt.setPassed(false);

        TestAttempt saved = testAttemptRepository.save(attempt);

        return new StartTestResponse(
                saved.getId(),
                testId,
                test.getDurationMinutes(),
                "Test Started",
                saved.getStartTime()
        );
    }

    public ResultResponse evaluate(Long attemptId, List<AnswerRequest> answers) {
        log.info("Evaluating test attempt with id : {}", attemptId);
        int score = 0;

        for (AnswerRequest ans : answers) {

            MCQOption option = optionRepository.findById(ans.getSelectedOptionId())
                    .orElseThrow();

            boolean correct = option.getIsCorrect();

            if (correct) score++;

            testAnswerRepository.save(new TestAnswer(
                    null,
                    attemptId,
                    ans.getQuestionId(),
                    ans.getSelectedOptionId(),
                    correct
            ));
        }

        TestAttempt attempt = testAttemptRepository.findById(attemptId).orElseThrow();

        attempt.setEndTime(LocalDateTime.now());
        attempt.setScore(score);

        Test test = testRepository.findById(attempt.getTestId()).orElseThrow();

        List<TestQuestion> mappings =
                testQuestionRepository.findByTestId(test.getId());

        int totalQuestions = mappings.size();

        boolean passed = score >= (totalQuestions / 2);
        int percentage = 0;
        if (totalQuestions > 0) {
            percentage = (score * 100) / totalQuestions;
        }

        attempt.setPassed(passed);

        attempt.setTotalQuestions(totalQuestions);

        LocalDateTime end =
                LocalDateTime.now();

        attempt.setEndTime(end);

        TestAssignment assignment =
                assignmentRepository.findTopByTestIdAndUserIdOrderByAssignedAtDesc(attempt.getTestId(), attempt.getUserId())
                        .orElseThrow();

        assignment.setStatus(
                TestStatus.COMPLETED
        );

        assignmentRepository.save(
                assignment
        );
        log.info("Updating assignment status to COMPLETED for testId: {}, userId: {}", attempt.getTestId(), attempt.getUserId());
        List<TeamMember> members =
                teamMemberRepository.findByUserId(attempt.getUserId());

        if (!members.isEmpty()) {
            TeamMember member = members.get(0);

            log.info("Updating team member status to COMPLETED for userId: {}", attempt.getUserId());
            member.setStatus(TeamStatus.COMPLETED);
            teamMemberRepository.save(member);
        }
        long durationUsed =
                Duration
                        .between(
                                attempt.getStartTime(),
                                end
                        )
                        .toMinutes();

        boolean timeExceeded =
                durationUsed
                        >
                        test.getDurationMinutes();

        testAttemptRepository.save(attempt);
        log.info("Evaluation completed for attemptId: {}, score: {}, totalQuestions: {}, passed: {}, percentage: {}, durationUsed: {}, timeExceeded: {}",
                attemptId, score, totalQuestions, passed, percentage, durationUsed, timeExceeded);
        return new ResultResponse(
                score,
                totalQuestions,
                passed,
                percentage,
                attempt.getStartTime(),
                end,
                durationUsed,
                timeExceeded
        );
    }

    public MessageOutDto sendInvites(Long testId, Long teamId) {
        log.info("Sending invites for testId: {} to teamId: {}", testId, teamId);
        List<TeamMember> members = teamMemberRepository.findByTeamId(teamId);
        if (
                members.isEmpty()
        ) {

            throw new InvalidRequestException(ErrorMessage.valueOf("No members found"), teamId);

        }
        Test test =
                testRepository
                        .findById(testId)
                        .orElseThrow();

        if (
                !test.isPublished()
        ) {
            throw new InvalidRequestException(ErrorMessage.valueOf("Publish test first"), testId);
        }

        for (TeamMember m : members) {
            log.info("Inviting userId: {} for testId: {}", m.getUserId(), testId);
            try {
                User user = userRepository.findById(m.getUserId()).orElseThrow();

                String token = tokenService.generateToken(user.getUserId(), testId);

                log.info("Generated token for userId: {}, testId: {}", user.getUserId(), testId);
                String inviteLink =
                        "http://localhost:3000/tests/" + testId + "/attempt?token=" + token;
                log.info("Generated invite link: {}", inviteLink);
                emailService.sendTestInvite(user.getEmail(), inviteLink);

                Optional<TestAssignment> existing =
                        assignmentRepository.findTopByTestIdAndUserIdOrderByAssignedAtDesc(testId, user.getUserId());

                if (existing.isEmpty()) {
                    assignmentRepository.save(new TestAssignment(
                            null,
                            testId,
                            user.getUserId(),
                            TestStatus.ASSIGNED,
                            LocalDateTime.now()
                    ));
                }
                m.setStatus(TeamStatus.ASSIGNED);
                teamMemberRepository.save(m);
            } catch (Exception e) {
                log.error("Failed to invite userId: {}", m.getUserId(), e);
            }
        }

        return new MessageOutDto("Invites sent successfully");
    }

    public ResponseEntity<?> validateTokenAndGetAttempt(
            Long testId,
            String token
    ) {
        log.info("Validating token for testId: {}", testId);
        Claims claims =
                tokenService.parseToken(token);

        Long tokenTestId =
                claims.get(
                        "testId",
                        Long.class
                );

        Integer userId =
                Integer.parseInt(
                        claims.getSubject()
                );

        if (!tokenTestId.equals(testId)) {
            throw new RuntimeException(
                    "Invalid token"
            );
        }

        TestAssignment assignment =
                assignmentRepository.findTopByTestIdAndUserIdOrderByAssignedAtDesc(testId, userId)
                        .orElseThrow();

        if (
                assignment.getStatus()
                        == TestStatus.COMPLETED
        ) {
            log.info("Test already completed for testId: {}, userId: {}", testId, userId);
            throw new InvalidRequestException(ErrorMessage.valueOf("Test already completed"), testId);
        }

        Test test =
                testRepository
                        .findById(testId)
                        .orElseThrow();

        // CHANGE 4 → auto expire if time exceeded
        if (
                assignment.getStatus()
                        == TestStatus.STARTED
                        &&
                        assignment.getAssignedAt() != null
        ) {
            log.info("Checking if test time expired for testId: {}, userId: {}", testId, userId);
            LocalDateTime expiry =
                    assignment
                            .getAssignedAt()
                            .plusMinutes(
                                    test.getDurationMinutes()
                            );

            if (
                    LocalDateTime.now()
                            .isAfter(expiry)
            ) {
                log.info("Test time expired for testId: {}, userId: {}", testId, userId);
                assignment.setStatus(
                        TestStatus.COMPLETED
                );

                assignmentRepository.save(
                        assignment
                );

                throw new InvalidRequestException(ErrorMessage.valueOf("Test time expired"), testId);
            }
        }

        if (
                assignment.getStatus()
                        == TestStatus.ASSIGNED
        ) {
            log.info("Updating assignment status to STARTED for testId: {}, userId: {}", testId, userId);
            assignment.setStatus(
                    TestStatus.STARTED
            );

            // using assignedAt as start time
            assignment.setAssignedAt(
                    LocalDateTime.now()
            );

            assignmentRepository.save(
                    assignment
            );
            List<TeamMember> members = teamMemberRepository.findByUserId(userId);

            if (!members.isEmpty()) {
                TeamMember member = members.get(0);

                member.setStatus(TeamStatus.STARTED);
                teamMemberRepository.save(member);
            }
        }

        StartTestResponse response =
                startTest(
                        testId,
                        userId
                );
        log.info("Returning response for testId: {}, userId: {}", testId, userId);
        return ResponseEntity.ok(
                response
        );
    }

    public List<CandidateTestResponse> getAssignedTestsForUser(
            Integer userId
    ){


        List<TestAssignment> assignments =
                assignmentRepository.findByUserId(userId);



        return assignments.stream()
                .map(assignment -> {


                    Test test =
                            testRepository.findById(
                                            assignment.getTestId()
                                    )
                                    .orElseThrow();



                    Optional<TestAttempt> attempt =
                            testAttemptRepository
                                    .findTopByTestIdAndUserIdOrderByStartTimeDesc(
                                            assignment.getTestId(),
                                            userId
                                    );



                    Integer score = null;

                    Integer totalQuestions = null;

                    boolean passed = false;



                    if(attempt.isPresent()){

                        score =
                                attempt.get().getScore();


                        totalQuestions =
                                attempt.get().getTotalQuestions();


                        passed =
                                attempt.get().isPassed();

                    }



                    return new CandidateTestResponse(

                            test.getId(),

                            test.getTitle(),

                            test.getTag(),

                            test.getDurationMinutes(),

                            assignment.getStatus(),

                            score,

                            totalQuestions,

                            passed

                    );


                })
                .toList();

    }

    public StartTestResponse startWithToken(
            Long testId,
            String token
    ){
        log.info("Starting test with id : {} using token", testId);
        Claims claims =
                tokenService.parseToken(token);


        Integer userId =
                Integer.parseInt(
                        claims.getSubject()
                );

        log.info("Parsed userId : {} from token", userId);
        return startTest(
                testId,
                userId
        );
    }

    public List<TestReport> getReportsByTest(Long testId) {

        List<TestAttempt> attempts =
                testAttemptRepository.findByTestId(testId);

        return attempts.stream().map(attempt -> {

            User user = userRepository.findById(attempt.getUserId())
                    .orElseThrow();

            return new TestReport(
                    attempt.getId(),
                    attempt.getTestId(),
                    attempt.getUserId(),
                    user.getFullName(),
                    user.getEmail(),
                    attempt.getScore()==null ? 0 : attempt.getScore(),
                    attempt.getTotalQuestions()==null ? 0 : attempt.getTotalQuestions(),
                    attempt.isPassed(),
                    attempt.getStartTime(),
                    attempt.getEndTime(),
                    TestStatus.COMPLETED
            );

        }).toList();
    }

    public List<TestReport> getReportsByUser(Integer userId){


        List<TestAttempt> attempts =
                testAttemptRepository.findByUserId(userId);


        return attempts.stream()
                .map(attempt->{


                    User user =
                            userRepository.findById(attempt.getUserId())
                                    .orElseThrow();



                    return new TestReport(

                            attempt.getId(),
                            attempt.getTestId(),
                            attempt.getUserId(),

                            user.getFullName(),
                            user.getEmail(),

                            attempt.getScore()==null?0:attempt.getScore(),

                            attempt.getTotalQuestions()==null?0:
                                    attempt.getTotalQuestions(),

                            attempt.isPassed(),

                            attempt.getStartTime(),
                            attempt.getEndTime(),
                            TestStatus.COMPLETED

                    );


                })
                .toList();


    }
}

