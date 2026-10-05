<script setup>
import PhotoSlider from "@/components/sections/contactsPage/PhotoSlider.vue"
import ContactInfoSection from "@/components/sections/contactsPage/ContactInfoSection.vue"
import MapSection from "@/components/sections/contactsPage/MapSection.vue"
</script>

<template>
  <div class="container">
    <section class="page-head">
      <h1>Контакты и адрес</h1>
    </section>

    <ContactInfoSection />
    <MapSection />
  </div>
</template>

<style scoped>
.container {
  max-width: var(--maxw);
  background-color: white;
  padding: 56px 48px;
  border-radius: var(--radius);
  margin: 48px auto;
}
.page-head {
  padding: 0 0 32px;
}
.page-head h1 {
  margin: 10px 0 12px;
  font-size: 34px;
  font-weight: 850;
  letter-spacing: -0.6px;
  color: var(--ink);
}

</style>