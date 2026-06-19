import { useEffect, useState } from "react";

import {
  getTeamsByOrg,
  getTeamMembersDetails,
  addMemberToTeam,
} from "../../../api/teamApi";

import { getOrganizationUsers } from "../../../api/orgApi";

import "../team/team.css";

export default function TeamSelector({ orgId, onTeamSelect, onSendInvite }) {
  const [teams, setTeams] = useState([]);

  const [selectedTeamId, setSelectedTeamId] = useState(null);

  const [teamMembers, setTeamMembers] = useState([]);

  const [orgUsers, setOrgUsers] = useState([]);

  useEffect(() => {
    if (orgId) {
      loadTeams();
    }
  }, [orgId]);

  const loadTeams = async () => {
    const res = await getTeamsByOrg(orgId);

    setTeams(res.data);
  };

  const selectTeam = async (e) => {
    const teamId = e.target.value;

    setSelectedTeamId(teamId);

    if (onTeamSelect) {
      onTeamSelect(teamId);
    }

    if (!teamId) {
      setTeamMembers([]);
      setOrgUsers([]);
      return;
    }

    const membersRes = await getTeamMembersDetails(teamId);

    setTeamMembers(membersRes.data);

    const usersRes = await getOrganizationUsers(orgId);

    setOrgUsers(usersRes.data);
  };

  const addMember = async (userId) => {
    await addMemberToTeam(selectedTeamId, userId);

    const res = await getTeamMembersDetails(selectedTeamId);

    setTeamMembers(res.data);
  };

  const isAlreadyMember = (userId) => {
    return teamMembers.some((m) => m.userId === userId);
  };

  return (
    <div className="team-page">
<div className="team-section">
        <select className="team-dropdown" onChange={selectTeam}>
          <option value="">Select Team</option>

          {teams.map((t) => (
            <option key={t.id} value={t.id}>
              {t.name}
            </option>
          ))}
        </select>
      </div>
      <div className="team-grid">
      {/* <h2 className="team-title">Team Management</h2> */}
      

        {/* LEFT */}
        <div className="team-box">
          <h3>Organization Users</h3>

          {orgUsers.map((u) => (
            <div key={u.userId} className="user-card">
              <div>
                <b>{u.fullName}</b>
                <p>{u.email}</p>
              </div>

              <button
                disabled={isAlreadyMember(u.userId)}
                onClick={() => addMember(u.userId)}
              >
                {isAlreadyMember(u.userId) ? "Added ✓" : "Add"}
              </button>
            </div>
          ))}
        </div>

        {/* RIGHT */}
        <div className="team-box">

          <div className="team-header">
            <h3>Team Members</h3>

            <button className="invite-btn" onClick={onSendInvite}>
              Send Invite
            </button>
          </div>

          {teamMembers.map((m) => (
            <div key={m.userId} className="member-card">
              <b>{m.name}</b>
              <p>{m.email}</p>
            </div>
          ))}
        </div>

      </div>
    </div>
  );
}



