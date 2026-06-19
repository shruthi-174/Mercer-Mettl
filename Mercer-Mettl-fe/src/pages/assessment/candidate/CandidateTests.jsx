import { useEffect, useState } from "react";
import { getMyTests } from "../../../api/testApi";
import AttemptTest from "./AttemptTest";
import "./candidate.css";

export default function CandidateTests() {
  const [tests, setTests] = useState([]);
  const [selected, setSelected] = useState(null);

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const res = await getMyTests();
      setTests(res.data || []);
    } catch (error) {
      console.log("Failed to load tests", error);
    }
  };

  if (selected) {
    return <AttemptTest test={selected} close={() => setSelected(null)} />;
  }

  return (
    <div className="candidate-tests-container">
      <h2 className="candidate-section-title">My Assigned Tests</h2>

      {tests.length === 0 ? (
        <p>No tests assigned</p>
      ) : (
        <table className="candidate-tests-table">
          <thead>
            <tr>
              <th>Test</th>
              <th>Duration</th>
              <th>Status</th>
              <th>Result</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {tests.map((t) => (
              <tr key={t.testId}>
                <td>{t.title}</td>
                <td>{t.durationMinutes} min</td>

                <td>
                  <span className={`status status-${t.status}`}>
                    {t.status}
                  </span>
                </td>

                <td>
                  {t.status === "COMPLETED" ? (
                    <>
                      {t.score ?? 0}/{t.totalQuestions ?? 0}{" "}
                      {t.passed ? "✔" : "✖"}
                    </>
                  ) : (
                    "-"
                  )}
                </td>

                <td>
                  {t.status === "ASSIGNED" && (
                    <button
                      className="btn-primary"
                      onClick={() => setSelected(t)}
                    >
                      Start
                    </button>
                  )}

                  {t.status === "STARTED" && (
                    <button
                      className="btn-primary"
                      onClick={() => setSelected(t)}
                    >
                      Resume
                    </button>
                  )}

                  {t.status === "COMPLETED" && (
                    <button className="btn-disabled" disabled>
                      Completed
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}