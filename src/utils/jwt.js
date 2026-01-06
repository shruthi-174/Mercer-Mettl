import { jwtDecode } from 'jwt-decode';

export const getUserRole = () => {
  const token = localStorage.getItem('accessToken');
  if (!token) return null;
  return jwtDecode(token).role;
};
