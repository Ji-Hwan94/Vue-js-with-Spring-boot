import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "",
});

axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      logout();
      // TODO: MVP2차 작업 시 여기서 refreshToken 요청하고 토큰 갱신 후 재요청
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
