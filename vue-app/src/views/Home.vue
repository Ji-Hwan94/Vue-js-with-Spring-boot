<template>
  <div class="container-main">
    <div class="text-center">
      <h1 class="text-4xl font-bold text-gray-800 mb-8">게시판 애플리케이션</h1>
      <div class="space-y-4">
        <router-link
          to="/boards"
          class="btn-primary inline-block text-lg px-8 py-4"
        >
          게시판 보기
        </router-link>
        <div class="mt-4">
          <router-link to="/login" class="btn-secondary mr-4">로그인</router-link>
          <router-link to="/register" class="btn-secondary">회원가입</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Home 컴포넌트
</script>

<style scoped>
/* 컴포넌트별 스타일 */
</style>