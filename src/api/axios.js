import axios from 'axios';
import { storage } from '../utils/storage';

const api = axios.create({
  baseURL: 'http://localhost:8080'
});

api.interceptors.request.use(config => {
  const token = storage.getAccess();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

let isRefreshing = false;

api.interceptors.response.use(
  res => res,
  async err => {
    if (err.response?.status === 401 && !isRefreshing) {
      isRefreshing = true;
      const refreshToken = storage.getRefresh();

      const res = await axios.post(
        'http://localhost:8080/auth/refresh',
        { refreshToken }
      );

      storage.setTokens(res.data.accessToken, refreshToken);
      err.config.headers.Authorization = `Bearer ${res.data.accessToken}`;
      isRefreshing = false;
      return api(err.config);
    }
    return Promise.reject(err);
  }
);

export default api;
