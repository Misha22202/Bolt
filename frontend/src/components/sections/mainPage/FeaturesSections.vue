<script setup>
import FeatureCard from "@/components/uiBase/FeatureCard.vue";
import { features } from '@/data/features.js'
</script>

<template>
<section class="features-section">
  <div class="section-heading">
    <div>
      <span class="tag">Почему "Первый винт"</span>
      <h2>Ремонт, который можно понять</h2>
    </div>
  </div>

  <div class="features-grid">
    <FeatureCard
      v-for="feature in features"
      :key="feature.title"
      v-bind="feature"
    />
  </div>
</section>
</template>

<style scoped>
.section-heading {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 32px;
  margin-bottom: 28px;
}

.tag {
  display: inline-block;
  margin-bottom: 8px;
  font-size: 12px;
  font-weight: 750;
  letter-spacing: 1.4px;
  text-transform: uppercase;
  color: var(--accent-dark);
}

h2 {
  margin: 0;
  color: var(--ink);
  font-size: 30px;
  line-height: 1.15;
  font-weight: 850;
  letter-spacing: -0.5px;
}

.section-heading p {
  max-width: 400px;
  margin: 0;
  color: var(--ink-soft);
  font-size: 14.5px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

@media screen and (max-width: 768px) {
  .features-section {
    padding-top: 56px;
  }

  .section-heading {
    align-items: flex-start;
    flex-direction: column;
    gap: 12px;
  }

  h2 {
    font-size: 26px;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>