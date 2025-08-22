<script setup lang="ts">
import BoardList from "../components/BoardListComponets/BoardList.vue";
import BoardSearch from "../components/BoardListComponets/BoardSearch.vue";
</script>

<template>
  <div class="container-main">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold">게시판</h1>
      <router-link to="/boards/create" class="btn-primary">글쓰기</router-link>
    </div>
    <BoardSearch />
    <BoardList />
  </div>
</template>
