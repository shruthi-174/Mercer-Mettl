import axios from './axios';

export const registerUser = (data) => axios.post('/user/register', data);
export const getMyProfile = () => axios.get('/user/me');
export const getOrgUsers = () => axios.get('/user/org');
export const deleteUser = (id) => axios.delete(`/user/${id}`);
export const changeRole = (id, data) => axios.put(`/user/${id}/role`, data);
