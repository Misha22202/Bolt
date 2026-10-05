<script setup>
import FeatureCard from "@/components/uiBase/FeatureCard.vue";
</script>

<template>
  <section id="about" class="about-section">
    <div class="about-intro">
      <h2>Разбираем до последнего винта</h2>
      <p>
        Мы не меняем деталь наугад. Сначала находим точную причину поломки,
        затем объясняем, что произошло, и согласовываем ремонт.
      </p>

      <div class="about-note">
        <span>Ремонтируем технику так, как ремонтировали бы свою.</span>
      </div>
    </div>
  </section>
</template>

<style scoped>
.about-section {
  gap: 48px;
  align-items: center;
  padding: 24px 0 24px;
}

h2 {
  margin: 0 0 16px;
  font-size: 32px;
  line-height: 1.12;
  font-weight: 850;
  letter-spacing: -0.6px;
  color: var(--ink);
}

.about-intro > p {
  max-width: 100%;
  margin: 0;
  color: var(--ink-soft);
  font-size: 15.5px;
  line-height: 1.65;
}

.about-note {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
  color: var(--ink);
  font-size: 15px;
  font-weight: 650;
}

.about-note-mark {
  display: grid;
  width: 38px;
  height: 38px;
  place-items: center;
  border-radius: 50%;
  color: var(--accent-dark);
  background: var(--accent);
  font-size: 12px;
  font-weight: 800;
}

.about-features {
  display: grid;
  gap: 16px;
}

@media screen and (max-width: 768px) {
  .about-section {
    grid-template-columns: 1fr;
    gap: 28px;
    padding-top: 56px;
  }

  h2 {
    font-size: 27px;
  }
}
</style>