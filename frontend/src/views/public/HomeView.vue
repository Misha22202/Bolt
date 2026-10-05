<script setup>
import MainSection from "@/components/sections/mainPage/MainSection.vue";
import ServicesSection from "@/components/sections/mainPage/ServicesSection.vue";
import AboutSection from "@/components/sections/mainPage/AboutSection.vue";
import CasesSection from "@/components/sections/mainPage/CasesSection.vue";
import ContactSection from "@/components/sections/mainPage/ContactSection.vue";
import FeaturesSections from "@/components/sections/mainPage/FeaturesSections.vue";
</script>

<template>
  <MainSection />
<div class="content-col">

  <FeaturesSections />
  <ServicesSection />
  <CasesSection />
  <ContactSection />
</div>
</template>

<style scoped>

</style>