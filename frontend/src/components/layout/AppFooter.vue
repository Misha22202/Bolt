<script setup>
import { navLinks } from '@/data/nav.js'
import ScrewIcon from "@/components/icons/ScrewIcon.vue";
</script>

<template>
<footer class="footer">
  <div class="footer-inner">
    <div class="footer-grid">
      <div class="footer-brand">
        <div class="logo">
          <ScrewIcon :size="34" color="var(--accent)" />
          <div class="logo-text">
            <b>ПЕРВЫЙ ВИНТ</b>
            <span>Мастерская</span>
          </div>
        </div>
        <p>Ремонт электроники в Воронеже. Диагностика, пайка, замена компонентов бытовой техники.</p>
      </div>

      <div class="footer-items">
        <h4>Навигация</h4>
        <ul>
          <li v-for="link in navLinks" :key="link.to">
            <router-link v-if="link.type === 'route'" :to="link.to">
              {{ link.label }}
            </router-link>
            <a v-else :href="link.to">
              {{ link.label }}
            </a>
          </li>
        </ul>
      </div>

      <div class="footer-items">
        <h4>Контакты</h4>
        <ul>
          <li><a href="https://t.me/pervyivint" target="_blank">Telegram @pervyivint</a></li>
          <li><a href="https://t.me/pervyivint?direct" target="_blank">Написать напрямую</a></li>
        </ul>
      </div>
    </div>

    <div class="footer-bottom">
      <span>©2026 Первый винт. Все права защищены.</span>
      <span>Ремонт электроники · Воронеж</span>
    </div>
  </div>
</footer>
</template>

<style scoped>
.footer {
  background: var(--layout);
  color: #c9c9cd;
  margin-top: 60px;
}
.footer-inner {
  max-width: var(--maxw);
  margin: 0 auto;
  padding: 52px 24px 28px;
}
.footer-grid {
  display: grid;
  grid-template-columns: 1.4fr 1fr 1fr;
  gap: 40px;
  margin-bottom: 40px;
}
.footer-grid h4 {
  color: #fff;
  font-size: 13px;
  letter-spacing: 1.5px;
  text-transform: uppercase;
  margin: 0 0 16px;
}
.footer-grid ul {
  list-style:none;
  padding: 0;
  margin: 0;
}
.footer-grid li {
  margin-bottom: 10px;
  font-size: 14px;
}
.footer-grid li a:hover {
  color: #fff;
}
.footer-brand p {
  font-size: 13.5px;
  color: #9a9aa0;
  max-width: 280px;
  margin-top: 12px;
}
.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}
.logo-text {
  display: flex;
  flex-direction: column;
  line-height: 1.1;
}
.logo-text b {
  color: var(--accent);
}
.logo-text span {
  font-size: 10px;
  letter-spacing: 2px;
  text-transform: uppercase;
}
.footer-bottom {
  border-top: 1px solid #2a2a2d;
  padding-top: 22px;
  display: flex;
  justify-content: space-between;
  font-size: 12.5px;
  color: #77777c;
  flex-wrap: wrap;
  gap: 10px;
}
.footer-items h4 {
  color: var(--accent);
}
@media screen and (max-width: 700px) {
  .footer-grid {
    grid-template-columns: 1fr;
    gap: 28px;
  }
  .footer-inner {
    padding: 40px 20px 24px;
  }
  .footer-bottom {
    flex-direction: column;
    gap: 6px;
  }
}
</style>