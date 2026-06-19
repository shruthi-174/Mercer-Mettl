import { useEffect, useState } from "react";
import { getTestQuestions, removeQuestionFromTest } from "../../api/testApi";

export default function TestQuestions({ testId, refresh }) {

  const [questions, setQuestions] = useState([]);

  const load = async () => {

    try {

      const res = await getTestQuestions(testId);

      setQuestions(res.data);

    } catch (err) {

      alert(err.response?.data?.message);

    }

  };

  const remove = async (questionId) => {

    try {

      await removeQuestionFromTest(testId, questionId);

      load();

    } catch (err) {

      alert(err.response?.data?.message);

    }

  };

  useEffect(() => {

    load();

  }, [testId, refresh]);

  return (

    <div style={{ marginTop: "20px" }}>

      <h3>Questions In Test</h3>

      {questions.length === 0 && (
        <p>No questions added to this test yet.</p>
      )}

      {questions.map(q => (

        <div key={q.questionId} style={{
          border: "1px solid #ddd",
          padding: "15px",
          marginBottom: "10px",
          borderRadius: "6px"
        }}>

          <h4>{q.questionText}</h4>

          <p>Difficulty: {q.difficulty}</p>

          {q.options.map(o => (

            <div key={o.id} style={{ marginLeft: "10px" }}>
              • {o.optionText}
            </div>

          ))}

          <button
            onClick={() => remove(q.questionId)}
            style={{ marginTop: "10px" }}
          >
            Remove
          </button>

        </div>

      ))}

    </div>

  );

}