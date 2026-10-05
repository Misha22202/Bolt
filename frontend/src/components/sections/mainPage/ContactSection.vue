<script setup>
import BaseButton from '@/components/uiBase/BaseButton.vue'
import FeatureIcon from '@/components/icons/FeatureIcon.vue'
import { useContactModal } from '@/composables/useContactModal.js'

const { open } = useContactModal()
</script>

<template>
  <section id="contact" class="contact-section">
    <div class="contact-panel">
      <div>
        <span class="contact-kicker">Нужен ремонт?</span>
        <h2>Расскажите, что случилось с техникой</h2>
        <p>
          Оставьте заявку — уточним детали и сориентируем по дальнейшим действиям.
        </p>
      </div>

      <div class="contact-actions">
        <BaseButton variant="accent" @click="open">
          <FeatureIcon name="diagnostic" :size="18" />
          Записаться на диагностику
        </BaseButton>

        <a
            class="telegram-link"
            href="https://t.me/perviyvint"
            target="_blank"
            rel="noopener noreferrer"
        >
          Написать в Telegram →
        </a>
      </div>
    </div>
  </section>
</template>

<style scoped>
.contact-section {
  padding: 84px 0 0;
}

.contact-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 32px;
  padding: 46px 48px;
  overflow: hidden;
  border-radius: calc(var(--radius) + 4px);
  color: #fff;
  background: var(--card-dark);
}

.contact-kicker {
  display: block;
  margin-bottom: 8px;
  color: var(--accent);
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 1.4px;
  text-transform: uppercase;
}

.contact-panel h2 {
  max-width: 600px;
  margin: 0 0 10px;
  color: #fff;
  font-size: 29px;
  line-height: 1.15;
  font-weight: 850;
}

.contact-panel p {
  max-width: 540px;
  margin: 0;
  color: rgba(255,255,255,0.66);
  font-size: 14.5px;
}

.contact-actions {
  display: flex;
  flex: 0 0 auto;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.telegram-link {
  color: rgba(255,255,255,0.72);
  font-size: 13.5px;
}

.telegram-link:hover {
  color: var(--accent);
}

@media screen and (max-width: 768px) {
  .contact-section {
    padding-top: 56px;
  }

  .contact-panel {
    align-items: flex-start;
    flex-direction: column;
    padding: 30px 24px;
  }

  .contact-panel h2 {
    font-size: 24px;
  }

  .contact-actions {
    align-items: stretch;
    width: 100%;
  }

  .contact-actions :deep(.btn) {
    width: 100%;
  }

  .telegram-link {
    text-align: center;
  }
}
</style>