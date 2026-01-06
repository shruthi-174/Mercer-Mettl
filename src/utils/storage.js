export const storage = {
  setTokens: (access, refresh) => {
    localStorage.setItem('accessToken', access);
    localStorage.setItem('refreshToken', refresh);
  },
  clear: () => {
    localStorage.clear();
  },
  getAccess: () => localStorage.getItem('accessToken'),
  getRefresh: () => localStorage.getItem('refreshToken')
};
