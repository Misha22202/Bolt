<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

import { adminFetch } from '@/api/adminApi.js'
import AdminHeader from "@/components/admin/AdminHeader.vue";

const router = useRouter()

const mediaItems = ref([])
const isLoading = ref(true)
const isUploading = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const selectedFile = ref(null)
const previewUrl = ref('')

const uploadForm = reactive({
  category: 'WORKSHOP',
  altText: '',
  sortOrder: 0
})

const categories = [
  {
    value: 'WORKSHOP',
    label: 'Мастерская'
  },
  {
    value: 'CASE',
    label: 'Работы / кейсы'
  },
  {
    value: 'HERO',
    label: 'Главная фотография'
  },
  {
    value: 'OTHER',
    label: 'Другое'
  }
]

const hasSelectedFile = computed(() => selectedFile.value !== null)

function resetMessages() {
  errorMessage.value = ''
  successMessage.value = ''
}

async function loadMedia() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await adminFetch('/api/admin/media')

    if (response.status === 401) {
      await router.replace('/admin/login')
      return
    }

    if (!response.ok) {
      throw new Error('Не удалось загрузить список фотографий.')
    }

    mediaItems.value = await response.json()
  } catch (error) {
    errorMessage.value =
        error instanceof Error
            ? error.message
            : 'Не удалось загрузить список фотографий.'
  } finally {
    isLoading.value = false
  }
}

function handleFileChange(event) {
  const [file] = event.target.files

  resetMessages()

  if (!file) {
    selectedFile.value = null
    previewUrl.value = ''
    return
  }

  const allowedTypes = [
    'image/jpeg',
    'image/png',
    'image/webp'
  ]

  if (!allowedTypes.includes(file.type)) {
    errorMessage.value = 'Можно загрузить только JPG, PNG или WebP.'
    event.target.value = ''
    return
  }

  if (file.size > 10 * 1024 * 1024) {
    errorMessage.value = 'Размер одного файла не должен превышать 10 МБ.'
    event.target.value = ''
    return
  }

  if (previewUrl.value) {
    URL.revokeObjectURL(previewUrl.value)
  }

  selectedFile.value = file
  previewUrl.value = URL.createObjectURL(file)
}

async function uploadFile() {
  resetMessages()

  if (!selectedFile.value) {
    errorMessage.value = 'Сначала выберите изображение.'
    return
  }

  isUploading.value = true

  try {
    const data = new FormData()

    data.append('file', selectedFile.value)
    data.append('category', uploadForm.category)
    data.append('altText', uploadForm.altText.trim())
    data.append('sortOrder', String(uploadForm.sortOrder))

    const response = await adminFetch('/api/admin/media', {
      method: 'POST',
      body: data
    })

    if (response.status === 401) {
      await router.replace('/admin/login')
      return
    }

    if (!response.ok) {
      throw new Error('Не удалось загрузить изображение.')
    }

    selectedFile.value = null
    uploadForm.altText = ''
    uploadForm.sortOrder = 0

    if (previewUrl.value) {
      URL.revokeObjectURL(previewUrl.value)
      previewUrl.value = ''
    }

    successMessage.value = 'Изображение успешно загружено.'
    await loadMedia()
  } catch (error) {
    errorMessage.value =
        error instanceof Error
            ? error.message
            : 'Не удалось загрузить изображение.'
  } finally {
    isUploading.value = false
  }
}

async function deleteMedia(item) {
  const isConfirmed = window.confirm(
      `Удалить файл «${item.originalFilename}»? Это действие нельзя отменить.`
  )

  if (!isConfirmed) {
    return
  }

  resetMessages()

  try {
    const response = await adminFetch(`/api/admin/media/${item.id}`, {
      method: 'DELETE'
    })

    if (response.status === 401) {
      await router.replace('/admin/login')
      return
    }

    if (!response.ok && response.status !== 204) {
      throw new Error('Не удалось удалить изображение.')
    }

    successMessage.value = 'Изображение удалено.'
    await loadMedia()
  } catch (error) {
    errorMessage.value =
        error instanceof Error
            ? error.message
            : 'Не удалось удалить изображение.'
  }
}

function categoryLabel(category) {
  return categories.find((item) => item.value === category)?.label || category
}

onMounted(loadMedia)
</script>

<template>
  <main class="admin-page">
    <AdminHeader />

    <div class="admin-container">
      <section class="page-heading">
        <div>
          <span class="eyebrow">Медиатека</span>
          <h1>Фотографии</h1>
          <p>
            Загружайте фотографии мастерской, работ и изображение для
            главного экрана. После этого их можно будет назначать секциям сайта.
          </p>
        </div>
      </section>

      <section class="upload-card">
        <div class="upload-heading">
          <h2>Загрузить фото</h2>
          <span>JPG, PNG или WebP · до 10 МБ</span>
        </div>

        <form class="upload-form" @submit.prevent="uploadFile">
          <div class="file-field">
            <label class="file-label" for="media-file">
              <span>Выберите изображение</span>

              <small v-if="selectedFile">
                {{ selectedFile.name }}
              </small>

              <small v-else>
                Файл пока не выбран
              </small>
            </label>

            <input
                id="media-file"
                type="file"
                accept="image/jpeg,image/png,image/webp"
                :disabled="isUploading"
                @change="handleFileChange"
            >
          </div>

          <div v-if="previewUrl" class="preview">
            <img :src="previewUrl" alt="Предпросмотр выбранного изображения">
          </div>

          <div class="upload-options">
            <div class="field">
              <label for="media-category">Раздел</label>

              <select
                  id="media-category"
                  v-model="uploadForm.category"
                  :disabled="isUploading"
              >
                <option
                    v-for="category in categories"
                    :key="category.value"
                    :value="category.value"
                >
                  {{ category.label }}
                </option>
              </select>
            </div>

            <div class="field">
              <label for="media-order">Порядок показа</label>

              <input
                  id="media-order"
                  v-model.number="uploadForm.sortOrder"
                  type="number"
                  min="0"
                  step="1"
                  :disabled="isUploading"
              >
            </div>

            <div class="field field--wide">
              <label for="media-alt">
                Описание фотографии
              </label>

              <input
                  id="media-alt"
                  v-model="uploadForm.altText"
                  type="text"
                  maxlength="255"
                  placeholder="Например: Ремонт пылесоса в мастерской"
                  :disabled="isUploading"
              >
            </div>
          </div>

          <p
              v-if="errorMessage"
              class="form-message form-message--error"
              role="alert"
          >
            {{ errorMessage }}
          </p>

          <p
              v-if="successMessage"
              class="form-message form-message--success"
              role="status"
          >
            {{ successMessage }}
          </p>

          <button
              class="primary-button"
              type="submit"
              :disabled="isUploading || !hasSelectedFile"
          >
            {{ isUploading ? 'Загружаем…' : 'Загрузить фотографию' }}
          </button>
        </form>
      </section>

      <section class="gallery-section">
        <div class="gallery-heading">
          <div>
            <h2>Все загруженные файлы</h2>
            <p>{{ mediaItems.length }} шт.</p>
          </div>

          <button
              class="secondary-button"
              type="button"
              :disabled="isLoading"
              @click="loadMedia"
          >
            Обновить
          </button>
        </div>

        <div v-if="isLoading" class="empty-state">
          Загружаем фотографии…
        </div>

        <div v-else-if="!mediaItems.length" class="empty-state">
          Здесь появятся загруженные изображения.
        </div>

        <div v-else class="media-grid">
          <article
              v-for="item in mediaItems"
              :key="item.id"
              class="media-card"
          >
            <a
                class="image-link"
                :href="item.publicUrl"
                target="_blank"
                rel="noopener noreferrer"
            >
              <img
                  :src="item.publicUrl"
                  :alt="item.altText || item.originalFilename"
              >
            </a>

            <div class="media-info">
              <span class="media-category">
                {{ categoryLabel(item.category) }}
              </span>

              <p class="media-name">
                {{ item.originalFilename }}
              </p>

              <p class="media-alt">
                {{ item.altText || 'Описание не указано' }}
              </p>

              <div class="media-bottom">
                <span>#{{ item.sortOrder }}</span>

                <button
                    class="delete-button"
                    type="button"
                    @click="deleteMedia(item)"
                >
                  Удалить
                </button>
              </div>
            </div>
          </article>
        </div>
      </section>
    </div>
  </main>
</template>

<style scoped>
.admin-page {
  min-height: 100dvh;
  background: var(--bg);
}

.admin-header {
  display: flex;
  min-height: 72px;
  align-items: center;
  gap: 28px;
  padding: 0 32px;
  color: #fff;
  background: var(--card-dark);
  border-bottom: 1px solid #2a2a2d;
}

.admin-brand {
  display: inline-flex;
  flex-shrink: 0;
  align-items: center;
  gap: 10px;
}

.admin-brand span {
  display: flex;
  flex-direction: column;
  line-height: 1.05;
}

.admin-brand b {
  color: #fff;
  font-size: 14px;
  letter-spacing: 0.8px;
}

.admin-brand small {
  margin-top: 4px;
  color: #9a9aa0;
  font-size: 10px;
  letter-spacing: 1.3px;
  text-transform: uppercase;
}

.admin-nav {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-right: auto;
}

.admin-nav a {
  color: #b6b6bb;
  font-size: 13.5px;
}

.admin-nav a:hover,
.admin-nav a.router-link-active {
  color: var(--accent);
}

.logout-button,
.secondary-button,
.primary-button,
.delete-button {
  cursor: pointer;
  font: inherit;
}

.logout-button {
  padding: 8px 12px;
  border: 1px solid rgba(255, 255, 255, 0.22);
  border-radius: 8px;
  color: #fff;
  background: transparent;
  font-size: 13px;
}

.admin-container {
  width: min(100% - 48px, 1120px);
  margin: 0 auto;
  padding: 54px 0 72px;
}

.page-heading {
  max-width: 720px;
  margin-bottom: 28px;
}

.eyebrow {
  display: inline-block;
  margin-bottom: 9px;
  color: var(--accent-dark);
  font-size: 11px;
  font-weight: 800;
  letter-spacing: 1.4px;
  text-transform: uppercase;
}

.page-heading h1 {
  margin: 0 0 10px;
  color: var(--ink);
  font-size: 38px;
  line-height: 1.1;
  letter-spacing: -0.8px;
}

.page-heading p {
  margin: 0;
  color: var(--ink-soft);
  font-size: 14.5px;
  line-height: 1.6;
}

.upload-card,
.media-card,
.empty-state {
  border: 1px solid var(--line);
  border-radius: var(--radius);
  background: var(--card);
}

.upload-card {
  padding: 28px;
  box-shadow: 0 10px 24px rgba(17, 17, 19, 0.035);
}

.upload-heading,
.gallery-heading,
.media-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
}

.upload-heading {
  margin-bottom: 23px;
}

.upload-heading h2,
.gallery-heading h2 {
  margin: 0;
  color: var(--ink);
  font-size: 20px;
}

.upload-heading span,
.gallery-heading p {
  margin: 0;
  color: var(--muted);
  font-size: 12.5px;
}

.upload-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.file-field {
  position: relative;
}

.file-field input {
  position: absolute;
  width: 1px;
  height: 1px;
  overflow: hidden;
  clip: rect(0 0 0 0);
  clip-path: inset(50%);
  white-space: nowrap;
}

.file-label {
  display: flex;
  min-height: 84px;
  flex-direction: column;
  justify-content: center;
  gap: 6px;
  box-sizing: border-box;
  padding: 16px 18px;
  border: 1px dashed var(--accent-border);
  border-radius: 10px;
  cursor: pointer;
  background: var(--accent-soft);
}

.file-label span {
  color: var(--ink);
  font-size: 14px;
  font-weight: 750;
}

.file-label small {
  color: var(--muted);
  font-size: 12.5px;
}

.preview {
  width: 180px;
  overflow: hidden;
  border: 1px solid var(--line);
  border-radius: 10px;
  aspect-ratio: 4 / 3;
  background: #eeeeef;
}

.preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-options {
  display: grid;
  grid-template-columns: 1fr 170px 2fr;
  gap: 16px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.field label {
  color: var(--ink-soft);
  font-size: 13px;
  font-weight: 650;
}

.field input,
.field select {
  width: 100%;
  box-sizing: border-box;
  padding: 11px 12px;
  border: 1px solid var(--line);
  border-radius: 9px;
  outline: none;
  color: var(--ink);
  background: #fff;
  font: inherit;
  font-size: 14px;
}

.field input:focus,
.field select:focus {
  border-color: var(--accent-dark);
  box-shadow: 0 0 0 4px var(--accent-soft);
}

.form-message {
  margin: 0;
  font-size: 13px;
}

.form-message--error {
  color: var(--danger);
}

.form-message--success {
  color: var(--success);
}

.primary-button {
  align-self: flex-start;
  padding: 12px 17px;
  border: 0;
  border-radius: 9px;
  color: var(--accent-dark);
  background: var(--accent);
  font-size: 13.5px;
  font-weight: 800;
}

.primary-button:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

.gallery-section {
  margin-top: 48px;
}

.gallery-heading {
  margin-bottom: 16px;
}

.secondary-button {
  padding: 9px 12px;
  border: 1px solid var(--line);
  border-radius: 8px;
  color: var(--ink);
  background: #fff;
  font-size: 13px;
}

.media-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.media-card {
  overflow: hidden;
}

.image-link {
  display: block;
  overflow: hidden;
  border-bottom: 1px solid var(--line);
  aspect-ratio: 4 / 3;
  background: #eeeeef;
}

.image-link img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.25s ease;
}

.image-link:hover img {
  transform: scale(1.04);
}

.media-info {
  padding: 15px;
}

.media-category {
  display: inline-flex;
  padding: 4px 7px;
  border-radius: 6px;
  color: var(--accent-dark);
  background: var(--accent-soft);
  font-size: 10.5px;
  font-weight: 800;
}

.media-name {
  overflow: hidden;
  margin: 12px 0 5px;
  color: var(--ink);
  font-size: 13px;
  font-weight: 700;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.media-alt {
  overflow: hidden;
  min-height: 34px;
  margin: 0;
  color: var(--muted);
  font-size: 12px;
  line-height: 1.4;
}

.media-bottom {
  margin-top: 15px;
  color: var(--muted);
  font-size: 11.5px;
}

.delete-button {
  padding: 5px 7px;
  border: 0;
  color: var(--danger);
  background: transparent;
  font-size: 12px;
}

.empty-state {
  padding: 30px;
  color: var(--muted);
  font-size: 14px;
  text-align: center;
}

@media screen and (max-width: 850px) {
  .admin-header {
    padding: 0 20px;
  }

  .admin-container {
    width: min(100% - 36px, 1120px);
    padding-top: 38px;
  }

  .upload-options {
    grid-template-columns: 1fr 1fr;
  }

  .field--wide {
    grid-column: span 2;
  }

  .media-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media screen and (max-width: 620px) {
  .admin-header {
    flex-wrap: wrap;
    gap: 12px;
    padding: 15px 18px;
  }

  .admin-nav {
    order: 3;
    width: 100%;
    gap: 16px;
  }

  .logout-button {
    margin-left: auto;
  }

  .upload-options {
    grid-template-columns: 1fr;
  }

  .field--wide {
    grid-column: span 1;
  }

  .media-grid {
    grid-template-columns: 1fr;
  }
}
</style>