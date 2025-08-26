import { defineStore } from "pinia";
import { ref, computed } from "vue";
import axiosInstance from "@/api/axiosInstance";

export const useAuthStore = defineStore("auth", () => {
  // ========== HttpOnly 쿠키 방식으로 변경됨 ==========
  // 이전 localStorage 방식 (주석 처리)
  // const accessToken = ref(localStorage.getItem('accessToken') || null)
  // const refreshToken = ref(localStorage.getItem('refreshToken') || null)
  // const isAuthenticated = computed(() => !!accessToken.value)

  // 새로운 httpOnly 쿠키 방식
  const user = ref(null);
  const isAuthenticated = ref(false); // API 호출로 확인
  const isLoading = ref(false); // 인증 상태 확인 중인지 표시

  const setUser = (userData) => {
    user.value = userData;
    isAuthenticated.value = true;
  };

  const clearAuth = () => {
    user.value = null;
    isAuthenticated.value = false;
  };

  const getTargetRoute = () => {
    return !isAuthenticated.value ? { name: "Login" } : { name: "BoardList" };
  };

  // 새로 추가: httpOnly 쿠키를 이용한 인증 상태 확인
  const checkAuthStatus = async () => {
    isLoading.value = true;
    try {
      // 백엔드에서 httpOnly 쿠키를 검증하고 사용자 정보 반환
      const response = await axiosInstance.get("/users/me");
      setUser(response.data);
      return true;
    } catch (error) {
      // 쿠키가 없거나 만료된 경우
      clearAuth();
      return false;
    } finally {
      isLoading.value = false;
    }
  };

  // 이전 방식 (localStorage에 추가)
  // const setTokens = (tokens) => {
  //   accessToken.value = tokens.accessToken
  //   refreshToken.value = tokens.refreshToken
  //   localStorage.setItem('accessToken', tokens.accessToken)
  //   if (tokens.refreshToken) {
  //     localStorage.setItem('refreshToken', tokens.refreshToken)
  //   }
  // }

  const logout = async () => {
    try {
      // 백엔드에 로그아웃 요청하여 httpOnly 쿠키 삭제
      await axiosInstance.post("/users/logout");
    } catch (error) {
      console.error("로그아웃 API 실패:", error);
    } finally {
      clearAuth();
    }
  };

  // 이전 방식 (주석 처리)
  // const clearAuth = () => {
  //   accessToken.value = null
  //   refreshToken.value = null
  //   user.value = null
  //   localStorage.removeItem('accessToken')
  //   localStorage.removeItem('refreshToken')
  // }

  return {
    user,
    isAuthenticated,
    isLoading,
    setUser,
    clearAuth,
    getTargetRoute,
    checkAuthStatus,
    logout,
    // 이전에 반환했던 것들 (주석 처리)
    // accessToken,
    // refreshToken,
    // setTokens
  };
});
