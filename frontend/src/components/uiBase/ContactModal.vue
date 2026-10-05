<script setup>
import { reactive, ref, watch } from "vue"
import { useContactModal } from "@/composables/useContactModal.js"

const SCRIPT_URL = 'https://script.google.com/macros/s/AKfycbwhAWsX4JBU8CyaalGMWAwcZ8CR1e-RTe7h_y4SUgwQ40RasoKLADISdAhsmOvEVX2j/exec'

const { state, close } = useContactModal()

const form  = reactive({
  name: '',
  phone: '',
  description: '',
})

const status = ref('idle') // idle | loading | success | error

async function handleSubmit() {
  if (!form.name || !form.phone) {
    status.value = 'error'
    return
  }

  status.value = 'loading'

  try {
    await fetch(SCRIPT_URL, {
      method: 'POST',
      body: JSON.stringify(form)
    })

    status.value = 'success'
    form.name = ''
    form.phone = ''
    form.description = ''
  } catch (error) {
    status.value = 'error'
  }
}

function handleOverlayClick() {
  close()
}

function handleEsc(e) {
  if (e.key === 'Escape') {
    close()
  }
}

watch(() => state.isOpen, (isOpen) => {
  if (isOpen) {
    status.value = 'idle'
    document.addEventListener('keydown', handleEsc)
    document.body.style.overflowY = 'hidden'
  } else {
    document.removeEventListener('keydown', handleEsc)
    document.body.style.overflowY = ''
  }
})
</script>

<template>
<Teleport to="body">
  <Transition name="modal-fade">
    <div v-if="state.isOpen" class="modal-overlay" @click.self="handleOverlayClick">
      <div class="modal-card">
        <button class="modal-close" aria-label="Закрыть" @click="close">✕</button>

        <h3>Записаться на диагностику</h3>
        <p class="modal-lead">Оставьте контакты, чтобы мы могли связаться с вами</p>

        <form class="contact-form" @submit.prevent="handleSubmit">
          <div class="field">
            <label for="modal-name">Имя <span class="required">*</span></label>
            <input id="modal-name" v-model="form.name" type="text" placeholder="Как к вам обращаться" required />
          </div>

          <div class="field">
            <label for="modal-phone">Телефон <span class="required">*</span></label>
            <input id="modal-phone" v-model="form.phone" type="tel" placeholder="+7 900 000-00-00" required />
          </div>

          <div class="field">
            <label for="modal-description">Че стало лееее</label>
            <textarea
              id="modal-description"
              v-model="form.description"
              rows="3"
              placeholder="Кратко опишите че стало леее (необязательно)">
            </textarea>
          </div>

          <button type="submit" class="submit-btn" :disabled="status === 'loading'">
            {{ status === 'loading' ? 'Отправляем...' : 'Отправить заявку' }}
          </button>

          <p v-if="status === 'success'" class="form-msg success">
            Заявка отправлена, скоро с вами свяжутся
          </p>
          <p v-if="status === 'error'" class="form-msg error">
            Пожалуйста, заполните имя и телефон, без них отправить заявку не получится
          </p>
        </form>
      </div>
    </div>
  </Transition>
</Teleport>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(17, 17, 19, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
  padding: 20px;
}
.modal-card {
  background: var(--card);
  border-radius: var(--radius);
  padding: 36px;
  max-width: 420px;
  width: 100%;
  position: relative;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.25);
}
.modal-close {
  position: absolute;
  top: 18px;
  right: 18px;
  background: transparent;
  border: none;
  font-size: 16px;
  color: var(--muted);
  cursor: pointer;
  line-height: 1;
}
.modal-close:hover {
  color: var(--ink);
}
.modal-card h3 {
  font-size: 22px;
  font-weight: 800;
  margin: 0 0 8px;
}
.modal-lead {
  font-size: 14px;
  color: var(--muted);
  margin: 0 0 24px;
}
.contact-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.field label {
  font-size: 13px;
  color: var(--muted);
}
.field label .required {
  color: #d33;
}
.field input,
.field textarea {
  border: 1px solid var(--line);
  border-radius: 8px;
  padding: 11px 14px;
  font-size: 14.5px;
  font-family: inherit;
  color: var(--ink);
  background: #fff;
}
.field textarea {
  resize: none;
}
.field input:focus,
.field textarea:focus {
  outline: none;
  border-color: var(--ink);
}

.submit-btn {
  background: var(--ink);
  color: #fff;
  border: none;
  border-radius: 10px;
  padding: 13px 26px;
  font-size: 14.5px;
  font-weight: 600;
  cursor: pointer;
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.form-msg {
  font-size: 13.5px;
  margin: 0;
}
.form-msg.success {
  color: #1fae5a;
}
.form-msg.error {
  color: #d33;
}

@media screen and (max-width: 768px) {
  .modal-card {
    padding: 26px 22px;
    max-height: 90vh;
    overflow-y: auto;
  }
  .modal-card h3 {
    font-size: 19px;
  }
}

.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.2s;
}
.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}
</style>