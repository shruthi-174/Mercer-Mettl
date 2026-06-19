import { useEffect, useState } from "react";

import { getAllTests } from "../../api/testApi";
import api from "../../api/axios";

import "./report.css";


export default function Reports() {

    const [tests, setTests] = useState([]);

    const [selectedTest, setSelectedTest] = useState("");

    const [reports, setReports] = useState([]);


    useEffect(() => {
        loadTests();
    }, []);


    const loadTests = async () => {

        const res = await getAllTests();

        setTests(res.data);

    };


    const loadReports = async () => {

        if(!selectedTest){
            alert("Please select test");
            return;
        }


        const res =
            await api.get(
                `/tests/reports/test/${selectedTest}`
            );


        setReports(res.data);

    };


    return (

        <div className="reports-page">


            <h2 className="reports-title">
                Test Reports
            </h2>



            <div className="report-filter">


                <select
                    value={selectedTest}
                    onChange={
                        e=>setSelectedTest(e.target.value)
                    }
                >

                    <option value="">
                        Select Test
                    </option>


                    {
                        tests.map(t=>(

                            <option
                                key={t.testId}
                                value={t.testId}
                            >
                                {t.title}
                            </option>

                        ))
                    }

                </select>



                <button
                    onClick={loadReports}
                >
                    View Report
                </button>


            </div>



            {
                reports.length > 0 && (

                <div className="report-table-container">


                <table className="report-table">


                    <thead>

                    <tr>

                        <th>User</th>

                        <th>Email</th>

                        <th>Score</th>

                        <th>Total Questions</th>

                        <th>Percentage</th>

                        <th>Status</th>

                        <th>Started</th>

                        <th>Completed</th>

                    </tr>

                    </thead>



                    <tbody>


                    {
                        reports.map(r=>(

                        <tr key={r.attemptId}>


                            <td>
                                {r.userName}
                            </td>


                            <td>
                                {r.email}
                            </td>


                            <td>
                                {r.score}
                            </td>


                            <td>
                                {r.totalQuestions}
                            </td>


                            <td>
                                {
                                r.totalQuestions
                                ?
                                ((r.score/r.totalQuestions)*100).toFixed(2)
                                :
                                0
                                }%
                            </td>


                            <td>

                                <span
                                className={
                                    r.passed
                                    ?
                                    "passed"
                                    :
                                    "failed"
                                }
                                >

                                {
                                r.passed
                                ?
                                "Passed"
                                :
                                "Failed"
                                }

                                </span>

                            </td>


                            <td>
                                {r.startTime}
                            </td>


                            <td>
                                {r.endTime}
                            </td>



                        </tr>

                        ))
                    }


                    </tbody>


                </table>


                </div>

                )

            }



        </div>

    );

}