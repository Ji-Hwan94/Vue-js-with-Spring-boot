<template>
  <div class="container-main">
    <div class="max-w-md mx-auto">
      <div class="card">
        <h2 class="text-2xl font-bold text-center mb-6">회원가입</h2>
        <form @submit.prevent="handleRegister">
          <div class="mb-4">
            <label class="form-label">아이디</label>
            <input
              v-model="registerForm.username"
              type="text"
              class="form-input w-full"
              required
            />
          </div>
          <div class="mb-4">
            <label class="form-label">이메일</label>
            <input
              v-model="registerForm.email"
              type="email"
              class="form-input w-full"
              required
            />
          </div>
          <div class="mb-4">
            <label class="form-label">비밀번호</label>
            <input
              v-model="registerForm.password"
              type="password"
              class="form-input w-full"
              required
            />
          </div>
          <div class="mb-6">
            <label class="form-label">비밀번호 확인</label>
            <input
              v-model="registerForm.confirmPassword"
              type="password"
              class="form-input w-full"
              required
            />
          </div>
          <button type="submit" class="btn-primary w-full">회원가입</button>
        </form>
        <div class="text-center mt-4">
          <router-link to="/login" class="text-blue-500 hover:underline">
            로그인
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";

const registerForm = reactive({
  username: "",
  email: "",
  password: "",
  confirmPassword: "",
});

const handleRegister = () => {
  if (registerForm.password !== registerForm.confirmPassword) {
    alert("비밀번호가 일치하지 않습니다.");
    return;
  }
  console.log("회원가입 시도:", registerForm);
};
</script>

<style scoped>
/* Register 컴포넌트 스타일 */
</style>
