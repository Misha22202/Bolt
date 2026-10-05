<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

import AdminHeader from '@/components/admin/AdminHeader.vue'
import { adminFetch } from '@/api/adminApi.js'

const router = useRouter()

const services = ref([])
const isLoading = ref(true)
const isSaving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const editingId = ref(null)

const emptyForm = {
  name: '',
  description: '',
  price: '',
  icon: 'tools',
  sortOrder: 0,
  isVisible: true
}

const form = reactive({
  ...emptyForm
})

const isEditing = computed(() => editingId.value !== null)

function resetMessages() {
  errorMessage.value = ''
  successMessage.value = ''
}

function resetForm() {
  Object.assign(form, emptyForm)
  editingId.value = null
  resetMessages()
}

async function loadServices() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await adminFetch('/api/admin/services')

    if (response.status === 401) {
      await router.replace('/admin/login')
      return
    }

    if (!response.ok) {
      throw new Error('Не удалось загрузить список услуг.')
    }

    services.value = await response.json()
  } catch (error) {
    errorMessage.value =
        error instanceof Error
            ? error.message
            : 'Не удалось загрузить список услуг.'
  } finally {
    isLoading.value = false
  }
}

function startEditing(service) {
  editingId.value = service.id

  Object.assign(form, {
    name: service.name ?? '',
    description: service.description ?? '',
    price: service.price ?? '',
    icon: service.icon ?? 'tools',
    sortOrder: service.sortOrder ?? 0,
    isVisible: service.isVisible ?? true
  })

  resetMessages()

  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

async function saveService() {
  resetMessages()

  if (!form.name.trim()) {
    errorMessage.value = 'Укажите название услуги.'
    return
  }

  isSaving.value = true

  try {
    const url = isEditing.value
        ? `/api/admin/services/${editingId.value}`
        : '/api/admin/services'

    const response = await adminFetch(url, {
      method: isEditing.value ? 'PUT' : 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: form.name.trim(),
        description: form.description.trim() || null,
        price: form.price.trim() || null,
        icon: form.icon,
        sortOrder: Number(form.sortOrder),
        isVisible: form.isVisible
      })
    })

    if (response.status === 401) {
      await router.replace('/admin/login')
      return
    }

    if (!response.ok) {
      throw new Error('Не удалось сохранить услугу.')
    }

    successMessage.value = isEditing.value
        ? 'Услуга обновлена.'
        : 'Услуга добавлена.'

    resetForm()
    await loadServices()
  } catch (error) {
    errorMessage.value =
        error instanceof Error
            ? error.message
            : 'Не удалось сохранить услугу.'
    console.log("[AdminServicesView Error] ", error, ", ", error.message)
  } finally {
    isSaving.value = false
  }
}

async function deleteService(service) {
  const isConfirmed = window.confirm(
      `Удалить услугу «${service.name}»? Это действие нельзя отменить.`
  )

  if (!isConfirmed) {
    return
  }

  resetMessages()

  try {
    const response = await adminFetch(
        `/api/admin/services/${service.id}`,
        {
          method: 'DELETE'
        }
    )

    if (response.status === 401) {
      await router.replace('/admin/login')
      return
    }

    if (!response.ok && response.status !== 204) {
      throw new Error('Не удалось удалить услугу.')
    }

    if (editingId.value === service.id) {
      resetForm()
    }

    successMessage.value = 'Услуга удалена.'
    await loadServices()
  } catch (error) {
    errorMessage.value =
        error instanceof Error
            ? error.message
            : 'Не удалось удалить услугу.'
  }
}

onMounted(loadServices)
</script>

<template>
  <main class="admin-page">
    <AdminHeader />

    <div class="admin-container">
      <section class="page-heading">
        <div>
          <span class="eyebrow">Содержимое сайта</span>
          <h1>Услуги</h1>
          <p>
            Добавляйте, редактируйте, скрывайте и удаляйте услуги,
            которые затем будут показаны на публичном сайте.
          </p>
        </div>

        <button
            v-if="isEditing"
            class="secondary-button"
            type="button"
            @click="resetForm"
        >
          Отменить редактирование
        </button>
      </section>

      <section class="editor-card">
        <div class="editor-card-heading">
          <h2>
            {{ isEditing ? 'Редактирование услуги' : 'Новая услуга' }}
          </h2>

          <span v-if="isEditing" class="edit-badge">
            ID: {{ editingId }}
          </span>
        </div>

        <form class="service-form" @submit.prevent="saveService">
          <div class="field field--wide">
            <label for="service-name">
              Название <span>*</span>
            </label>

            <input
                id="service-name"
                v-model="form.name"
                type="text"
                maxlength="255"
                placeholder="Например: Ремонт робота-пылесоса"
                :disabled="isSaving"
                required
            >
          </div>

          <div class="field">
            <label for="service-price">Цена</label>

            <input
                id="service-price"
                v-model="form.price"
                type="text"
                maxlength="100"
                placeholder="от 1 200 ₽"
                :disabled="isSaving"
            >
          </div>

          <div class="field">
            <label for="service-order">Порядок показа</label>

            <input
                id="service-order"
                v-model.number="form.sortOrder"
                type="number"
                min="0"
                step="1"
                :disabled="isSaving"
            >
          </div>

          <div class="field">
            <label for="service-icon">Иконка</label>

            <select
                id="service-icon"
                v-model="form.icon"
                :disabled="isSaving"
            >
              <option value="tools">Инструменты</option>
              <option value="diagnostic">Диагностика</option>
              <option value="board">Плата</option>
              <option value="warranty">Гарантия</option>
              <option value="price">Цена</option>
              <option value="speed">Скорость</option>
            </select>
          </div>

          <div class="field field--checkbox">
            <label class="checkbox-label" for="service-visible">
              <input
                  id="service-visible"
                  v-model="form.isVisible"
                  type="checkbox"
                  :disabled="isSaving"
              >

              <span>Показывать на сайте</span>
            </label>
          </div>

          <div class="field field--wide">
            <label for="service-description">Описание</label>

            <textarea
                id="service-description"
                v-model="form.description"
                rows="4"
                maxlength="3000"
                placeholder="Кратко опишите, что входит в услугу."
                :disabled="isSaving"
            ></textarea>
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

          <div class="form-actions">
            <button
                class="primary-button"
                type="submit"
                :disabled="isSaving"
            >
              {{
                isSaving
                    ? 'Сохраняем…'
                    : isEditing
                        ? 'Сохранить изменения'
                        : 'Добавить услугу'
              }}
            </button>

            <button
                v-if="isEditing"
                class="text-button"
                type="button"
                :disabled="isSaving"
                @click="resetForm"
            >
              Отмена
            </button>
          </div>
        </form>
      </section>

      <section class="list-section">
        <div class="list-heading">
          <div>
            <h2>Все услуги</h2>
            <p>Порядок списка зависит от поля «Порядок показа».</p>
          </div>

          <button
              class="secondary-button"
              type="button"
              :disabled="isLoading"
              @click="loadServices"
          >
            Обновить
          </button>
        </div>

        <div v-if="isLoading" class="empty-state">
          Загружаем услуги…
        </div>

        <div
            v-else-if="!services.length"
            class="empty-state"
        >
          Услуг пока нет. Добавьте первую через форму выше.
        </div>

        <div v-else class="services-list">
          <article
              v-for="service in services"
              :key="service.id"
              class="service-row"
              :class="{ 'service-row--hidden': !service.isVisible }"
          >
            <div class="service-order">
              {{ service.sortOrder }}
            </div>

            <div class="service-content">
              <div class="service-title-line">
                <h3>{{ service.name }}</h3>

                <span
                    v-if="!service.isVisible"
                    class="hidden-badge"
                >
                  Скрыта
                </span>
              </div>

              <p v-if="service.description">
                {{ service.description }}
              </p>
            </div>

            <span class="service-price">
              {{ service.price || 'Цена не указана' }}
            </span>

            <div class="row-actions">
              <button
                  class="icon-button"
                  type="button"
                  title="Редактировать"
                  @click="startEditing(service)"
              >
                Изменить
              </button>

              <button
                  class="icon-button icon-button--danger"
                  type="button"
                  title="Удалить"
                  @click="deleteService(service)"
              >
                Удалить
              </button>
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
  transition: color 0.2s ease;
}

.admin-nav a:hover,
.admin-nav a.router-link-active {
  color: var(--accent);
}

.logout-button,
.secondary-button,
.primary-button,
.text-button,
.icon-button {
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

.page-heading,
.list-heading,
.editor-card-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 24px;
}

.page-heading {
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
  max-width: 670px;
  margin: 0;
  color: var(--ink-soft);
  font-size: 14.5px;
  line-height: 1.6;
}

.editor-card,
.service-row,
.empty-state {
  border: 1px solid var(--line);
  border-radius: var(--radius);
  background: var(--card);
}

.editor-card {
  padding: 28px;
  box-shadow: 0 10px 24px rgba(17, 17, 19, 0.035);
}

.editor-card-heading {
  margin-bottom: 24px;
}

.editor-card h2,
.list-heading h2 {
  margin: 0;
  color: var(--ink);
  font-size: 20px;
}

.edit-badge,
.hidden-badge {
  display: inline-flex;
  align-items: center;
  padding: 5px 8px;
  border-radius: 7px;
  color: var(--accent-dark);
  background: var(--accent-soft);
  font-size: 11px;
  font-weight: 750;
}

.service-form {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 18px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.field--wide {
  grid-column: span 4;
}

.field--checkbox {
  justify-content: flex-end;
}

.field label {
  color: var(--ink-soft);
  font-size: 13px;
  font-weight: 650;
}

.field label span {
  color: var(--danger);
}

.field input,
.field select,
.field textarea {
  width: 100%;
  box-sizing: border-box;
  border: 1px solid var(--line);
  border-radius: 9px;
  outline: none;
  padding: 11px 12px;
  color: var(--ink);
  background: #fff;
  font: inherit;
  font-size: 14px;
}

.field textarea {
  resize: vertical;
}

.field input:focus,
.field select:focus,
.field textarea:focus {
  border-color: var(--accent-dark);
  box-shadow: 0 0 0 4px var(--accent-soft);
}

.checkbox-label {
  display: flex;
  min-height: 42px;
  align-items: center;
  gap: 9px;
  white-space: nowrap;
}

.checkbox-label input {
  width: 16px;
  height: 16px;
  accent-color: var(--accent-dark);
}

.form-message {
  grid-column: span 4;
  margin: 0;
  font-size: 13px;
}

.form-message--error {
  color: var(--danger);
}

.form-message--success {
  color: var(--success);
}

.form-actions {
  display: flex;
  grid-column: span 4;
  align-items: center;
  gap: 16px;
}

.primary-button {
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
  opacity: 0.65;
}

.secondary-button {
  padding: 9px 12px;
  border: 1px solid var(--line);
  border-radius: 8px;
  color: var(--ink);
  background: #fff;
  font-size: 13px;
}

.text-button {
  border: 0;
  color: var(--muted);
  background: transparent;
  font-size: 13px;
}

.list-section {
  margin-top: 46px;
}

.list-heading {
  align-items: center;
  margin-bottom: 16px;
}

.list-heading p {
  margin: 6px 0 0;
  color: var(--muted);
  font-size: 13px;
}

.services-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.service-row {
  display: grid;
  grid-template-columns: 44px minmax(0, 1fr) 130px auto;
  align-items: center;
  gap: 18px;
  padding: 18px;
}

.service-row--hidden {
  opacity: 0.6;
}

.service-order {
  display: grid;
  width: 35px;
  height: 35px;
  place-items: center;
  border-radius: 9px;
  color: var(--accent-dark);
  background: var(--accent-soft);
  font-size: 12px;
  font-weight: 800;
}

.service-title-line {
  display: flex;
  align-items: center;
  gap: 10px;
}

.service-content h3 {
  margin: 0;
  color: var(--ink);
  font-size: 15px;
}

.service-content p {
  margin: 5px 0 0;
  color: var(--muted);
  font-size: 13px;
  line-height: 1.45;
}

.service-price {
  color: var(--ink);
  font-size: 13.5px;
  font-weight: 750;
  text-align: right;
}

.row-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.icon-button {
  padding: 7px 9px;
  border: 1px solid var(--line);
  border-radius: 7px;
  color: var(--ink-soft);
  background: #fff;
  font-size: 12px;
}

.icon-button--danger {
  color: var(--danger);
}

.empty-state {
  padding: 30px;
  color: var(--muted);
  font-size: 14px;
  text-align: center;
}

@media screen and (max-width: 820px) {
  .admin-header {
    padding: 0 20px;
  }

  .admin-container {
    width: min(100% - 36px, 1120px);
    padding-top: 38px;
  }

  .service-form {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .field--wide,
  .form-message,
  .form-actions {
    grid-column: span 2;
  }

  .service-row {
    grid-template-columns: 40px minmax(0, 1fr);
  }

  .service-price {
    grid-column: 2;
    text-align: left;
  }

  .row-actions {
    grid-column: 2;
    justify-content: flex-start;
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

  .page-heading {
    flex-direction: column;
  }

  .service-form {
    grid-template-columns: 1fr;
  }

  .field--wide,
  .form-message,
  .form-actions {
    grid-column: span 1;
  }
}
</style>