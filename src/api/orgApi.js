import axios from './axios';

export const createOrg = (data) => axios.post('/org/create', data);
export const getAllOrgs = () => axios.get('/org/all');
export const updateOrg = (id, data) => axios.put(`/org/update/${id}`, data);
