<template>
  <div class="container-main">
    <div class="max-w-md mx-auto">
      <div class="card">
        <h2 class="text-2xl font-bold text-center mb-6">로그인</h2>
        <form @submit.prevent="handleLogin">
          <div class="mb-4">
            <label class="form-label">아이디</label>
            <input
              v-model="loginForm.email"
              type="text"
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
import { reactive } from "vue";
import { loginService } from "../../service/loginService";

const loginForm = reactive({
  email: "",
  password: "",
});

const handleLogin = () => {
  loginService.getLogin(loginForm);
};
</script>

<style scoped>
/* Login 컴포넌트 스타일 */
</style>
