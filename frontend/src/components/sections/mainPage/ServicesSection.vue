<script setup>
import { onMounted, ref } from 'vue'

import ServiceRow from '@/components/sections/mainPage/ServiceRow.vue'

const services = ref([])
const isLoading = ref(true)
const loadError = ref('')

async function loadServices() {
  isLoading.value = true
  loadError.value = ''

  try {
    const response = await fetch('/api/public/services')

    if (!response.ok) {
      throw new Error('Не удалось загрузить услуги.')
    }

    services.value = await response.json()
  } catch (error) {
    loadError.value =
        error instanceof Error
            ? error.message
            : 'Не удалось загрузить услуги.'
  } finally {
    isLoading.value = false
  }
}

onMounted(loadServices)
</script>

<template>
  <section id="services">
    <div class="section-head">
      <div>
        <span class="tag">Услуги</span>
        <h2>Чиним быстро и по делу</h2>
        <p>Прозрачные цены и понятные сроки ремонта.</p>
      </div>
    </div>

    <div v-if="isLoading" class="services-status">
      Загружаем услуги…
    </div>

    <div v-else-if="loadError" class="services-status services-status--error">
      {{ loadError }}
    </div>

    <div v-else-if="!services.length" class="services-status">
      Услуги пока не добавлены.
    </div>

    <div v-else class="services-row">
      <ServiceRow
          v-for="item in services"
          :key="item.id"
          :name="item.name"
          :desc="item.description"
          :price="item.price"
          :icon="item.icon"
      />
    </div>
  </section>
</template>

<style scoped>
section {
  padding: 64px 0;
}

.section-head {
  margin-bottom: 20px;
}

.section-head h2 {
  margin: 0;
  font-size: 28px;
  font-weight: 800;
  letter-spacing: -0.3px;
}

.section-head p {
  margin: 8px 0 0;
  color: var(--muted);
  font-size: 14.5px;
  max-width: 420px;
}

.tag {
  font-size: 12px;
  letter-spacing: 1.5px;
  text-transform: uppercase;
  color: var(--muted);
}

.services-row {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  background: var(--card);
}

.services-status {
  padding: 26px;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  background: var(--card);
  color: var(--muted);
  font-size: 14px;
  text-align: center;
}

.services-status--error {
  color: var(--danger);
}

@media screen and (max-width: 768px) {
  section {
    padding: 64px 0 0;
  }
}
</style>