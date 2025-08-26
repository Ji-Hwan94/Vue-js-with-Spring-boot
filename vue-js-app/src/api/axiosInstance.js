// ========== HttpOnly 쿠키 방식으로 변경됨 ==========
// 이전 localStorage 방식에서는 useAuthStore import가 필요했음 (주석 처리)
// import { useAuthStore } from "@/stores/authStore";
import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "/api",
  withCredentials: true, // httpOnly 쿠키를 자동으로 전송하기 위해 필요
});

// 이전 방식: request interceptor에서 Authorization 헤더 추가 (주석 처리)
// axiosInstance.interceptors.request.use((config) => {
//   const authStore = useAuthStore();
//   if (authStore.accessToken) {
//     config.headers["Authorization"] = `Bearer ${authStore.accessToken}`;
//   }
//   return config;
// });

// HttpOnly 쿠키 방식에서는 request interceptor 불필요
// 브라우저가 자동으로 쿠키를 포함해서 요청을 보냄
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // refresh 요청 실패 시 바로 로그아웃
    if (
      originalRequest.url?.includes("/users/refresh") &&
      (error.response?.status === 401 || error.response?.status === 403)
    ) {
      window.dispatchEvent(new CustomEvent("auth-logout"));
      return Promise.reject(error);
    }

    // 401/403 에러 시 토큰 갱신 시도
    if (error.response?.status === 401 || error.response?.status === 403) {
      try {
        await axiosInstance.post("/users/refresh");
        return axiosInstance(originalRequest);
      } catch (error) {
        throw error;
      }
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
