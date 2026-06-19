import { useState } from "react";
import { createTest } from "../../api/testApi";
import QuestionBuilder from "./QuestionBuilder";
import { toast } from "react-toastify";
import './createTest.css';

export default function CreateTest() {

  const [form, setForm] = useState({
    title: "",
    tag: "",
    durationMinutes: ""
  });

  const [testId, setTestId] = useState(null);

  const handleChange = (e) => {

    setForm({
      ...form,
      [e.target.name]: e.target.value
    });

  };

  const submit = async (e) => {

    e.preventDefault();

    try {

      const res = await createTest(form);

      setTestId(res.data.testId);

      toast.success("Test created successfully. Now add questions.");

    } catch (err) {

      toast.error(err.response?.data?.message || "Failed to create test");

    }
  };

  if (testId) {

    return (
      <QuestionBuilder
        testId={testId}
        tag={form.tag}
      />
    );
  }

  return (

    <div className="page">

      <form className="card" onSubmit={submit}>

        <h2>Create Test</h2>

        <input
          placeholder="Test Title"
          name="title"
          value={form.title}
          onChange={handleChange}
        />

        <input
          placeholder="Tag (JAVA, SPRING)"
          name="tag"
          value={form.tag}
          onChange={handleChange}
        />

        <input
          placeholder="Duration Minutes"
          name="durationMinutes"
          value={form.durationMinutes}
          onChange={handleChange}
        />

        <button>Create Test</button>

      </form>

    </div>
  );
}