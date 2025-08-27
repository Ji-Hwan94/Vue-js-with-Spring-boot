<template>
  <div
    class="fixed inset-0 bg-black/50 flex items-center justify-center"
    @click="handleClose"
  >
    <div class="card max-w-2xl w-full mx-4" @click.stop>
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-xl font-bold">게시글 수정</h3>
        <button @click="handleClose" class="text-gray-400 hover:text-gray-600">
          <svg
            class="w-6 h-6"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M6 18L18 6M6 6l12 12"
            ></path>
          </svg>
        </button>
      </div>

      <div class="mb-6">
        <div class="space-y-4">
          <div>
            <label for="edit-title" class="form-label">제목</label>
            <input
              id="edit-title"
              v-model="localForm.title"
              type="text"
              class="form-input w-full"
              placeholder="제목을 입력하세요"
              required
            />
          </div>

          <div>
            <label for="edit-description" class="form-label">내용</label>
            <textarea
              id="edit-description"
              v-model="localForm.description"
              rows="8"
              class="form-input w-full"
              placeholder="내용을 입력하세요"
              required
            ></textarea>
          </div>

          <div>
            <label class="form-label">첨부파일</label>
            <FileUpload
              v-model="localForm.files"
              :multiple="true"
              accept="image/*,application/pdf,.doc,.docx,.txt"
              @files-selected="handleFilesSelected"
              @file-delete="setDeleteId"
            />
          </div>
        </div>
      </div>

      <div class="flex justify-end space-x-3">
        <button @click="handleClose" class="btn-secondary">취소</button>
        <button @click="handleUpdate" class="btn-primary" :disabled="loading">
          {{ loading ? "수정 중..." : "수정" }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onUnmounted, ref, watch } from "vue";
import FileUpload from "./FileUpload.vue";

const props = defineProps({
  board: {
    type: Object,
    default: null,
  },
  loading: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["close", "update"]);

const localForm = ref({
  title: "",
  description: "",
  files: [],
});

const deleteFileIds = ref([]);

// props.board가 변경될 때 로컬 폼 업데이트
watch(
  () => props.board,
  (newBoard) => {
    if (newBoard) {
      localForm.value.title = newBoard.title;
      localForm.value.description = newBoard.description;
      localForm.value.files = newBoard.files || [];
    }
  },
  { immediate: true }
);

const handleClose = () => {
  emit("close");
};

const setDeleteId = (param) => {
  deleteFileIds.value = param;
};

const handleFilesSelected = (files) => {
  localForm.value.files = files;
};

const handleUpdate = () => {
  if (!localForm.value.title.trim() || !localForm.value.description.trim()) {
    alert("제목과 내용을 모두 입력해주세요.");
    return;
  }

  emit("update", { ...localForm.value, deleteFileIds: deleteFileIds.value });
};

onUnmounted(() => {
  localForm.value = props.board;
});
</script>
