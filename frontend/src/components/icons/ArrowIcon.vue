<script setup>
defineProps({
  size: {
    type: String,
    default: 20,
  },
  color: {
    type: String,
    default: 'currentColor',
  },
  rotate: {
    type: [String, Number],
    default: 0,
  },
  strokeWidth: {
    type: [String, Number],
    default: 2,
  }
})
</script>

<template>
  <svg
      :width="size"
      :height="size"
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden="true"
      :style="{ transform: `rotate(${rotate}deg)` }"
  >
    <path
        d="M5 12H19M13 6L19 12L13 18"
        :stroke="color"
        :stroke-width="strokeWidth"
        stroke-linecap="round"
        stroke-linejoin="round"
    />
  </svg>
</template>