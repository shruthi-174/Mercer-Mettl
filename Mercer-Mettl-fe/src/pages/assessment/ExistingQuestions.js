import { useEffect, useState } from "react";
import { getQuestionsByTag } from "../../api/questionApi";
import { addQuestionsToTest } from "../../api/testApi";
import Modal from "../../components/Modal";

export default function ExistingQuestions({ testId, tag, close, onQuestionsAdded }) {

  const [questions, setQuestions] = useState([]);
  const [selected, setSelected] = useState([]);

  useEffect(() => {
    fetchQuestions();
  }, []);

  const fetchQuestions = async () => {

    const res = await getQuestionsByTag(tag);

    setQuestions(res.data);

  };

  const toggle = (id) => {

    if (selected.includes(id)) {

      setSelected(selected.filter(x => x !== id));

    } else {

      setSelected([...selected, id]);

    }

  };

  const add = async () => {

    try {

      await addQuestionsToTest(testId, selected);

      alert("Questions added");

      if (onQuestionsAdded) {
        onQuestionsAdded();
      }

      close();

    } catch (err) {

      alert(err.response?.data?.message);

    }

  };

  return (

    <Modal close={close}>

      <h3>Select Questions</h3>

      {questions.map(q => (

        <div key={q.id} style={{ marginBottom: "10px" }}>

          <input
            type="checkbox"
            onChange={() => toggle(q.id)}
          />

          {q.questionText}

        </div>

      ))}

      <button onClick={add}>
        Add Selected Questions
      </button>

    </Modal>

  );

}