import { useEffect, useState } from 'react';
import { getAllTests, publishTest } from '../../api/testApi';
import ViewTestQuestions from '../assessment/ViewQuestions';

export default function Tests() {

  const [tests, setTests] = useState([]);
  const [viewTestId, setViewTestId] = useState(null);

  const load = async () => {

    try {

      const res = await getAllTests();

      setTests(res.data);

    } catch (err) {

      alert(err.response?.data?.message);

    }

  };

  const publish = async (id) => {

    try {

      await publishTest(id);

      alert("Test Published Successfully");

      load();

    } catch (err) {

      alert(err.response?.data?.message);

    }

  };

  useEffect(() => {
    load();
  }, []);

  return (

    <div className="tests-page">

      <h2>Test Details</h2>

      <table className="tests-table">

        <thead>
          <tr>
            <th>Test Name</th>
            <th>Duration</th>
            <th>Status</th>
            <th>View</th>
            <th>Publish</th>
          </tr>
        </thead>

        <tbody>

          {tests.map(t => (

            <tr key={t.testId}>

              <td>{t.title}</td>

              <td>{t.durationMinutes} mins</td>

              <td>
                {t.published ? "Published" : "Draft"}
              </td>

              <td>

                <button
                  className="view-btn"
                  onClick={() => setViewTestId(t.testId)}
                >
                  View Questions
                </button>

              </td>

              <td>

                {!t.published && (

                  <button
                    className="publish-btn"
                    onClick={() => publish(t.testId)}
                  >
                    Publish
                  </button>

                )}

              </td>

            </tr>

          ))}

        </tbody>

      </table>

      {viewTestId && (

        <ViewTestQuestions
          testId={viewTestId}
          close={() => setViewTestId(null)}
        />

      )}

    </div>

  );

}