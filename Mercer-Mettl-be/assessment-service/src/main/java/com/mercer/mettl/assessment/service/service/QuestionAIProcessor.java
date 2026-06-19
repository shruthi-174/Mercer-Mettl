package com.mercer.mettl.assessment.service.service;

import com.mercer.mettl.assessment.service.dto.OllamaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionAIProcessor {

    private final WebClient webClient;

    public String generateMCQs(String skill, int experience, int count) {

        String prompt = """
        You are a JSON generator.

        Generate exactly %d MCQ interview questions.

        Skill: %s
        Experience: %d years

        STRICT RULES:
        - Valid JSON only
        - Double quotes only
        - true/false lowercase
        - Exactly 4 options
        - Exactly one correct
        - No explanation
        - No markdown

        JSON FORMAT:
        {
          "questions": [
            {
              "questionText": "",
              "options": [
                { "text": "", "correct": true },
                { "text": "", "correct": false },
                { "text": "", "correct": false },
                { "text": "", "correct": false }
              ],
              "tags": []
            }
          ]
        }
        """.formatted(count, skill, experience);

        Map<String, Object> body = Map.of(
                "model", "phi",
                "prompt", prompt,
                "stream", false,
                "options", Map.of("temperature", 0)
        );

        return webClient.post()
                .uri("http://localhost:11434/api/generate")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(OllamaResponse.class)
                .map(OllamaResponse::getResponse)
                .block();
    }
}
