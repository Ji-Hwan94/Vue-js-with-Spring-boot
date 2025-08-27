<template>
  <div class="space-y-4">
    <!-- 파일 업로드 영역 -->
    <div
      class="border-2 border-dashed border-gray-300 hover:border-blue-400 transition-colors rounded-lg p-6 text-center cursor-pointer"
      @click="$refs.fileInput.click()"
      @dragover.prevent="isDragOver = true"
      @dragleave.prevent="isDragOver = false"
      @drop.prevent="handleDrop"
      :class="{ 'border-blue-400 bg-blue-50': isDragOver }"
    >
      <input
        ref="fileInput"
        type="file"
        class="hidden"
        :multiple="multiple"
        :accept="accept"
        @change="handleFileSelect"
      />

      <div class="space-y-2">
        <svg
          class="mx-auto h-12 w-12 text-gray-400"
          stroke="currentColor"
          fill="none"
          viewBox="0 0 48 48"
        >
          <path
            d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
        </svg>
        <div class="text-gray-600">
          <span
            class="font-medium text-blue-600 hover:text-blue-500 cursor-pointer"
          >
            클릭하여 파일 선택
          </span>
          <span class="text-gray-500"> 또는 드래그 앤 드롭</span>
        </div>
        <p class="text-xs text-gray-500">{{ acceptText }}</p>
      </div>
    </div>

    <!-- 선택된 파일 목록 -->
    <div v-if="files.length > 0" class="space-y-3">
      <h4 class="font-medium text-gray-700">
        선택된 파일 ({{ files.length }}개)
      </h4>
      <div class="space-y-2">
        <div
          v-for="(file, index) in files"
          :key="index"
          class="flex items-center space-x-3 p-3 border border-gray-200 rounded-lg"
        >
          <!-- 파일 정보 -->
          <div class="flex-1 min-w-0">
            <p class="text-sm font-medium text-gray-900 truncate">
              {{ file.name || file.originalName }}
            </p>
            <p class="text-xs text-gray-500">
              {{ formatFileSize(file.size || file.fileSize) }}
            </p>
          </div>

          <!-- 삭제 버튼 -->
          <button
            @click="removeFile(index, file.id)"
            class="flex-shrink-0 p-1 text-red-400 hover:text-red-600 transition-colors"
          >
            삭제
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from "vue";

const props = defineProps({
  modelValue: {
    type: Array,
    default: () => [],
  },
  multiple: {
    type: Boolean,
    default: true,
  },
  accept: {
    type: String,
    default: "image/*,application/pdf,.doc,.docx,.txt",
  },
  maxSize: {
    type: Number,
    default: 10 * 1024 * 1024, // 10MB
  },
});

const emit = defineEmits([
  "update:modelValue",
  "files-selected",
  "file-delete",
]);

const files = ref([...props.modelValue]);
const isDragOver = ref(false);
const deleteFileId = ref([]);

// accept 속성을 기반으로 설명 텍스트 생성
const acceptText = computed(() => {
  const acceptTypes = props.accept.toLowerCase();
  if (acceptTypes.includes("image")) return "이미지 파일 지원";
  if (acceptTypes.includes("pdf")) return "PDF, 문서 파일 지원";
  return "모든 파일 형식 지원";
});

// 파일 크기 포맷팅
const formatFileSize = (bytes) => {
  if (bytes === 0) return "0 Bytes";
  const k = 1024;
  const sizes = ["Bytes", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
};

// 파일 선택 처리
const handleFileSelect = (event) => {
  const selectedFiles = Array.from(event.target.files);
  processFiles(selectedFiles);
};

// 드래그 앤 드롭 처리
const handleDrop = (event) => {
  isDragOver.value = false;
  const droppedFiles = Array.from(event.dataTransfer.files);
  processFiles(droppedFiles);
};

// 파일 처리
const processFiles = (newFiles) => {
  const validFiles = newFiles.filter((file) => {
    if (file.size > props.maxSize) {
      alert(
        `파일 "${file.name}"이 너무 큽니다. 최대 크기: ${formatFileSize(props.maxSize)}`
      );
      return false;
    }
    return true;
  });

  if (props.multiple) {
    files.value = [...files.value, ...validFiles];
  } else {
    files.value = validFiles.slice(0, 1);
  }

  updateModelValue();
};

// 파일 삭제
const removeFile = (index, id) => {
  files.value.splice(index, 1);
  deleteFileId.value.push(id);
  updateModelValue();
};

// v-model 업데이트
const updateModelValue = () => {
  emit("update:modelValue", [...files.value]);
  emit("files-selected", [...files.value]);
  emit("file-delete", [...deleteFileId.value]);
};

// props 변경 감지
watch(
  () => props.modelValue,
  (newValue) => {
    files.value = [...newValue];
  },
  { deep: true }
);
</script>
