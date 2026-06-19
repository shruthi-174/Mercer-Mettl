import { useEffect, useState } from "react";
import { getMyResults, getTest } from "../../../api/testApi";

import "./candidate.css";

export default function CandidateResults() {
  const [results, setResults] = useState([]);
const [testMap, setTestMap] = useState({});

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const res = await getMyResults();
      setResults(res.data || []);
       const uniqueTestIds = [...new Set(results.map(r => r.testId))];

  const promises = uniqueTestIds.map(id => getTest(id));
  const responses = await Promise.all(promises);

  const map = {};
  responses.forEach(r => {
    map[r.data.testId] = r.data.title;
  });

  setTestMap(map);
    } catch (error) {
      console.log("Failed to load results", error);
    }
  };

  return (
    <div className="candidate-results-container">

      <h2 className = "result-h">My Results</h2>

      {results.length === 0 ? (
        <p>No results available</p>
      ) : (
        <table className="candidate-result-table">

          <thead>
            <tr>
              <th>Test</th>
              <th>Score</th>
              <th>Total</th>
              <th>Percentage</th>
              <th>Status</th>
            </tr>
          </thead>

          <tbody>
            {results.map((r) => {

              const total = r.totalQuestions ?? 0;
              const score = r.score ?? 0;

              const percentage =
                total > 0
                  ? Math.round((score / total) * 100)
                  : 0;

              return (
                <tr key={r.attemptId}>

                 <td>{testMap[r.testId] || "Loading..."}</td>

                  <td>{score}</td>

                  <td>{total}</td>

                  <td>{percentage}%</td>

                  <td>
                    <span
                      className={
                        r.passed
                          ? "candidate-pass"
                          : "candidate-fail"
                      }
                    >
                      {r.passed ? "Passed" : "Failed"}
                    </span>
                  </td>

                </tr>
              );
            })}
          </tbody>

        </table>
      )}

    </div>
  );
}