<template>
  <div class="container-main">
    <div class="card">
      <h2 class="text-2xl font-bold mb-6">{{ isEditMode ? '게시글 수정' : '새 게시글 작성' }}</h2>
      <form @submit.prevent="createBoard">
        <div class="mb-4">
          <label class="form-label">제목</label>
          <input
            v-model="boardForm.title"
            type="text"
            class="form-input w-full"
            placeholder="게시글 제목을 입력하세요"
            required
          />
        </div>
        <div class="mb-6">
          <label class="form-label">내용</label>
          <textarea
            v-model="boardForm.description"
            class="form-input w-full"
            rows="10"
            placeholder="게시글 내용을 입력하세요"
            required
          ></textarea>
        </div>
        <div class="flex justify-between">
          <router-link :to="isEditMode ? `/boards/${boardId}` : '/boards'" class="btn-secondary">취소</router-link>
          <button type="submit" class="btn-primary">{{ isEditMode ? '수정 완료' : '작성 완료' }}</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const boardId = ref(null);
const isEditMode = computed(() => route.name === "BoardEdit");

const boardForm = reactive({
  title: "",
  description: "",
});

const loadBoard = () => {
  if (isEditMode.value) {
    boardId.value = parseInt(route.params.id);
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
    
    const board = boards.find((b) => b.id === boardId.value);
    if (board) {
      boardForm.title = board.title;
      boardForm.description = board.description;
    }
  }
};

const createBoard = () => {
  if (!boardForm.title.trim() || !boardForm.description.trim()) {
    alert("제목과 내용을 모두 입력해주세요.");
    return;
  }
  
  if (isEditMode.value) {
    console.log("게시글 수정:", boardForm);
    alert("게시글이 수정되었습니다.");
    router.push(`/boards/${boardId.value}`);
  } else {
    console.log("게시글 작성:", boardForm);
    alert("게시글이 작성되었습니다.");
    router.push("/boards");
  }
};

onMounted(() => {
  loadBoard();
});
</script>

<style scoped>
/* BoardCreate 컴포넌트 스타일 */
</style>