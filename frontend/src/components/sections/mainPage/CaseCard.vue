<script setup>
defineProps({
  num: String,
  title: String,
  desc: String,
})
</script>

<template>
<div class="card">
  <span class="num">{{ num }}</span>
  <h3>{{ title }}</h3>
  <p>{{ desc }}</p>
</div>
</template>

<style scoped>
.card {
  background: var(--card);
  border: 1px solid var(--line);
  border-radius: var(--radius);
  padding: 26px;
}
.card .num {
  font-size: 12px;
  color: var(--muted);
  letter-spacing: 1px;
  margin-bottom: 14px;
  display: block;
}
.card h3 {
  font-size: 17px;
  margin: 0 0 10px;
  font-weight: 700;
}
.card p {
  font-size: 14px;
  color: var(--ink-soft);
  margin: 0;
}
</style>