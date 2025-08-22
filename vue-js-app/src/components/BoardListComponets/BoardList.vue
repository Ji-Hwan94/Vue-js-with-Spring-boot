<script setup>
import { onMounted, ref } from "vue";

const boards = ref([
  {
    id: 1,
    title: "첫 번째 게시글",
    description: "게시글 내용입니다.",
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
]);

const filteredBoards = ref([]);

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString("ko-KR");
};

onMounted(() => {
  filteredBoards.value = boards.value;
});
</script>
<template>
  <!-- 게시글 목록 -->
  <div class="card">
    <table class="table-default">
      <thead class="table-header">
        <tr>
          <th class="table-cell font-semibold w-24">번호</th>
          <th class="table-cell font-semibold">제목</th>
          <th class="table-cell font-semibold w-32">작성자</th>
          <th class="table-cell font-semibold w-32">작성일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="board in filteredBoards" :key="board.id">
          <td class="table-cell">{{ board.id }}</td>
          <td class="table-cell">
            <router-link
              :to="`/boards/${board.id}`"
              class="text-blue-600 hover:underline"
            >
              {{ board.title }}
            </router-link>
          </td>
          <td class="table-cell">{{ board.username }}</td>
          <td class="table-cell">{{ formatDate(board.created_at) }}</td>
        </tr>
        <tr v-if="filteredBoards.length === 0">
          <td colspan="4" class="table-cell text-center text-gray-500">
            게시글이 없습니다.
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
