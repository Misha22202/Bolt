<script setup>
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'

import BaseButton from '@/components/uiBase/BaseButton.vue'
import { navLinks } from '@/data/nav.js'
import ScrewIcon from '@/components/icons/ScrewIcon.vue'
import { useContactModal } from '@/composables/useContactModal.js'

const route = useRoute()
const { open } = useContactModal()

const isMenuOpen = ref(false)
const desktopNav = ref(null)
const navIndicator = ref(null)

function toggleMenu() {
  isMenuOpen.value = !isMenuOpen.value
}

function closeMenu() {
  isMenuOpen.value = false
}

function isRouteLink(link) {
  return link.type === 'route'
}

function isActiveRoute(link) {
  if (!isRouteLink(link)) {
    return false
  }

  if (link.to === '/') {
    return route.path === '/'
  }

  return route.path === link.to
}

async function updateNavIndicator() {
  await nextTick()

  if (!desktopNav.value || !navIndicator.value) {
    return
  }

  const activeLink = desktopNav.value.querySelector('.nav-link--active')

  if (!activeLink) {
    navIndicator.value.style.opacity = '0'
    return
  }

  const navRect = desktopNav.value.getBoundingClientRect()
  const linkRect = activeLink.getBoundingClientRect()

  navIndicator.value.style.width = `${linkRect.width}px`
  navIndicator.value.style.transform =
      `translateX(${linkRect.left - navRect.left}px)`
  navIndicator.value.style.opacity = '1'
}

watch(
    () => route.fullPath,
    updateNavIndicator,
    { immediate: true }
)

onMounted(() => {
  window.addEventListener('resize', updateNavIndicator)
  updateNavIndicator()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateNavIndicator)
})
</script>

<template>
  <header class="header">
    <div class="header-inner">
      <router-link to="/" class="logo" aria-label="Первый Винт — главная">
        <ScrewIcon :size="34" color="var(--accent)" />

        <div class="logo-text">
          <b>ПЕРВЫЙ ВИНТ</b>
          <span>Мастерская</span>
        </div>
      </router-link>
      
      <nav ref="desktopNav" class="nav-desktop" aria-label="Основная навигация">
        <template v-for="link in navLinks" :key="link.to">
          <router-link
              v-if="isRouteLink(link)"
              :to="link.to"
              class="nav-link"
              :class="{ 'nav-link--active': isActiveRoute(link) }"
          >
            {{ link.label }}
          </router-link>

          <a
              v-else
              :href="link.to"
              class="nav-link"
          >
            {{ link.label }}
          </a>
        </template>

        <span ref="navIndicator" class="nav-indicator" aria-hidden="true"></span>
      </nav>

      <BaseButton
          variant="accent"
          class="header-cta header-cta-desktop"
          @click="open"
      >
        Заказать ремонт
      </BaseButton>

      <button
          class="burger"
          :class="{ 'burger-open': isMenuOpen }"
          type="button"
          aria-label="Открыть меню"
          :aria-expanded="isMenuOpen"
          @click="toggleMenu"
      >
        <span></span>
        <span></span>
        <span></span>
      </button>
    </div>

    <Transition name="menu-fade">
      <nav
          v-if="isMenuOpen"
          class="nav-mobile"
          aria-label="Мобильная навигация"
      >
        <template v-for="link in navLinks" :key="link.to">
          <router-link
              v-if="isRouteLink(link)"
              :to="link.to"
              class="nav-mobile-link"
              @click="closeMenu"
          >
            {{ link.label }}
          </router-link>

          <a
              v-else
              :href="link.to"
              class="nav-mobile-link"
              @click="closeMenu"
          >
            {{ link.label }}
          </a>
        </template>

        <BaseButton
            variant="accent"
            class="header-cta"
            @click="open(); closeMenu()"
        >
          Заказать ремонт
        </BaseButton>
      </nav>
    </Transition>
  </header>
</template>

<style scoped>
.header {
  background: var(--layout);
  color: #f4f4f5;
  position: sticky;
  top: 0;
  z-index: 999;
}
.header-inner {
  max-width: var(--maxw);
  margin: 0 auto;
  padding: 16px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
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
  font-size: 17px;
  letter-spacing: .5px;
  color: var(--accent);
}
.logo-text span {
  font-size: 10px;
  letter-spacing: 2px;
  text-transform: uppercase;
}
.nav-desktop {
  position: relative;
  display: flex;
  align-items: center;
  gap: 28px;
  height: 52px;
}

.nav-link {
  position: relative;
  display: inline-flex;
  align-items: center;
  height: 100%;
  color: #c9c9cd;
  font-size: 14px;
  font-weight: 400;
  white-space: nowrap;
  transition: color 0.25s ease;
}

.nav-link:hover {
  color: var(--accent);
}

.nav-link--active {
  color: var(--accent);
}
.nav-indicator {
  position: absolute;
  bottom: 0;
  left: 0;
  z-index: 1;
  width: 0;
  height: 2px;
  border-radius: 999px;
  pointer-events: none;
  opacity: 0;
  background: var(--accent);
  transition:
      transform 0.32s cubic-bezier(0.22, 1, 0.36, 1),
      width 0.32s cubic-bezier(0.22, 1, 0.36, 1),
      opacity 0.16s ease;
}
.header-cta {
  background: var(--accent) !important;
  color: var(--accent-dark) !important;
  padding: 9px 18px !important;
}
.header-cta:hover {
  background: var(--accent-hover) !important;
}
.burger {
  display: none;
}
.nav-mobile {
  display: none;
}

@media screen and (max-width: 768px) {
  .nav-desktop {
    display: none;
  }

  .nav-mobile .router-link-active {
    color: var(--accent);
  }

  .nav-mobile .router-link-active::before {
    content: "";
    display: inline-block;
    width: 6px;
    height: 6px;
    margin-right: 8px;
    border-radius: 50%;
    background: var(--accent);
  }
  .header-cta-desktop {
    display: none;
  }
  .burger {
    position: relative;
    display: flex;
    gap: 5px;
    width: 32px;
    height: 32px;
    background: transparent;
    border: none;
    cursor: pointer;
    padding: 0;
    flex-shrink: 0;
  }
  .burger span {
    position: absolute;
    left: 5px;
    right: 5px;
    height: 2px;
    background: #fff;
    transition: transform .2s, opacity .2s, top .2s;
  }
  .burger span:nth-child(1) {
    top: 11px;
  }
  .burger span:nth-child(2) { top:
      15px;
  }
  .burger span:nth-child(3) {
    top: 19px;
  }
  .burger-open span:nth-child(1) {
    top: 15px;
    transform: rotate(45deg);
  }
  .burger-open span:nth-child(2) {
    opacity: 0;
  }
  .burger-open span:nth-child(3) {
    top: 15px;
    transform: rotate(-45deg);
  }
  .nav-mobile {
    display: flex;
    flex-direction: column;
    gap: 4px;
    padding: 12px 24px 20px;
    border-top: 1px solid #2a2a2d;
  }
  .nav-mobile-link {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #2a2a2d;
    color: #c9c9cd;
    font-size: 14px;
  }

  .nav-mobile-link.router-link-active {
    color: var(--accent);
  }
  .nav-mobile .header-cta {
    margin-top: 12px;
    justify-content: center;
    width: 100%;
  }
  .menu-fade-enter-active,
  .menu-fade-leave-active {
    transition: opacity .2s, transform .2s;
  }
  .menu-fade-enter-from,
  .menu-fade-leave-to {
    opacity: 0;
    transform: translateY(-8px);
  }
}
</style>