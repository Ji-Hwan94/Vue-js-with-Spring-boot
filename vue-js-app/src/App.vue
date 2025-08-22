<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <nav class="bg-white shadow-md">
      <div class="container-main">
        <div class="flex justify-between items-center py-4">
          <router-link to="/" class="text-xl font-bold text-gray-800">
            게시판 앱
          </router-link>
          <div class="space-x-4">
            <router-link
              :to="getTargetRoute()"
              class="text-gray-600 hover:text-gray-800"
            >
              게시판
            </router-link>

            <template v-if="!isAuthenticated">
              <router-link
                to="/login"
                class="text-gray-600 hover:text-gray-800"
              >
                로그인
              </router-link>
            </template>
            <template v-else>
              <a
                class="text-gray-600 hover:text-gray-800 cursor-pointer"
                @click="logout"
              >
                로그아웃
              </a>
            </template>

            <router-link
              to="/register"
              class="text-gray-600 hover:text-gray-800"
            >
              회원가입
            </router-link>
          </div>
        </div>
      </div>
    </nav>
    <main>
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia";
import { useAuthStore } from "./stores/authStore";
import { useRouter } from "vue-router";
import { onMounted, onUnmounted } from "vue";

const authStore = useAuthStore();
const router = useRouter();

// ========== HttpOnly 쿠키 방식으로 변경됨 ==========
// 이전 방식 (주석 처리)
// const { accessToken } = storeToRefs(authStore);

// 새로운 방식: isAuthenticated로 인증 상태 확인
const { isAuthenticated } = storeToRefs(authStore);

const logout = async () => {
  // httpOnly 쿠키 삭제를 위해 백엔드 API 호출
  await authStore.logout();
  router.push("/");
};

// 새로 추가: axios interceptor에서 발생시킨 전역 로그아웃 이벤트 처리
const handleGlobalLogout = () => {
  authStore.clearAuth();
  router.push("/login");
};

const getTargetRoute = () => {
  // 이전: !accessToken -> 새로운: !isAuthenticated.value
  if (!isAuthenticated.value) {
    alert("로그인을 먼저 하세요.");
    return { name: "Login" };
  } else {
    return { name: "BoardList" };
  }
};

onMounted(async () => {
  // 새로 추가: 앱 시작시 httpOnly 쿠키를 이용한 인증 상태 확인
  // localStorage 방식에서는 불필요했지만, httpOnly 쿠키는 API 호출로만 확인 가능
  await authStore.checkAuthStatus();
  
  // 새로 추가: axiosInstance에서 발생시키는 전역 로그아웃 이벤트 리스너 등록
  // 401 응답시 순환참조 방지를 위해 커스텀 이벤트 사용
  window.addEventListener('auth-logout', handleGlobalLogout);
});

onUnmounted(() => {
  // 이벤트 리스너 정리
  window.removeEventListener('auth-logout', handleGlobalLogout);
});
</script>

<style scoped>
/* App 컴포넌트 스타일 */
</style>
