import axios from './axios';

export const createOrgAdmin = (data) =>
  axios.post('/user/create-org-admin', data);

export const registerUser = (data) =>
  axios.post('/user/register', data);


export const getMyProfile = () => axios.get('/user/me');
export const getOrgUsers = (orgId) =>
  axios.get(`/org/${orgId}/users`);
export const changeUserRole = (id, data) =>
  axios.put(`/user/${id}/role`, data);

export const deleteUser = (userId) =>
  axios.delete(`/user/${userId}`);
export const getOrganizationDetails = (orgId) =>
  axios.get(`/org/${orgId}`);

export const getOrganizationUsers = (orgId) =>
  axios.get(`/org/${orgId}/users`);