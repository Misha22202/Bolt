<script setup>
import CaseCard from '@/components/sections/mainPage/CaseCard.vue'
import { cases } from "@/data/cases.js";
</script>

<template>
<section id="cases">
  <div class="section-head">
    <div>
      <span class="tag">Примеры работ</span>
      <h2>Последние ремонты</h2>
    </div>
  </div>
  <div class="grid-3">
    <CaseCard v-for="item in cases" :key="item.num" v-bind="item" />
  </div>
</section>
</template>

<style scoped>
section {
  padding: 0 0 24px;
}
.section-head {
  margin-bottom: 40px;
}
.section-head h2 {
  font-size: 28px;
  margin: 0;
  font-weight: 800;
}
.tag {
  font-size: 12px;
  letter-spacing: 1.5px;
  text-transform: uppercase;
  color: var(--muted);
}
.grid-3 {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
@media screen and (max-width: 760px) {
  .grid-3 {
    grid-template-columns: 1fr;
  }
  section {
    padding: 64px 0 0;
  }
}
</style>