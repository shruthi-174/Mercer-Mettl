import { useState } from 'react';
import { generateQuestions } from '../../api/questionApi';
import { addQuestionsToTest } from '../../api/testApi';
import Modal from '../../components/Modal';

export default function GenerateAIQuestions({
  testId,
  tag,
  close,
  onQuestionsAdded
}) {

  const [experience, setExperience] = useState(2);
  const [count, setCount] = useState(5);
  const [questions, setQuestions] = useState([]);
  const [selected, setSelected] = useState([]);

  const generate = async () => {

    try {

      const res = await generateQuestions(tag, experience, count);

      setQuestions(res.data);

    } catch (err) {
      alert(err.response?.data?.message);
    }
  };

  const toggle = (id) => {

    if (selected.includes(id)) {
      setSelected(selected.filter(q => q !== id));
    } else {
      setSelected([...selected, id]);
    }

  };

  const addToTest = async () => {

    try {

      await addQuestionsToTest(testId, selected);

      alert("AI Questions added to test");

      onQuestionsAdded();   // 🔥 refresh
      close();              // close modal

    } catch (err) {
      alert(err.response?.data?.message);
    }
  };

  return (

    <Modal close={close}>

      <h3>Generate AI Questions</h3>

      <div className="form-group">

        <label>Experience</label>
        <input
          type="number"
          value={experience}
          onChange={e => setExperience(e.target.value)}
        />

      </div>

      <div className="form-group">

        <label>Number of Questions</label>
        <input
          type="number"
          value={count}
          onChange={e => setCount(e.target.value)}
        />

      </div>

      <button onClick={generate}>
        Generate
      </button>

      <hr />

      {questions.map(q => (

        <div key={q.id} className="question-row">

          <input
            type="checkbox"
            onChange={() => toggle(q.id)}
          />

          <span>{q.questionText}</span>

        </div>

      ))}

      {questions.length > 0 && (

        <button onClick={addToTest}>
          Add Selected Questions
        </button>

      )}

    </Modal>
  );
}