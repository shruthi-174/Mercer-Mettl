import { useState } from "react";
import { createQuestion } from "../../api/questionApi";
import Modal from "../../components/Modal";

export default function ManualQuestion({ tag, close }) {

  const [questionText, setQuestionText] = useState("");

  const [options, setOptions] = useState([
    { text: "", correct: false },
    { text: "", correct: false },
    { text: "", correct: false },
    { text: "", correct: false }
  ]);

  const handleOptionChange = (index, value) => {

    const newOptions = [...options];

    newOptions[index].text = value;

    setOptions(newOptions);

  };

  const selectCorrect = (index) => {

    const newOptions = options.map((opt, i) => ({
      ...opt,
      correct: i === index
    }));

    setOptions(newOptions);

  };

  const submit = async () => {

    try {

      await createQuestion({
        questionText,
        difficulty: 1,
        tags: [tag],
        options
      });

      alert("Question Created");

      close();

    } catch (err) {

      alert(err.response?.data?.message);

    }

  };

  return (

    <Modal close={close}>

      <h3>Create Question</h3>

      <input
        placeholder="Question text"
        value={questionText}
        onChange={(e) => setQuestionText(e.target.value)}
        style={{ width: "100%", marginBottom: "15px" }}
      />

      <h4>Options</h4>

      {options.map((op, index) => (

        <div key={index} style={{ marginBottom: "10px" }}>

          <input
            placeholder={`Option ${index + 1}`}
            value={op.text}
            onChange={(e) => handleOptionChange(index, e.target.value)}
            style={{ marginRight: "10px" }}
          />

          <input
            type="radio"
            name="correctOption"
            onChange={() => selectCorrect(index)}
          />

          Correct

        </div>

      ))}

      <button onClick={submit}>
        Create Question
      </button>

    </Modal>

  );

}