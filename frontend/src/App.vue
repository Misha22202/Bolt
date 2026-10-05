<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'
import ContactModal from '@/components/uiBase/ContactModal.vue'

const route = useRoute()

const showSiteChrome = computed(() => !route.meta.hideSiteChrome)
</script>

<template>
  <div class="bg-pattern"></div>

  <template v-if="showSiteChrome">
    <AppHeader />
  </template>

  <main>
    <router-view />
  </main>

  <template v-if="showSiteChrome">
    <AppFooter />
    <ContactModal />
  </template>
</template>