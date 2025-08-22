<script setup>
import { boardService } from "@/service/boardService";
import { onMounted, ref } from "vue";

const boards = ref([]);

const boardSearch = async () => {
  try {
    const result = await boardService.getAllBoard();
    boards.value = result.data;
  } catch (error) {
    console.log(error);
  }
};

onMounted(() => {
  boardSearch();
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
        <tr v-for="board in boards" :key="board.id">
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
          <td class="table-cell">{{ board.createdAt.substring(0, 10) }}</td>
        </tr>
        <tr v-if="boards.length === 0">
          <td colspan="4" class="table-cell text-center text-gray-500">
            게시글이 없습니다.
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
