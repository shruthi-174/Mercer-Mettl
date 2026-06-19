import { useState, useEffect } from "react";
import { getTeamsByOrg, createTeam } from "../../../api/teamApi";

import { getAllTests, sendInviteToTeam } from "../../../api/testApi";

import TeamSelector from "./TeamSelector";

export default function TeamManagement({ orgId }) {
  const [tests, setTests] = useState([]);

  const [selectedTest, setSelectedTest] = useState(null);

  const [selectedTeam, setSelectedTeam] = useState(null);
  const [newTeamName, setNewTeamName] = useState("");

  useEffect(() => {
    loadTests();
  }, []);

  const loadTests = async () => {
    const res = await getAllTests();

    setTests(res.data);
  };

  const createNewTeam = async () => {
    if (!newTeamName) return alert("Enter team name");

    await createTeam({ name: newTeamName, orgId });
    alert("Team created");
    setNewTeamName("");
  };

  const sendInvite = async () => {
    if (!selectedTest || !selectedTeam) {
      alert("Please select test and team");
      return;
    }

    try {
      await sendInviteToTeam(selectedTest, selectedTeam);

      alert("Invite sent successfully");
    } catch (error) {
      console.log(error);

      alert("Failed to send invite");
    }
  };

  return (
    <div className="team-management-page">
     <h2 className="team-title">TEAM MANAGEMENT</h2>
      {/* TOP SECTION */}
      <div className="top-bar">
        <div className = "select-test-list">
          {/* <h3>Select Test</h3> */}
          <select  className="team-dropdown-list" onChange={(e) => setSelectedTest(e.target.value)}>
            <option value="">Select Test</option>
            {tests.map((t) => (
              <option key={t.testId} value={t.testId}>
                {t.title}
              </option>
            ))}
          </select>
        </div>

        <div className="create-team">   
          <input
            placeholder="New Team Name"
            value={newTeamName}
            onChange={(e) => setNewTeamName(e.target.value)}
          />
          <button onClick={createNewTeam}>Create Team</button>
        </div>
      </div>

      {/* TEAM SELECTOR */}
      <TeamSelector
        orgId={orgId}
        onTeamSelect={(teamId) => setSelectedTeam(teamId)}
        onSendInvite={sendInvite}
      />
    </div>
  );
}

