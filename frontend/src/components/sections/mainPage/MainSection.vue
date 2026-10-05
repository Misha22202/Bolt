<script setup>
import BaseButton from '@/components/uiBase/BaseButton.vue'
import FeatureIcon from "@/components/icons/FeatureIcon.vue";
import { useContactModal } from "@/composables/useContactModal.js"

const { open } = useContactModal()

import mainImage from '@/assets/images/main/workshop-main.jpeg'

const mainStyle = {
  backgroundImage: `url(${mainImage})`,
}
</script>

<template>
  <section class="main main--photo" :style="mainStyle">
    <div class="main-content">

      <h1>Чинить с умом,<br />а не с молотком</h1>

      <p class="lead">
        Ремонтируем пылесосы, роботы пылесосы, дрели и любую другую технику.
        Находим причину поломки, а не меняем детали наугад.
      </p>

      <div class="main-actions">
        <BaseButton variant="accent" @click="open">
          <FeatureIcon name="tools" :size="18" />
          Записаться на диагностику
        </BaseButton>

        <BaseButton href="#services" variant="light">
          Смотреть услуги
        </BaseButton>
      </div>

      <div class="main-facts">
        <div class="main-fact">
          <FeatureIcon name="diagnostic" :size="20" />
          <span>Диагностика<br /><b>без навязанных работ</b></span>
        </div>

        <div class="main-fact">
          <FeatureIcon name="warranty" :size="20" />
          <span>Гарантия<br /><b>на выполненный ремонт</b></span>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.main {
  position: relative;
  min-height: 510px;
  display: flex;
  align-items: flex-end;
  overflow: hidden;
  background:
    linear-gradient(135deg, #202023 0%, #111113 100%);
  box-shadow: 0 16px 34px rgba(17, 17, 19, 0.14);
}
.main--photo {
  bachground-position: center;
  background-size: cover;
}
.main--photo::before {
  content: '';
  position: absolute;
  inset: 0;
  z-index: 0;
  background:
    linear-gradient(90deg, rgba(17, 17, 19, 0.90) 0%, rgba(17, 17, 19, 0.68) 52%, rgba(17, 17, 19, 0.28) 100%),
    linear-gradient(0deg, rgba(17, 17, 19, 0.45), transparent 55%);;
}
.main-placeholder span {
  font-size: 15px;
  font-weight: 600;
}

.main-placeholder small {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.32);
}

.main-content {
  position: relative;
  z-index: 1;
  width: min(1100px, 100%);
  padding: 56px 56px 56px 430px;
  color: #fff;
}

h1 {
  margin: 0 0 18px;
  color: #fff;
  font-size: clamp(34px, 4vw, 54px);
  line-height: 1.05;
  letter-spacing: -1.4px;
  font-weight: 850;
}

.lead {
  max-width: 560px;
  margin: 0 0 30px;
  font-size: 16px;
  line-height: 1.62;
  color: rgba(255,255,255,0.78);
}

.main-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.main-facts {
  display: flex;
  flex-wrap: wrap;
  gap: 22px;
  margin-top: 40px;
}

.main-fact {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  color: var(--accent);
}

.main-fact span {
  color: rgba(255,255,255,0.66);
  font-size: 14px;
  line-height: 1.35;
}

.main-fact b {
  font-weight: 600;
  color: rgba(255,255,255,0.92);
}

@media screen and (max-width: 768px) {
  .main {
    min-height: 530px;
  }

  .main-content {
    padding: 32px 24px;
  }

  .main--photo::before {
    background:
        linear-gradient(0deg, rgba(17,17,19,0.92) 5%, rgba(17,17,19,0.58) 100%);
  }

  .main-actions {
    flex-direction: column;
  }

  .main-actions :deep(.btn) {
    justify-content: center;
    width: 100%;
  }

  .main-facts {
    gap: 14px;
    margin-top: 28px;
  }
}
</style>