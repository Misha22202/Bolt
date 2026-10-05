<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

import AdminHeader from "@/components/admin/AdminHeader.vue";

const router = useRouter()

const adminUsername = ref('')
const status = ref('loading')
// loading | ready | error

const errorMessage = ref('')

onMounted(async () => {
  try {
    const response = await fetch('/api/admin/auth/me', {
      credentials: 'include'
    })

    if (response.status === 401) {
      await router.replace('/admin/login')
      return
    }

    if (!response.ok) {
      throw new Error('Не удалось проверить сессию администратора.')
    }

    const result = await response.json()

    adminUsername.value = result.username
    status.value = 'ready'
  } catch (error) {
    status.value = 'error'
    errorMessage.value =
        error instanceof Error
            ? error.message
            : 'Не удалось загрузить панель управления.'
  }
})
</script>

<template>
  <main class="admin-page">
    <AdminHeader/>

    <section v-if="status === 'loading'" class="admin-container">
      <div class="status-card">
        Проверяем авторизацию…
      </div>
    </section>

    <section v-else-if="status === 'error'" class="admin-container">
      <div class="status-card status-card--error">
        {{ errorMessage }}
      </div>
    </section>

    <section v-else class="admin-container">
      <div class="admin-intro">
        <span class="eyebrow">Панель управления</span>

        <h1>Здравствуйте, {{ adminUsername }}.</h1>

        <p>
          Авторизация активна. Здесь появится управление услугами,
          текстами сайта, работами, фотографиями и заявками.
        </p>
      </div>

      <div class="admin-grid">
        <article class="admin-card">
          <span class="card-number">01</span>
          <h2>Тексты сайта</h2>
          <p>
            Заголовки, описания, контакты, график и содержимое секций.
          </p>
        </article>

        <a :href="'/admin/services/'">
        <article class="admin-card">
          <span class="card-number">02</span>
          <h2>Услуги</h2>
          <p>
            Добавление, редактирование, скрытие и сортировка услуг.
          </p>
        </article>
        </a>

        <a :href="'/admin/contacts/'">
        <article class="admin-card">
          <span class="card-number">03</span>
          <h2>Контакты</h2>
          <p>
            Адрес, номер телефона, url telegram, рабочие часы
          </p>
        </article>
        </a>

        <article class="admin-card">
          <span class="card-number">04</span>
          <h2>Заявки</h2>
          <p>
            Просмотр обращений клиентов и изменение их статусов.
          </p>
        </article>
      </div>
    </section>
  </main>
</template>

<style scoped>
.admin-page {
  min-height: 100dvh;
  background: var(--bg);
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

.admin-container {
  width: min(100% - 48px, 1080px);
  margin: 0 auto;
  padding: 62px 0;
}

.admin-intro {
  max-width: 680px;
  margin-bottom: 34px;
}

.eyebrow {
  display: inline-block;
  margin-bottom: 10px;
  color: var(--accent-dark);
  font-size: 11px;
  font-weight: 800;
  letter-spacing: 1.4px;
  text-transform: uppercase;
}

.admin-intro h1 {
  margin: 0 0 12px;
  color: var(--ink);
  font-size: clamp(30px, 4vw, 42px);
  line-height: 1.08;
  letter-spacing: -1px;
}

.admin-intro p {
  margin: 0;
  color: var(--ink-soft);
  font-size: 15px;
  line-height: 1.65;
}

.admin-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.admin-card,
.status-card {
  padding: 26px;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  background: var(--card);
  box-shadow: 0 8px 20px rgba(17, 17, 19, 0.03);
  transition: all 0.3s ease;
}

.admin-card:hover {
  transform: scale(1.05);
  transition: all 0.3s ease;
}

.admin-card {
  display: flex;
  min-height: 182px;
  flex-direction: column;
}

.card-number {
  margin-bottom: 18px;
  color: var(--accent-dark);
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 1.2px;
}

.admin-card h2 {
  margin: 0 0 9px;
  color: var(--ink);
  font-size: 19px;
}

.admin-card p {
  margin: 0;
  color: var(--muted);
  font-size: 14px;
  line-height: 1.5;
}

.card-status {
  margin-top: auto;
  padding-top: 22px;
  color: var(--accent-dark);
  font-size: 12px;
  font-weight: 700;
}

.status-card {
  color: var(--ink-soft);
  font-size: 14px;
}

.status-card--error {
  border-color: rgba(221, 51, 51, 0.35);
  color: var(--danger);
}

@media screen and (max-width: 680px) {
  .admin-header {
    min-height: 64px;
    padding: 0 20px;
  }

  .admin-container {
    width: min(100% - 36px, 1080px);
    padding: 42px 0;
  }

  .admin-grid {
    grid-template-columns: 1fr;
  }
}
</style>