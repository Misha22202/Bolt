<script setup>
import { computed, reactive, watch } from 'vue'

import BaseButton from '@/components/uiBase/BaseButton.vue'

const props = defineProps({
  service: {
    type: Object,
    default: null
  },
  isSaving: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['save', 'cancel'])

const isEditing = computed(() => props.service !== null)

const form = reactive({
  name: '',
  description: '',
  price: '',
  icon: 'tools',
  sortOrder: 0,
  isVisible: true
})

function setFormValues(service) {
  form.name = service?.name || ''
  form.description = service?.description || ''
  form.price = service?.price || ''
  form.icon = service?.icon || 'tools'
  form.sortOrder = service?.sortOrder ?? 0
  form.isVisible = service?.isVisible ?? true
}

watch(
    () => props.service,
    (service) => setFormValues(service),
    { immediate: true }
)

function submit() {
  emit('save', {
    name: form.name.trim(),
    description: form.description.trim(),
    price: form.price.trim(),
    icon: form.icon.trim() || 'tools',
    sortOrder: Number(form.sortOrder),
    isVisible: form.isVisible
  })
}
</script>

<template>
  <form class="service-editor" @submit.prevent="submit">
    <div class="service-editor__head">
      <div>
        <h2>{{ isEditing ? 'Редактировать услугу' : 'Новая услуга' }}</h2>
        <p>Поля со звёздочкой обязательны для сохранения.</p>
      </div>

      <button
          v-if="isEditing"
          class="service-editor__close"
          type="button"
          aria-label="Закрыть форму редактирования"
          @click="$emit('cancel')"
      >
        ×
      </button>
    </div>

    <label class="admin-field">
      <span>Название *</span>
      <input
          v-model="form.name"
          type="text"
          maxlength="255"
          required
          placeholder="Например: Ремонт пылесосов"
      >
    </label>

    <label class="admin-field">
      <span>Описание</span>
      <textarea
          v-model="form.description"
          rows="4"
          maxlength="3000"
          placeholder="Что входит в услугу"
      ></textarea>
    </label>

    <div class="service-editor__row">
      <label class="admin-field">
        <span>Цена</span>
        <input
            v-model="form.price"
            type="text"
            maxlength="100"
            placeholder="Например: от 500 ₽"
        >
      </label>

      <label class="admin-field">
        <span>Иконка</span>
        <input
            v-model="form.icon"
            type="text"
            maxlength="50"
            required
            placeholder="tools"
        >
      </label>
    </div>

    <div class="service-editor__row">
      <label class="admin-field">
        <span>Порядок показа</span>
        <input
            v-model.number="form.sortOrder"
            type="number"
            min="0"
            step="1"
        >
      </label>

      <label class="admin-checkbox">
        <input v-model="form.isVisible" type="checkbox">
        <span>Показывать на сайте</span>
      </label>
    </div>

    <div class="service-editor__actions">
      <BaseButton type="submit" variant="primary" :disabled="isSaving">
        {{ isSaving ? 'Сохраняем…' : 'Сохранить' }}
      </BaseButton>

      <BaseButton
          v-if="isEditing"
          type="button"
          variant="secondary"
          :disabled="isSaving"
          @click="$emit('cancel')"
      >
        Отмена
      </BaseButton>
    </div>
  </form>
</template>

<style scoped>
.service-editor {
  display: grid;
  gap: 18px;
  padding: 24px;
  border: 1px solid #2a2a2d;
  border-radius: 18px;
  background: #1b1b1e;
}

.service-editor__head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
}

.service-editor__head h2 {
  margin: 0;
  font-size: 20px;
}

.service-editor__head p {
  margin: 6px 0 0;
  color: #9a9aa0;
  font-size: 14px;
}

.service-editor__close {
  width: 34px;
  height: 34px;
  border: 0;
  border-radius: 8px;
  color: #fff;
  background: #2a2a2d;
  font-size: 26px;
  line-height: 1;
  cursor: pointer;
}

.service-editor__row {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.admin-field {
  display: grid;
  gap: 7px;
}

.admin-field span {
  color: #d6d6da;
  font-size: 14px;
  font-weight: 600;
}

.admin-field input,
.admin-field textarea {
  box-sizing: border-box;
  width: 100%;
  border: 1px solid #3a3a3f;
  border-radius: 10px;
  outline: none;
  padding: 11px 12px;
  color: #f4f4f5;
  background: #121214;
  font: inherit;
}

.admin-field textarea {
  resize: vertical;
}

.admin-field input:focus,
.admin-field textarea:focus {
  border-color: var(--accent);
}

.admin-checkbox {
  display: flex;
  align-items: center;
  gap: 10px;
  align-self: end;
  min-height: 44px;
  color: #d6d6da;
  cursor: pointer;
}

.admin-checkbox input {
  width: 17px;
  height: 17px;
  accent-color: var(--accent);
}

.service-editor__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

@media (max-width: 600px) {
  .service-editor__row {
    grid-template-columns: 1fr;
  }
}
</style>