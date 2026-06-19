import { useEffect, useState } from "react";
import { getMyProfile } from "../../../api/candidateApi";

import "./candidate.css";

export default function CandidateHome() {

  const [profile, setProfile] = useState(null);


  useEffect(() => {
    load();
  }, []);


  const load = async () => {

    const response = await getMyProfile();

    setProfile(response.data);

  };


  if (!profile)
    return <p>Loading...</p>;


  return (

    <div className="candidate-dashboard-container">

      <div className="candidate-welcome-card">

        <h2>
          Welcome, {profile.fullName}
        </h2>


        <p>
          You can view your assigned tests and assessment results from the menu.
        </p>

      </div>


    </div>

  );
}