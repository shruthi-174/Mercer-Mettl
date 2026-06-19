import axios from 'axios';
import { storage } from '../utils/storage';

const api = axios.create({
  baseURL: 'http://localhost:8080'
});

/**
 * REQUEST INTERCEPTOR
 * - Attach token ONLY for protected APIs
 * - NEVER attach token for auth endpoints
 */
api.interceptors.request.use(config => {
  const token = storage.getAccess();

  const isAuthEndpoint =
    config.url.includes('/auth/login') ||
    config.url.includes('/auth/refresh') ||
    config.url.includes('/auth/forgot-password') ||
    config.url.includes('/auth/reset-password');

  if (token && !isAuthEndpoint) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

let isRefreshing = false;

/**
 * RESPONSE INTERCEPTOR
 * - Refresh token only for protected APIs
 * - Never retry login calls
 */
api.interceptors.response.use(
  res => res,
  async err => {
    const originalRequest = err.config;

    if (
      err.response?.status === 401 &&
      !isRefreshing &&
      !originalRequest.url.includes('/auth/login')
    ) {
      isRefreshing = true;

      try {
        const refreshToken = storage.getRefresh();
        const res = await axios.post(
          'http://localhost:8080/auth/refresh',
          { refreshToken }
        );

        storage.setTokens(res.data.accessToken, refreshToken);

        originalRequest.headers.Authorization =
          `Bearer ${res.data.accessToken}`;

        isRefreshing = false;
        return api(originalRequest);
      } catch (e) {
        storage.clear();
        window.location.href = '/login';
      }
    }

    return Promise.reject(err);
  }
);

export default api;
