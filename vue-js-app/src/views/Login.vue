<template>
  <div class="container-main">
    <div class="max-w-md mx-auto">
      <div class="card">
        <h2 class="text-2xl font-bold text-center mb-6">로그인</h2>
        <form @submit.prevent="handleLogin">
          <div class="mb-4">
            <label class="form-label">아이디</label>
            <input
              v-model="loginForm.username"
              type="text"
              class="form-input w-full"
              required
            />
          </div>
          <div class="mb-4">
            <label class="form-label">이메일</label>
            <input
              v-model="loginForm.email"
              type="email"
              class="form-input w-full"
              required
            />
          </div>
          <div class="mb-6">
            <label class="form-label">비밀번호</label>
            <input
              v-model="loginForm.password"
              type="password"
              class="form-input w-full"
              required
            />
          </div>
          <button type="submit" class="btn-primary w-full">로그인</button>
        </form>
        <div class="text-center mt-4">
          <router-link to="/register" class="text-blue-500 hover:underline">
            회원가입
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { loginService } from "@/service/loginService";
import { useAuthStore } from "@/stores/authStore";
import { reactive } from "vue";
import { useRouter } from "vue-router";

const authStore = useAuthStore();
const router = useRouter();

const loginForm = reactive({
  username: "user4",
  email: "user4@google.com",
  password: "123456",
});

const handleLogin = async () => {
  try {
    // 백엔드에 로그인 요청 (httpOnly 쿠키가 응답으로 설정됨)
    await loginService.getLogin(loginForm);

    // ========== HttpOnly 쿠키 방식으로 변경됨 ==========
    // 이전 방식: 응답에서 토큰을 받아서 store에 저장 (주석 처리)
    // const { accessToken, refreshToken } = response.data;
    // authStore.setTokens({ accessToken, refreshToken });

    // 새로운 방식: httpOnly 쿠키가 설정되었으므로 사용자 정보를 가져옴
    // 백엔드에서 쿠키를 검증하고 사용자 정보를 반환하는 API 호출
    await authStore.checkAuthStatus();

    router.push("/boards");
  } catch (error) {
    console.error("로그인 실패:", error);
  }
};
</script>

<style scoped>
/* Login 컴포넌트 스타일 */
</style>
