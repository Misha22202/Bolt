<script setup>
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

import ScrewIcon from '@/components/icons/ScrewIcon.vue'

const router = useRouter()

const form = reactive({
  username: '',
  password: ''
})

const status = ref('idle')
// idle | loading | error

const errorMessage = ref('')

const isLoading = computed(() => status.value === 'loading')

async function submitLogin() {
  if (!form.username.trim() || !form.password) {
    status.value = 'error'
    errorMessage.value = 'Введите логин и пароль.'
    return
  }

  status.value = 'loading'
  errorMessage.value = ''

  try {
    const response = await fetch('/api/admin/auth/login', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: form.username.trim(),
        password: form.password
      })
    })

    if (response.status === 401) {
      throw new Error('Неверный логин или пароль.')
    }

    if (!response.ok) {
      throw new Error('Не удалось выполнить вход. Попробуйте ещё раз.')
    }

    const result = await response.json()

    if (!result.authenticated) {
      throw new Error('Не удалось подтвердить авторизацию.')
    }

    form.password = ''

    await router.replace('/admin')
  } catch (error) {
    status.value = 'error'
    errorMessage.value =
        error instanceof Error
            ? error.message
            : 'Не удалось выполнить вход. Попробуйте ещё раз.'
  } finally {
    if (status.value !== 'error') {
      status.value = 'idle'
    }
  }
}
</script>

<template>
  <main class="admin-login-page">
    <section class="login-card" aria-labelledby="login-title">
      <router-link to="/" class="brand" aria-label="Вернуться на сайт">
        <span class="brand-icon">
          <ScrewIcon :size="30" color="var(--accent)" />
        </span>

        <span class="brand-text">
          <b>ПЕРВЫЙ ВИНТ</b>
          <small>Панель управления</small>
        </span>
      </router-link>

      <div class="login-heading">
        <span class="eyebrow">Администрирование</span>
        <h1 id="login-title">Вход в админку</h1>
        <p>
          Введите данные администратора, заданные в настройках backend.
        </p>
      </div>

      <form class="login-form" @submit.prevent="submitLogin">
        <div class="field">
          <label for="admin-username">Логин</label>

          <input
              id="admin-username"
              v-model="form.username"
              type="text"
              name="username"
              autocomplete="username"
              placeholder="admin"
              :disabled="isLoading"
              required
          >
        </div>

        <div class="field">
          <label for="admin-password">Пароль</label>

          <input
              id="admin-password"
              v-model="form.password"
              type="password"
              name="password"
              autocomplete="current-password"
              placeholder="Введите пароль"
              :disabled="isLoading"
              required
          >
        </div>

        <p
            v-if="status === 'error'"
            class="form-message form-message--error"
            role="alert"
        >
          {{ errorMessage }}
        </p>

        <button
            class="login-button"
            type="submit"
            :disabled="isLoading"
        >
          {{ isLoading ? 'Входим…' : 'Войти' }}
        </button>
      </form>

      <p class="login-note">
        Сессия сохраняется до 8 часов. Выйти можно будет из панели управления.
      </p>
    </section>
  </main>
</template>

<style scoped>
.admin-login-page {
  display: grid;
  min-height: 100dvh;
  place-items: center;
  padding: 24px;
  background:
      radial-gradient(
          circle at 18% 18%,
          var(--accent-soft),
          transparent 30%
      ),
      var(--bg);
}

.login-card {
  width: min(100%, 430px);
  padding: 38px;
  border: 1px solid var(--line);
  border-radius: calc(var(--radius) + 4px);
  background: rgba(255, 255, 255, 0.94);
  box-shadow:
      0 24px 60px rgba(17, 17, 19, 0.10),
      0 2px 8px rgba(17, 17, 19, 0.04);
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: 11px;
  margin-bottom: 38px;
}

.brand-icon {
  display: grid;
  width: 44px;
  height: 44px;
  place-items: center;
  border-radius: 12px;
  background: var(--card-dark);
}

.brand-text {
  display: flex;
  flex-direction: column;
  line-height: 1.1;
}

.brand-text b {
  color: var(--ink);
  font-size: 15px;
  letter-spacing: 0.7px;
}

.brand-text small {
  margin-top: 4px;
  color: var(--muted);
  font-size: 10px;
  letter-spacing: 1.4px;
  text-transform: uppercase;
}

.login-heading {
  margin-bottom: 26px;
}

.eyebrow {
  display: inline-block;
  margin-bottom: 8px;
  color: var(--accent-dark);
  font-size: 11px;
  font-weight: 800;
  letter-spacing: 1.3px;
  text-transform: uppercase;
}

.login-heading h1 {
  margin: 0 0 10px;
  color: var(--ink);
  font-size: 30px;
  line-height: 1.1;
  letter-spacing: -0.5px;
}

.login-heading p {
  margin: 0;
  color: var(--muted);
  font-size: 14px;
  line-height: 1.55;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 17px;
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

.field input {
  width: 100%;
  box-sizing: border-box;
  padding: 12px 13px;
  border: 1px solid var(--line);
  border-radius: 9px;
  outline: none;
  color: var(--ink);
  background: #fff;
  font: inherit;
  font-size: 14.5px;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.field input::placeholder {
  color: #a4a4aa;
}

.field input:focus {
  border-color: var(--accent-dark);
  box-shadow: 0 0 0 4px var(--accent-soft);
}

.field input:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.login-button {
  width: 100%;
  margin-top: 3px;
  padding: 13px 20px;
  border: 0;
  border-radius: 10px;
  cursor: pointer;
  color: var(--accent-dark);
  background: var(--accent);
  font: inherit;
  font-size: 14.5px;
  font-weight: 800;
  transition: transform 0.18s ease, background 0.18s ease;
}

.login-button:hover:not(:disabled) {
  background: var(--accent-hover);
  transform: translateY(-1px);
}

.login-button:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.form-message {
  margin: -3px 0 0;
  font-size: 13px;
  line-height: 1.45;
}

.form-message--error {
  color: var(--danger);
}

.login-note {
  margin: 23px 0 0;
  padding-top: 18px;
  border-top: 1px solid var(--line);
  color: var(--muted);
  font-size: 12.5px;
  line-height: 1.45;
}

@media screen and (max-width: 520px) {
  .admin-login-page {
    padding: 16px;
  }

  .login-card {
    padding: 27px 22px;
  }

  .brand {
    margin-bottom: 30px;
  }

  .login-heading h1 {
    font-size: 27px;
  }
}
</style>