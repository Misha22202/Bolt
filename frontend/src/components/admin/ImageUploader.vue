<script setup>
import { computed, ref } from 'vue'

import BaseButton from '@/components/uiBase/BaseButton.vue'
import { uploadImage } from '@/api/media.js'

const emit = defineEmits(['uploaded'])

const fileInput = ref(null)
const selectedFile = ref(null)
const previewUrl = ref('')
const isUploading = ref(false)
const errorMessage = ref('')
const uploadedFile = ref(null)

const allowedTypes = [
  'image/jpeg',
  'image/png',
  'image/webp'
]

const maxSizeBytes = 10 * 1024 * 1024

const formattedSize = computed(() => {
  if (!selectedFile.value) {
    return ''
  }

  const megabytes = selectedFile.value.size / 1024 / 1024

  return `${megabytes.toFixed(2)} MB`
})

function revokePreviewUrl() {
  if (previewUrl.value) {
    URL.revokeObjectURL(previewUrl.value)
    previewUrl.value = ''
  }
}

function resetSelection() {
  revokePreviewUrl()

  selectedFile.value = null
  errorMessage.value = ''

  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

function selectFile(event) {
  const [file] = event.target.files || []

  errorMessage.value = ''
  uploadedFile.value = null
  revokePreviewUrl()

  if (!file) {
    selectedFile.value = null
    return
  }

  if (!allowedTypes.includes(file.type)) {
    selectedFile.value = null
    event.target.value = ''
    errorMessage.value = 'Можно загрузить только JPG, PNG или WebP.'
    return
  }

  if (file.size > maxSizeBytes) {
    selectedFile.value = null
    event.target.value = ''
    errorMessage.value = 'Размер одного изображения не должен превышать 10 MB.'
    return
  }

  selectedFile.value = file
  previewUrl.value = URL.createObjectURL(file)
}

async function submit() {
  if (!selectedFile.value || isUploading.value) {
    return
  }

  isUploading.value = true
  errorMessage.value = ''
  uploadedFile.value = null

  try {
    const result = await uploadImage(selectedFile.value)

    uploadedFile.value = result
    emit('uploaded', result)
  } catch (error) {
    errorMessage.value = error.message || 'Не удалось загрузить изображение.'
  } finally {
    isUploading.value = false
  }
}

async function copyUrl() {
  if (!uploadedFile.value?.publicUrl) {
    return
  }

  try {
    await navigator.clipboard.writeText(uploadedFile.value.publicUrl)
  } catch {
    errorMessage.value = 'Не удалось скопировать ссылку. Скопируйте её вручную.'
  }
}
</script>

<template>
  <section class="image-uploader">
    <div class="image-uploader__header">
      <div>
        <h2>Загрузить изображение</h2>
        <p>Поддерживаются JPG, PNG и WebP. Максимальный размер: 10 MB.</p>
      </div>
    </div>

    <label class="file-picker">
      <span class="file-picker__label">Выберите файл</span>

      <input
          ref="fileInput"
          type="file"
          accept="image/jpeg,image/png,image/webp"
          @change="selectFile"
      >

      <span class="file-picker__hint">
        {{ selectedFile ? selectedFile.name : 'Файл не выбран' }}
      </span>
    </label>

    <p v-if="selectedFile" class="image-uploader__meta">
      {{ formattedSize }} · {{ selectedFile.type }}
    </p>

    <div v-if="previewUrl" class="image-preview">
      <img :src="previewUrl" alt="Предпросмотр выбранного изображения">
    </div>

    <p v-if="errorMessage" class="image-uploader__error">
      {{ errorMessage }}
    </p>

    <div class="image-uploader__actions">
      <BaseButton
          variant="primary"
          :disabled="!selectedFile || isUploading"
          @click="submit"
      >
        {{ isUploading ? 'Загрузка…' : 'Загрузить' }}
      </BaseButton>

      <BaseButton
          v-if="selectedFile"
          variant="secondary"
          :disabled="isUploading"
          @click="resetSelection"
      >
        Очистить
      </BaseButton>
    </div>

    <div v-if="uploadedFile" class="upload-result">
      <p class="upload-result__title">Изображение загружено</p>

      <img
          class="upload-result__image"
          :src="uploadedFile.publicUrl"
          :alt="uploadedFile.originalFilename"
      >

      <label class="admin-field">
        <span>Публичный путь</span>

        <div class="upload-result__url">
          <input :value="uploadedFile.publicUrl" type="text" readonly>
          <button type="button" @click="copyUrl">
            Копировать
          </button>
        </div>
      </label>
    </div>
  </section>
</template>

<style scoped>
.image-uploader {
  display: grid;
  gap: 18px;
  border: 1px solid #2a2a2d;
  border-radius: 18px;
  padding: 24px;
  background: #1b1b1e;
}

.image-uploader__header h2 {
  margin: 0;
  color: #fff;
  font-size: 20px;
}

.image-uploader__header p {
  margin: 6px 0 0;
  color: #9a9aa0;
  font-size: 14px;
}

.file-picker {
  display: grid;
  gap: 8px;
  cursor: pointer;
}

.file-picker__label {
  color: #d6d6da;
  font-size: 14px;
  font-weight: 600;
}

.file-picker input {
  width: 100%;
  border: 1px dashed #4a4a50;
  border-radius: 10px;
  padding: 12px;
  color: #ddd;
  background: #121214;
  cursor: pointer;
}

.file-picker__hint {
  overflow: hidden;
  color: #9a9aa0;
  font-size: 13px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.image-uploader__meta {
  margin: -6px 0 0;
  color: #9a9aa0;
  font-size: 13px;
}

.image-preview {
  overflow: hidden;
  width: min(100%, 620px);
  aspect-ratio: 16 / 9;
  border: 1px solid #343439;
  border-radius: 12px;
  background: #111113;
}

.image-preview img {
  display: block;
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.image-uploader__error {
  margin: 0;
  border: 1px solid #7f1d1d;
  border-radius: 10px;
  padding: 12px;
  color: #fecaca;
  background: #3b1111;
}

.image-uploader__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.upload-result {
  display: grid;
  gap: 14px;
  margin-top: 6px;
  border-top: 1px solid #343439;
  padding-top: 22px;
}

.upload-result__title {
  margin: 0;
  color: #bbf7d0;
  font-weight: 700;
}

.upload-result__image {
  display: block;
  width: min(100%, 620px);
  max-height: 420px;
  border-radius: 12px;
  object-fit: contain;
  background: #111113;
}

.upload-result__url {
  display: flex;
  gap: 8px;
}

.upload-result__url input {
  min-width: 0;
  flex: 1;
}

.upload-result__url button {
  border: 0;
  border-radius: 8px;
  padding: 0 12px;
  color: #111;
  background: var(--accent);
  font: inherit;
  font-weight: 700;
  cursor: pointer;
}

.upload-result__note {
  margin: 0;
  color: #9a9aa0;
  font-size: 13px;
  line-height: 1.45;
}
</style>