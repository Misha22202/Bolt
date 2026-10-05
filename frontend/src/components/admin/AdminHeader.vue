<script setup>
import { useRouter } from 'vue-router'

import ScrewIcon from '@/components/icons/ScrewIcon.vue'
import { adminFetch } from '@/api/adminApi.js'

const router = useRouter()

async function logout() {
  try {
    await adminFetch('/api/admin/auth/logout', {
      method: 'POST'
    })
  } finally {
    await router.replace('/admin/login')
  }
}
</script>

<template>
  <header class="admin-header">
    <router-link to="/admin" class="admin-brand">
      <ScrewIcon :size="28" color="var(--accent)" />

      <span>
        <b>ПЕРВЫЙ ВИНТ</b>
        <small>Админинистративная панель</small>
      </span>
    </router-link>

    <nav class="admin-nav" aria-label="Навигация админки">
      <router-link to="/admin">Обзор</router-link>
      <router-link to="/admin/services">Услуги</router-link>
      <router-link to="/admin/media">Фото</router-link>
      <router-link to="/admin/contacts">Контакты</router-link>
    </nav>

    <button class="logout-button" type="button" @click="logout">
      <span class="logout-button--icon">×</span> Выйти
    </button>
  </header>
</template>

<style scoped>
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
  width: 100%;
  justify-content: center;
  flex-direction: row;
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

.logout-button {
  padding: 8px 12px;
  border: 1px solid rgba(255, 255, 255, 0.22);
  border-radius: 8px;
  cursor: pointer;
  color: #fff;
  background: transparent;
  font: inherit;
  font-size: 13px;
  transition: border-color 0.2s ease, background 0.2s ease;
  width: 238px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  column-gap: 4px;
}

.logout-button--icon {
  font-size: 24px;
}

.logout-button:hover {
  border-color: var(--accent);
  background: rgba(255, 255, 255, 0.08);
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
}
</style>