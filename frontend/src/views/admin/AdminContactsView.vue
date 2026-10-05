<script setup>
import { useRouter } from 'vue-router'
import { onMounted, reactive, ref } from 'vue'
import { adminFetch } from '@/api/adminApi.js'
import AdminHeader from '@/components/admin/AdminHeader.vue'

const router = useRouter()

const isLoading = ref(true)
const isSaving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const emptyForm = {
  phone: '',
  address: '',
  telegramUrl: '',
  workingHours: [
    { day: '', time: '' }
  ],
  contactsInfoNote: ''
}

const form = reactive({
  ...emptyForm
})

function resetMessages() {
  errorMessage.value = ''
  successMessage.value = ''
}

async function loadContacts() {
  isLoading.value = true
  resetMessages()

  try {
    const response = await adminFetch('/api/admin/contacts')

    if (response.status === 401) {
      await router.replace('/admin/login')
      return
    }

    if (!response.ok) {
      throw new Error('Не удалось загрузить контакты')
    }

    const data = await response.json()

    Object.assign(form, {
      phone: data.phone ?? '',
      address: data.address ?? '',
      telegramUrl: data.telegramUrl ?? '',
      workingHours: data.workingHours?.length
          ? data.workingHours.map((line) => ({ ...line }))
          : [{ day: '', time: '' }],
      contactsInfoNote: data.contactsInfoNote ?? ''
    })
  } catch (error) {
    errorMessage.value =
        error instanceof Error ? error.message : 'Не удалось загрузить контакты'
  } finally {
    isLoading.value = false
  }
}

function addWorkingHoursLine() {
  form.workingHours.push({ day: '', time: '' })
}

function removeWorkingHoursLine(index) {
  form.workingHours.splice(index, 1)

  if (form.workingHours.length === 0) {
    form.workingHours.push({ day: '', time: '' })
  }
}

async function saveContacts() {
  resetMessages()

  const cleanedWorkingHours = form.workingHours
      .map((line) => ({
        day: line.day.trim(),
        time: line.time.trim()
      }))
      .filter((line) => line.day.length > 0 && line.time.length > 0)

  if (!form.phone.trim()) {
    errorMessage.value = 'Укажите телефон'
    return
  }

  if (!form.address.trim()) {
    errorMessage.value = 'Укажите адрес'
    return
  }

  if (!cleanedWorkingHours.length) {
    errorMessage.value = 'Укажите рабочие часы'
    return
  }

  if (!form.telegramUrl.trim()) {
    errorMessage.value = 'Укажите адрес Telegram'
    return
  }

  isSaving.value = true

  try {
    const response = await adminFetch('/api/admin/contacts', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        phone: form.phone.trim(),
        address: form.address.trim(),
        telegramUrl: form.telegramUrl.trim() || null,
        workingHours: cleanedWorkingHours,
        contactsInfoNote: form.contactsInfoNote.trim() || null
      })
    })

    if (response.status === 401) {
      await router.replace('/admin/login')
      return
    }

    if (!response.ok) {
      throw new Error('Не удалось сохранить контакты')
    }

    successMessage.value = 'Контакты сохранены'

    form.workingHours = cleanedWorkingHours.length
        ? cleanedWorkingHours
        : [{ day: '', time: '' }]
  } catch (error) {
    errorMessage.value =
        error instanceof Error ? error.message : 'Не удалось сохранить контакты'
  } finally {
    isSaving.value = false
  }
}

onMounted(loadContacts)
</script>

<template>
  <main class="admin-page">
    <AdminHeader />

    <div class="admin-container">
      <section class="page-heading">
        <div>
          <span class="eyebrow">Содержимое сайта</span>
          <h1>Контакты</h1>
          <p>
            Эти данные показываются в подвале сайта, на кнопке связи
            и в модальном окне заявки.
          </p>
        </div>
      </section>

      <section v-if="isLoading" class="empty-state">
        Загружаем контакты…
      </section>

      <section v-else class="editor-card">
        <form class="contacts-form" @submit.prevent="saveContacts">
          <div class="field">
            <label for="contact-phone">
              Телефон <span>*</span>
            </label>

            <input
                id="contact-phone"
                v-model="form.phone"
                type="text"
                maxlength="40"
                placeholder="+7 (900) 000-00-00"
                :disabled="isSaving"
                required
            >
          </div>

          <div class="field">
            <label for="contact-telegram">
              Telegram <span>*</span>
            </label>

            <input
                id="contact-telegram"
                v-model="form.telegramUrl"
                type="url"
                maxlength="255"
                placeholder="https://t.me/perviyvint"
                :disabled="isSaving"
                required
            >
          </div>

          <div class="field field--wide">
            <label for="contact-address">
              Адрес <span>*</span>
            </label>

            <input
                id="contact-address"
                v-model="form.address"
                type="text"
                maxlength="255"
                placeholder="г. Воронеж, ул. Примерная, 1"
                :disabled="isSaving"
                required
            >
          </div>

          <div class="field field--wide">
            <label>
              График работы <span>*</span>
            </label>

            <div
                v-for="(line, index) in form.workingHours"
                :key="index"
                class="working-hours-row"
            >
              <input
                  v-model="line.day"
                  type="text"
                  maxlength="100"
                  placeholder="Например: Понедельник"
                  :disabled="isSaving"
              >

              <input
                  v-model="line.time"
                  type="text"
                  maxlength="100"
                  placeholder="Например: 11:00 — 20:00"
                  :disabled="isSaving"
              >

              <button
                  class="icon-button icon-button--danger"
                  type="button"
                  :disabled="isSaving || form.workingHours.length === 1"
                  @click="removeWorkingHoursLine(index)"
              >
                Удалить
              </button>
            </div>

            <button
                class="secondary-button"
                type="button"
                :disabled="isSaving"
                @click="addWorkingHoursLine"
            >
              Добавить строку
            </button>
          </div>

          <div class="field field--wide">
            <label for="contact-info-note">Дополнительная заметка</label>

            <textarea
                id="contact-info-note"
                v-model="form.contactsInfoNote"
                rows="3"
                maxlength="500"
                placeholder="Например: В праздничные дни график может отличаться"
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
              {{ isSaving ? 'Сохраняем…' : 'Сохранить изменения' }}
            </button>

            <button
                class="text-button"
                type="button"
                :disabled="isSaving || isLoading"
                @click="loadContacts"
            >
              Отменить и загрузить заново
            </button>
          </div>
        </form>
      </section>
    </div>
  </main>
</template>

<style scoped>
.admin-page {
  min-height: 100dvh;
  background: var(--bg);
}

.admin-container {
  width: min(100% - 48px, 820px);
  margin: 0 auto;
  padding: 54px 0 72px;
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
  max-width: 620px;
  margin: 0;
  color: var(--ink-soft);
  font-size: 14.5px;
  line-height: 1.6;
}

.editor-card,
.empty-state {
  border: 1px solid var(--line);
  border-radius: var(--radius);
  background: var(--card);
}

.editor-card {
  padding: 28px;
  box-shadow: 0 10px 24px rgba(17, 17, 19, 0.035);
}

.contacts-form {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.field--wide {
  grid-column: span 2;
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
.field textarea:focus {
  border-color: var(--accent-dark);
  box-shadow: 0 0 0 4px var(--accent-soft);
}

.field input:disabled,
.field textarea:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.working-hours-row {
  display: grid;
  grid-template-columns: 1fr 1fr auto;
  gap: 10px;
  margin-bottom: 10px;
}

.icon-button,
.secondary-button {
  cursor: pointer;
  font: inherit;
}

.icon-button {
  padding: 10px 12px;
  border: 1px solid var(--line);
  border-radius: 9px;
  color: var(--ink-soft);
  background: #fff;
  font-size: 12.5px;
  white-space: nowrap;
}

.icon-button--danger {
  color: var(--danger);
}

.icon-button:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

.secondary-button {
  padding: 9px 12px;
  border: 1px solid var(--line);
  border-radius: 8px;
  color: var(--ink);
  background: #fff;
  font-size: 13px;
}

.secondary-button:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

.form-message {
  grid-column: span 2;
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
  grid-column: span 2;
  align-items: center;
  gap: 16px;
}

.primary-button {
  padding: 12px 17px;
  border: 0;
  border-radius: 9px;
  cursor: pointer;
  color: var(--accent-dark);
  background: var(--accent);
  font: inherit;
  font-size: 13.5px;
  font-weight: 800;
}

.primary-button:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.text-button {
  border: 0;
  color: var(--muted);
  background: transparent;
  cursor: pointer;
  font: inherit;
  font-size: 13px;
}

.text-button:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.empty-state {
  padding: 30px;
  color: var(--muted);
  font-size: 14px;
  text-align: center;
}

@media screen and (max-width: 680px) {
  .admin-container {
    width: min(100% - 36px, 820px);
    padding-top: 38px;
  }

  .contacts-form {
    grid-template-columns: 1fr;
  }

  .field--wide,
  .form-message,
  .form-actions {
    grid-column: span 1;
  }

  .working-hours-row {
    grid-template-columns: 1fr;
  }
}
</style>