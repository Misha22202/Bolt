<script setup>
defineProps({
  variant: {type: String, default: 'primary'},
  href: { type: String, default: null },
  target: { type: String, default: null },
})
</script>

<template>
<a
  v-if="href"
  :href="href"
  :target="target"
  class="btn"
  :class="`btn-${variant}`"
>
  <slot />
</a>
  <button v-else class="btn" :class="`btn-${variant}`">
    <slot />
  </button>
</template>

<style scoped>
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 13px 26px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  line-height: 1.2;
  border: 1px solid transparent;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.2s ease;
}

.btn:hover {
  transform: translateY(-1px);
}

.btn:focus-visible {
  outline: 3px solid var(--accent-soft);
  outline-offset: 3px;
}

.btn-primary {
  background: var(--ink);
  color: #fff;
}
.btn-primary:hover {
  background: #000;
}
.btn-accent {
  color: var(--accent-dark);
  background: var(--accent);
}
.btn-accent:hover {
  background: var(--accent-hover);
}
.btn-light {
  color: #fff;
  border-color: rgba(255,255,255,0.38);
  background: rgba(255,255,255,0.06);
}
.btn-light:hover {
  border-color: rgba(255,255,255,0.7);
  background: rgba(255,255,255,0.13);
}
.btn-secondary {
  background: transparent;
  color: var(--ink);
  border: 1px solid var(--line);
}
.btn-secondary:hover {
  border-color: var(--ink);
}
</style>