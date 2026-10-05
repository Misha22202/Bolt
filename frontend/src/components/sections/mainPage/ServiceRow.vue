<script setup>
import FeatureIcon from "@/components/icons/FeatureIcon.vue";

defineProps({
  name: String,
  desc: String,
  price: String,
  icon: {
    type: String,
    default: 'tools',
  }
})
</script>

<template>
  <div class="service-item">
    <div class="service-main">
      <div class="service-icon">
        <FeatureIcon :name="icon" :size="20" />
      </div>

      <div>
        <div class="row-name">{{ name }}</div>
        <div class="row-desc">{{ desc }}</div>
      </div>
    </div>

    <div class="row-price">{{ price }}</div>
  </div>
</template>

<style scoped>
.service-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 20px 26px;
  border-bottom: 1px solid var(--line);
  transition: background 0.2s;
}

.service-item:hover {
  background: var(--accent-soft);
}

.service-item:last-child {
  border-bottom: none;
}

.service-main {
  display: flex;
  align-items: center;
  gap: 14px;
  min-width: 0;
}

.service-icon {
  width: 38px;
  height: 38px;
  flex: 0 0 38px;
  display: grid;
  place-items: center;
  border-radius: 10px;
  color: var(--accent-dark);
  background: var(--accent-soft);
}

.row-name {
  color: var(--ink);
  font-size: 15.5px;
  font-weight: 600;
}

.row-desc {
  margin-top: 3px;
  color: var(--muted);
  font-size: 13.5px;
}

.row-price {
  flex: 0 0 auto;
  padding: 7px 10px;
  border-radius: 8px;
  color: var(--accent-dark);
  background: var(--accent);
  font-size: 14px;
  font-weight: 700;
  white-space: nowrap;
}

@media screen and (max-width: 768px) {
  .service-item {
    align-items: flex-start;
    flex-direction: column;
    gap: 12px;
    padding: 18px;
  }

  .row-price {
    margin-left: 52px;
  }
}
</style>