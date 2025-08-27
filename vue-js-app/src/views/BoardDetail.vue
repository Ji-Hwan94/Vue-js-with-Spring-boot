<template>
  <div class="container-main">
    <div class="card" v-if="board">
      <div class="border-b border-gray-200 pb-4 mb-4">
        <h1 class="text-3xl font-bold mb-2">{{ board.title }}</h1>
        <div class="flex justify-between text-sm text-gray-600">
          <span>작성자: {{ board.username }}</span>
          <span>작성일: {{ board.createdAt?.substring(0, 10) }}</span>
        </div>
      </div>
      <div class="prose max-w-none mb-6">
        <p class="whitespace-pre-wrap">{{ board.description }}</p>
      </div>
      <div v-if="board.files && board.files.length > 0" class="mb-4">
        <label class="form-label">첨부 파일</label>
        <div class="mt-2 space-y-2">
          <div
            v-for="file in board.files"
            :key="file.id"
            class="flex items-center justify-between p-3 bg-gray-50 rounded-lg border"
          >
            <div class="flex items-center space-x-3">
              <div>
                <p class="text-sm font-medium text-gray-900">
                  {{ file.originalName }}
                </p>
                <p class="text-xs text-gray-500">
                  {{ formatFileSize(file.fileSize) }}
                </p>
              </div>
            </div>
            <button
              @click="downloadFile(file)"
              class="text-blue-600 hover:text-blue-800 text-sm font-medium"
            >
              다운로드
            </button>
          </div>
        </div>
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

    <!-- 수정 모달 -->
    <BoardEditModal
      v-if="isEditModalOpen"
      :board="board"
      :loading="isUpdating"
      @close="closeEditModal"
      @update="handleUpdate"
    />
  </div>
</template>

<script setup>
import { boardService } from "@/service/boardService";
import BoardEditModal from "@/components/BoardEditModal.vue";
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { commonService } from "@/service/commonService";

const route = useRoute();
const router = useRouter();
const board = ref(null);

const isEditModalOpen = ref(false);
const isUpdating = ref(false);

const loadBoard = async () => {
  try {
    const boardId = parseInt(route.params.id);
    const result = await boardService.detailBoard(boardId);
    board.value = result.data;
  } catch (error) {
    throw error;
  }
};

const deleteBoard = async () => {
  try {
    if (confirm("정말로 삭제하시겠습니까?")) {
      await boardService.deleteBoard(route.params.id);
      router.push("/boards");
    }
  } catch (error) {
    throw error;
  }
};

const editBoard = () => {
  isEditModalOpen.value = !isEditModalOpen.value;
};

const closeEditModal = () => {
  isEditModalOpen.value = !isEditModalOpen.value;
};

const handleUpdate = async (formData) => {
  try {
    isUpdating.value = true;
    const result = await boardService.updateFileBoard(board.value.id, formData);

    closeEditModal();
    alert("게시글이 수정되었습니다.");

    board.value = result.data;
  } catch (error) {
    console.error("게시글 수정 실패:", error);
    alert("게시글 수정에 실패했습니다.");
  } finally {
    isUpdating.value = false;
  }
};

const formatFileSize = (bytes) => {
  if (bytes === 0) return "0 Bytes";
  const k = 1024;
  const sizes = ["Bytes", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
};

const downloadFile = (file) => {
  // 파일 다운로드 구현
  commonService.fileDownLoad(file);
};

onMounted(() => {
  loadBoard();
});
</script>

<style scoped>
/* BoardDetail 컴포넌트 스타일 */
</style>
