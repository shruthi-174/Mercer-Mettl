package com.mercer.mettl.assessment.service.controller;

import com.mercer.mettl.assessment.service.dto.*;
import com.mercer.mettl.assessment.service.entity.TestReport;
import com.mercer.mettl.assessment.service.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
@Slf4j
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping
    public TestResponse createTest(final @RequestBody CreateTestRequest request) {

        return testService.createTest(request);
    }

    @GetMapping
    public List<TestResponse> getAllTests() {

        return testService.getAllTests();
    }

    @GetMapping("/{id}")
    public TestResponse getTest(@PathVariable Long id) {

        return testService.getTest(id);
    }

    @DeleteMapping("/{id}")
    public MessageOutDto deleteTest(@PathVariable Long id) {

        return testService.deleteTest(id);
    }

    @PostMapping("/{testId}/questions")
    public MessageOutDto addQuestions(
            @PathVariable Long testId,
            @RequestBody AddQuestionsRequest request) {

        return testService.addQuestions(testId, request);
    }

    @DeleteMapping("/{testId}/questions/{questionId}")
    public MessageOutDto removeQuestion(
            @PathVariable Long testId,
            @PathVariable Long questionId) {

        return testService.removeQuestion(testId, questionId);
    }

    @PostMapping("/{testId}/publish")
    public MessageOutDto publishTest(@PathVariable Long testId) {

        return testService.publishTest(testId);
    }

    @GetMapping("/{testId}/questions")
    public List<TestQuestionResponse> getQuestionsOfTest(
            @PathVariable Long testId) {

        return testService.getQuestionsOfTest(testId);
    }

//    @PostMapping("/{testId}/start")
//    public StartTestResponse startTest(
//            @PathVariable Long testId,
//            @RequestParam Integer userId) {
//
//        return testService.startTest(testId, userId);
//    }

    @PostMapping("/{testId}/start-token")
    public StartTestResponse startWithToken(
            @PathVariable Long testId,
            @RequestParam String token
    ) {
        return testService.startWithToken(testId, token);
    }

    @PostMapping("/attempts/{attemptId}/submit")
    public ResultResponse submit(@PathVariable Long attemptId,
                                 @RequestBody List<AnswerRequest> answers) {

        return testService.evaluate(attemptId, answers);
    }

    @PostMapping("/{testId}/invite")
    public MessageOutDto inviteCandidates(
            @PathVariable Long testId,
            @RequestParam Long teamId) {

        return testService.sendInvites(testId, teamId);
    }

    @GetMapping("/{testId}/attempt")
    public ResponseEntity<?> attempt(
            @PathVariable Long testId,
            @RequestParam String token
    ) {
        log.info("Attempt API hit testId={} token={}", testId, token);
        return testService.validateTokenAndGetAttempt(testId, token);
    }

    @GetMapping("/my-tests")
    public List<CandidateTestResponse> myTests(
            Authentication authentication
    )
    {


        JwtAuthenticationToken jwt =
                (JwtAuthenticationToken) authentication;


        Integer userId =
                Integer.parseInt(
                        jwt.getToken().getSubject()
                );


        return testService.getAssignedTestsForUser(userId);

    }

    @GetMapping("/reports/test/{testId}")
    public List<TestReport> getTestReports(@PathVariable Long testId) {
        return testService.getReportsByTest(testId);
    }

    @GetMapping("/my-results")
    public List<TestReport> myResults(
            Authentication authentication
    ){


        JwtAuthenticationToken jwt =
                (JwtAuthenticationToken) authentication;


        Integer userId =
                Integer.parseInt(
                        jwt.getToken().getSubject()
                );


        return testService.getReportsByUser(userId);

    }
}