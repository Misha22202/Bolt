<script setup>
import { onMounted, ref } from 'vue'
import FeatureIcon from '@/components/icons/FeatureIcon.vue'

const contactsInfo = ref(null)
const isLoading = ref(true)
const loadError = ref('')

async function loadContactsInfo() {
  isLoading.value = true
  loadError.value = ''

  try {
    const response = await fetch('/api/public/contacts')

    if (!response.ok) {
      throw new Error('Не удалось загрузить контакты')
    }

    contactsInfo.value = await response.json()
  } catch (error) {
    loadError.value =
        error instanceof Error
            ? error.message
            : 'Не удалось загрузить контакты'
  } finally {
    isLoading.value = false
  }
}

function phoneDigitsOnly(phone) {
  return phone ? phone.replace(/\D/g, '') : ''
}

onMounted(loadContactsInfo)
</script>

<template>
  <section class="info-section">
    <div v-if="isLoading" class="info-status">
      Загружаем контакты…
    </div>

    <div v-else-if="loadError" class="info-status info-status--error">
      {{ loadError }}
    </div>

    <template v-else-if="contactsInfo">
      <div class="info-grid">
        <div class="info-card">
          <span class="tag">Адрес</span>
          <p class="info-value">{{ contactsInfo.address }}</p>
          <p v-if="contactsInfo.contactsInfoNote" class="info-note">
            {{ contactsInfo.contactsInfoNote }}
          </p>
        </div>

        <div class="info-card">
          <span class="tag">Телефон и связь</span>

          <a
              class="info-value info-link"
              :href="`tel:${phoneDigitsOnly(contactsInfo.phone)}`"
          >
            {{ contactsInfo.phone }}
          </a>

          <a
              v-if="contactsInfo.telegramUrl"
              class="info-note info-link"
              :href="contactsInfo.telegramUrl"
              target="_blank"
              rel="noopener noreferrer"
          >
            Написать в Telegram →
          </a>
        </div>

        <div class="info-card">
          <span class="tag">
            <FeatureIcon name="speed" :size="14" />
            График работы
          </span>

          <ul class="hours-list">
            <li
                v-for="(item, index) in contactsInfo.workingHours"
                :key="index"
            >
              <span>{{ item.day }}</span>
              <b>{{ item.time }}</b>
            </li>
          </ul>
        </div>
      </div>
    </template>
  </section>
</template>

<style scoped>
.info-section {
  padding: 48px 0 0;
}
.info-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}
.info-card {
  background: var(--card);
  border: 1px solid var(--line);
  border-radius: var(--radius);
  padding: 26px;
}
.tag {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  font-weight: 750;
  letter-spacing: 1.4px;
  text-transform: uppercase;
  color: var(--accent-dark);
}
.info-value {
  display: block;
  margin: 12px 0 6px;
  font-size: 17px;
  font-weight: 700;
  color: var(--ink);
}
.info-note {
  display: block;
  margin: 0;
  font-size: 13.5px;
  color: var(--muted);
}
.info-link:hover {
  color: var(--accent-dark);
}
.hours-list {
  list-style: none;
  padding: 0;
  margin: 14px 0 0;
}
.hours-list li {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding: 8px 0;
  border-bottom: 1px solid var(--line);
  font-size: 14px;
}
.hours-list li:last-child {
  border-bottom: none;
}
.info-status {
  padding: 26px;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  background: var(--card);
  color: var(--muted);
  font-size: 14px;
  text-align: center;
}
.info-status--error {
  color: var(--danger);
}

@media screen and (max-width: 900px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>