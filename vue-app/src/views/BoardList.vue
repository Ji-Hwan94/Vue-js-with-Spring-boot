<template>
  <div class="container-main">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold">게시판</h1>
      <router-link to="/boards/create" class="btn-primary">글쓰기</router-link>
    </div>

    <!-- 검색 영역 -->
    <div class="card mb-6">
      <div class="flex gap-4 items-end">
        <div class="flex-1">
          <label class="form-label">검색어</label>
          <input
            v-model="searchForm.keyword"
            type="text"
            class="form-input w-full"
            placeholder="검색어를 입력하세요"
          />
        </div>
        <div class="w-32">
          <label class="form-label">검색 조건</label>
          <select v-model="searchForm.type" class="form-input w-full">
            <option value="title">제목</option>
            <option value="description">내용</option>
          </select>
        </div>
        <button @click="searchBoards" class="btn-primary">검색</button>
        <button @click="resetSearch" class="btn-secondary">초기화</button>
      </div>
    </div>

    <!-- 게시글 목록 -->
    <div class="card">
      <table class="table-default">
        <thead class="table-header">
          <tr>
            <th class="table-cell font-semibold">번호</th>
            <th class="table-cell font-semibold">제목</th>
            <th class="table-cell font-semibold">작성자</th>
            <th class="table-cell font-semibold">작성일</th>
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
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";

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

const searchForm = reactive({
  keyword: "",
  type: "title",
});

const filteredBoards = ref([]);

const searchBoards = () => {
  if (!searchForm.keyword.trim()) {
    filteredBoards.value = boards.value;
    return;
  }

  filteredBoards.value = boards.value.filter((board) => {
    const searchTarget =
      searchForm.type === "title" ? board.title : board.description;
    return searchTarget
      .toLowerCase()
      .includes(searchForm.keyword.toLowerCase());
  });
};

const resetSearch = () => {
  searchForm.keyword = "";
  searchForm.type = "title";
  filteredBoards.value = boards.value;
};

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString("ko-KR");
};

onMounted(() => {
  filteredBoards.value = boards.value;
});
</script>

<style scoped>
/* BoardList 컴포넌트 스타일 */
</style>