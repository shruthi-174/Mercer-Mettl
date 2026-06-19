package com.mercer.mettl.assessment.service.helper;

import com.mercer.mettl.assessment.service.constants.ErrorMessage;
import com.mercer.mettl.assessment.service.dto.OptionRequest;
import com.mercer.mettl.assessment.service.dto.AIQuestionRequest;
import com.mercer.mettl.assessment.service.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceHelper {

    public String sanitize(String raw) {
        return raw
                .replaceAll("'", "\"")
                .replaceAll("\\bTrue\\b", "true")
                .replaceAll("\\bFalse\\b", "false");
    }

    public void validate(AIQuestionRequest q) {
    log.info("Validating question: {}", q.getQuestionText());

        //        MCQ must have 4 options.
        if (q.getOptions().size() != 4)
            throw new InvalidRequestException(ErrorMessage.INVALID_OPTION_COUNT);

        //exactly 1 correct ans allowed
        if (q.getOptions().stream().filter(OptionRequest::isCorrect).count() != 1)
            throw new InvalidRequestException(ErrorMessage.INVALID_CORRECT_OPTION_COUNT);
    }

    public int mapDifficulty(int exp) {
        log.info("Mapping difficulty for exp: {}", exp);
        if (exp <= 1) return 1;
        if (exp <= 3) return 2;
        if (exp <= 5) return 3;
        return 4;
    }
}
