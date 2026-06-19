import { useParams, useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";

import {
  getQuestions,
  startTestWithToken,
  submitAttempt
} from "../../../api/candidateApi";

import "./candidate.css";

export default function PublicAttemptTest() {
  const { testId } = useParams();
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

  const [questions, setQuestions] = useState([]);
  const [attemptId, setAttemptId] = useState(null);
  const [answers, setAnswers] = useState([]);
  const [result, setResult] = useState(null);

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    const response = await startTestWithToken(testId, token);
    setAttemptId(response.data.attemptId);

    const q = await getQuestions(testId);
    setQuestions(q.data);
  };

  const choose = (questionId, optionId) => {
    setAnswers((prev) => [
      ...prev.filter((x) => x.questionId !== questionId),
      { questionId, selectedOptionId: optionId }
    ]);
  };

const submit = async () => {
  try {
    const res = await submitAttempt(attemptId, answers);
    setResult(res.data);
  } catch (err) {
    console.log(err);
  }
};

  if (result) {
    const isPassed = result.passed;

    return (
      <div className="result-container">
        <div className={isPassed ? "result-card pass" : "result-card fail"}>
          <h2>
            {isPassed ? "🎉 Congratulations! You Passed" : "😔 Better luck next time"}
          </h2>

          <p className="score">
            Score: {result.score} / {result.totalQuestions}
          </p>

          <p className="percentage">
            Percentage: {result.percentage}%
          </p>

          <p className="message">
            {isPassed
              ? "You have successfully cleared the assessment."
              : "Keep practicing and try again."}
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="attempt-container">
      <h2>Assessment</h2>

      {questions.map((q) => (
        <div key={q.questionId} className="question-card">
          <h4>{q.questionText}</h4>

          {q.options.map((o) => (
            <label key={o.id} className="option">
              <input
                type="radio"
                name={q.questionId}
                onChange={() => choose(q.questionId, o.id)}
              />
              {o.optionText}
            </label>
          ))}
        </div>
      ))}

      <button className="submit-btn" onClick={submit}>
        Submit Test
      </button>
    </div>
  );
}