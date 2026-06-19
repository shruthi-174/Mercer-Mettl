import api from './axios';


// Create a new team
export const createTeam = (data) =>
  api.post('/teams', data);


// Add user to a team
// teamId -> path variable
// userId -> request param
export const addMemberToTeam = (teamId, userId) =>
  api.post(`/teams/${teamId}/members?userId=${userId}`);


// Get only team member mapping details
// Returns TeamMember list (id, teamId, userId)
export const getTeamMembers = (teamId) =>
  api.get(`/teams/${teamId}/members`);


// Get all teams belonging to an organization
export const getTeamsByOrg = (orgId) =>
  api.get(`/teams/org/${orgId}`);


// Get detailed member information of a team
// Returns TeamMemberResponse
// Example: user name, email, role etc.
export const getTeamMembersDetails = (teamId) =>
  api.get(`/teams/${teamId}/members/details`);

export const getUserTeams=(userId)=>
api.get(`/teams/user/${userId}`);