<template>
  <div class="container-main">
    <div class="card" v-if="board">
      <div class="border-b border-gray-200 pb-4 mb-4">
        <h1 class="text-3xl font-bold mb-2">{{ board.title }}</h1>
        <div class="flex justify-between text-sm text-gray-600">
          <span>작성자: {{ board.username }}</span>
          <span>작성일: {{ formatDate(board.created_at) }}</span>
        </div>
      </div>
      <div class="prose max-w-none mb-6">
        <p class="whitespace-pre-wrap">{{ board.description }}</p>
      </div>
      <div class="flex justify-between">
        <router-link to="/boards" class="btn-secondary">목록으로</router-link>
        <div class="space-x-2">
          <button class="btn-primary" @click="editBoard">수정</button>
          <button class="btn-danger" @click="deleteBoard">삭제</button>
        </div>
      </div>
    </div>
    <div class="card text-center" v-else>
      <p class="text-gray-500">게시글을 찾을 수 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const board = ref(null);

const loadBoard = () => {
  const boardId = parseInt(route.params.id);
  // 임시 데이터 (실제로는 API에서 가져올 예정)
  const boards = [
    {
      id: 1,
      title: "첫 번째 게시글",
      description: "게시글 내용입니다.\n\n여러 줄의 내용을 표시할 수 있습니다.",
      username: "사용자1",
      created_at: "2024-01-01T10:00:00Z",
    },
    {
      id: 2,
      title: "두 번째 게시글",
      description: "두 번째 게시글의 내용입니다.",
      username: "사용자2",
      created_at: "2024-01-02T15:30:00Z",
    },
  ];
  
  board.value = boards.find((b) => b.id === boardId);
};

const deleteBoard = () => {
  if (confirm("정말로 삭제하시겠습니까?")) {
    console.log("게시글 삭제:", board.value.id);
    router.push("/boards");
  }
};

const editBoard = () => {
  router.push(`/boards/${board.value.id}/edit`);
};

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString("ko-KR");
};

onMounted(() => {
  loadBoard();
});
</script>

<style scoped>
/* BoardDetail 컴포넌트 스타일 */
</style>