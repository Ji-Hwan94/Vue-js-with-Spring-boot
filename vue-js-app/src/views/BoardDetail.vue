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
    await boardService.updateBoard(board.value.id, formData);

    board.value.title = formData.title;
    board.value.description = formData.description;

    closeEditModal();
    alert("게시글이 수정되었습니다.");
  } catch (error) {
    console.error("게시글 수정 실패:", error);
    alert("게시글 수정에 실패했습니다.");
  } finally {
    isUpdating.value = false;
  }
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
