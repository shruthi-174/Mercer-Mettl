import axios from './axios';

export const createOrg = (data) => axios.post('/org/create', data);
export const getAllOrgs = () => axios.get('/org/all');
export const updateOrg = (id, data) => axios.put(`/org/update/${id}`, data);
export const getOrganizationDetails = (orgId) =>
  axios.get(`/org/${orgId}`);

export const getOrganizationUsers = (orgId) =>
  axios.get(`/org/${orgId}/users`);