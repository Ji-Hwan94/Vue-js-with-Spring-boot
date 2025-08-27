<template>
  <div class="container-main">
    <div class="card">
      <h2 class="text-2xl font-bold mb-6">
        {{ isEditMode ? "게시글 수정" : "새 게시글 작성" }}
      </h2>
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
        <div class="mb-6">
          <label class="form-label">첨부파일</label>
          <FileUpload
            v-model="boardForm.files"
            :multiple="true"
            accept="image/*,application/pdf,.doc,.docx,.txt"
          />
        </div>
        <div class="flex justify-between">
          <router-link
            :to="isEditMode ? `/boards/${boardId}` : '/boards'"
            class="btn-secondary"
            >취소</router-link
          >
          <button type="submit" class="btn-primary">
            {{ isEditMode ? "수정 완료" : "작성 완료" }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import FileUpload from "@/components/FileUpload.vue";
import { boardService } from "@/service/boardService";
import { reactive, ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const boardId = ref(null);
const isEditMode = computed(() => route.name === "BoardEdit");

const boardForm = reactive({
  title: "",
  description: "",
  files: [],
});

const createBoard = async () => {
  try {
    if (!boardForm.title.trim() || !boardForm.description.trim()) {
      alert("제목과 내용을 모두 입력해주세요.");
      return;
    }

    const result = await boardService.createFileBoard(boardForm);

    if (result) {
      alert("게시물이 등록되었습니다.");
      router.push("/boards");
    }
  } catch (error) {
    console.log(error);
  }

  // if (isEditMode.value) {
  //   console.log("게시글 수정:", boardForm);
  //   alert("게시글이 수정되었습니다.");
  //   router.push(`/boards/${boardId.value}`);
  // } else {
  //   console.log("게시글 작성:", boardForm);
  //   alert("게시글이 작성되었습니다.");
  //   router.push("/boards");
  // }
};

onMounted(() => {});
</script>

<style scoped>
/* BoardCreate 컴포넌트 스타일 */
</style>
