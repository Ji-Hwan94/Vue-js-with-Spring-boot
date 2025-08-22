<script setup>
import { reactive } from "vue";

const searchForm = reactive({
  keyword: "",
  type: "title",
});

const resetSearch = () => {
  searchForm.keyword = "";
  searchForm.type = "title";
  filteredBoards.value = boards.value;
};

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
</script>
<template>
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
</template>
