<script setup>
import FeatureIcon from '@/components/icons/FeatureIcon.vue'

defineProps({
  icon: {
    type: String,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  text: {
    type: String,
    required: true
  }
})
</script>

<template>
<article class="feature-card">
  <div class="feature-icon">
    <FeatureIcon :name="icon" :size="25"/>
  </div>

  <div>
    <h3>{{ title }}</h3>
    <p>{{ text }}</p>
  </div>
</article>
</template>

<style scoped>
.feature-card {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  min-height: 150px;
  padding: 24px;
  background: rgba(255, 255, 255, 0.72);
  border: 1px solid var(--line);
  border-radius: var(--radius);
  box-shadow: 0 8px 20px rgba(17, 17, 19, 0.03);
  transition: transform 0.2s ease, border-color 0.2s ease, box-shadow 0.2s ease;
}

.feature-card:hover {
  transform: translateY(-3px);
  border-color: var(--accent-border);
  box-shadow: 0 14px 28px rgba(17, 17, 17, 0.08);
}

.feature-icon {
  width: 46px;
  height: 46px;
  flex: 0 0 46px;
  display: grid;
  place-items: center;
  color: var(--accent-dark);
  background: var(--accent);
  border-radius: 12px;
}

.feature-card p {
  margin: 0;
  font-size: 14px;
  line-height: 1.5;
  color: var(--ink-soft);
}

.feature-card h3 {
  margin: 0 0 18px;
}

@media screen and (max-width: 768px) {
  .feature-card {
    min-height: auto;
    padding: 20px;
  }
}
</style>