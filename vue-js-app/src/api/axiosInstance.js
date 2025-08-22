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
    if (error.response?.status === 401) {
      // httpOnly 쿠키가 만료되었거나 유효하지 않음
      // authStore를 여기서 직접 import하면 순환참조 발생 가능하므로
      // 커스텀 이벤트를 발생시켜 App.vue에서 처리하도록 함
      window.dispatchEvent(new CustomEvent('auth-logout'));
      
      // 이전 방식 (주석 처리)
      // const authStore = useAuthStore();
      // authStore.logout();
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
