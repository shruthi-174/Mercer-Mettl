import { createContext, useState } from 'react';
import api from '../api/axios';
import { storage } from '../utils/storage';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setAuth] = useState(!!storage.getAccess());

  const loginUser = async (email, password) => {
    const res = await api.post('/auth/login', { email, password });
    storage.setTokens(res.data.accessToken, res.data.refreshToken);
    setAuth(true);
  };

  const logout = () => {
    storage.clear();
    setAuth(false);
    window.location.href = '/login';
  };

  return (
    <AuthContext.Provider value={{ loginUser, logout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};
