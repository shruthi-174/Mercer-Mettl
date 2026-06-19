import { useEffect, useState } from "react";
import { getTestQuestions } from "../../api/testApi";
import Modal from "../../components/Modal";
import './viewTest.css'

export default function ViewTestQuestions({ testId, close }) {

  const [questions, setQuestions] = useState([]);

  const load = async () => {

    try {

      const res = await getTestQuestions(testId);

      setQuestions(res.data);

    } catch (err) {

      alert(err.response?.data?.message);

    }

  };

  useEffect(() => {
    load();
  }, []);

  return (

    <Modal close={close}>

      <h3>Test Questions</h3>

      {questions.length === 0 && (
        <p>No questions in this test.</p>
      )}

      {questions.map(q => (

        <div key={q.questionId} className="question-card">

          <h4>{q.questionText}</h4>

          <div className="difficulty">
            Difficulty: {q.difficulty}
          </div>

          {q.options.map(o => (

            <div key={o.id} className="option">

              • {o.optionText}

            </div>

          ))}

        </div>

      ))}

    </Modal>

  );

}