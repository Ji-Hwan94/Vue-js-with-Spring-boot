<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <nav class="bg-white shadow-md">
      <div class="container-main">
        <div class="flex justify-between items-center py-4">
          <router-link to="/" class="text-xl font-bold text-gray-800">
            게시판 앱
          </router-link>
          <div class="space-x-4">
            <router-link to="/boards" class="text-gray-600 hover:text-gray-800">
              게시판
            </router-link>
            <router-link to="/login" class="text-gray-600 hover:text-gray-800">
              로그인
            </router-link>
            <router-link to="/register" class="text-gray-600 hover:text-gray-800">
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
// App 컴포넌트
</script>

<style scoped>
/* App 컴포넌트 스타일 */
</style>
