<script setup>
defineProps({
  name: {
    type: String,
    required: true,
    validator: (value) => ['diagnostic', 'warranty', 'price', 'board', 'speed', 'tools'].includes(value)
  },
  size: {
    type: [String, Number],
    default: 24
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
  >
    <template v-if="name === 'diagnostic'">
      <path
          d="M15.5,14h-.8l-.3-.3A6.26,6.26,0,0,0,16,9.5,6.5,6.5,0,1,0,9.5,16a6.26,6.26,0,0,0,4.2-1.6l.3.3v.8l5,5L20.5,19Zm-6-9a4.48,4.48,0,0,1,4.39,3.5H11.46l-1.07,1.6L8.22,6,6.91,8.5H5.11A4.48,4.48,0,0,1,9.5,5Zm0,9A4.48,4.48,0,0,1,5,10H7.82l.41-.78,2,3.79,2-3H14A4.47,4.47,0,0,1,9.5,14Z"
          fill="currentColor"
          fill-rule="evenodd"
      />
    </template>
    <template v-else-if="name === 'warranty'">
      <path
          d="M12 3.2L19 6V11.1C19 15.4 16.1 19.2 12 20.8C7.9 19.2 5 15.4 5 11.1V6L12 3.2Z"
          stroke="currentColor"
          stroke-width="1.7"
          stroke-linejoin="round"
      />
      <path d="M8.8 12L11 14.1L15.5 9.6"
            stroke="currentColor"
            stroke-width="1.7"
            stroke-linecap="round"
            stroke-linejoin="round"
      />
    </template>
    <template v-else-if="name === 'price'">
      <path
          d="M2,11.308a1,1,0,0,0,.293.707l9.692,9.692a1,1,0,0,0,1.414,0L21.707,13.4a1,1,0,0,0,0-1.414L12.015,2.293A1,1,0,0,0,11.308,2H3A1,1,0,0,0,2,3ZM4,4h6.894l8.692,8.692-6.894,6.894L4,10.894ZM9.923,7.154a1.958,1.958,0,1,1-2.769,0A1.957,1.957,0,0,1,9.923,7.154Z"
          fill="currentColor"
      />
    </template>
    <template v-else-if="name === 'board'">
      <rect x="6.2" y="6.2" width="11.6" height="11.6" rx="1.6"
            stroke="currentColor" stroke-width="1.7" />
      <rect x="9.2" y="9.2" width="5.6" height="5.6" rx=".6"
            stroke="currentColor" stroke-width="1.5" />
      <path d="M9 2.8V6.2M12 2.8V6.2M15 2.8V6.2M9 17.8V21.2M12 17.8V21.2M15 17.8V21.2M2.8 9H6.2M2.8 12H6.2M2.8 15H6.2M17.8 9H21.2M17.8 12H21.2M17.8 15H21.2"
            stroke="currentColor"
            stroke-width="1.5"
            stroke-linecap="round"
      />
    </template>
    <template v-else-if="name === 'speed'">
      <circle cx="12" cy="12" r="8.4" stroke="currentColor" stroke-width="1.7" />
      <path d="M12 7V12L15.4 14.1"
            stroke="currentColor"
            stroke-width="1.7"
            stroke-linecap="round"
            stroke-linejoin="round"
      />
    </template>
    <template v-else-if="name === 'tools'">
      <g transform="scale(1.5)">
        <path
            fill-rule="evenodd"
            clip-rule="evenodd"
            d="M14.773 3.485l-.78-.184-2.108 2.096-1.194-1.216 2.056-2.157-.18-.792a4.42 4.42 0 0 0-1.347-.228 3.64 3.64 0 0 0-1.457.28 3.824 3.824 0 0 0-1.186.84 3.736 3.736 0 0 0-.875 1.265 3.938 3.938 0 0 0 0 2.966 335.341 335.341 0 0 0-6.173 6.234c-.21.275-.31.618-.284.963a1.403 1.403 0 0 0 .464.967c.124.135.272.247.437.328.17.075.353.118.538.127.316-.006.619-.126.854-.337 1.548-1.457 4.514-4.45 6.199-6.204.457.194.948.294 1.444.293a3.736 3.736 0 0 0 2.677-1.133 3.885 3.885 0 0 0 1.111-2.73 4.211 4.211 0 0 0-.196-1.378zM2.933 13.928a.31.31 0 0 1-.135.07.437.437 0 0 1-.149 0 .346.346 0 0 1-.144-.057.336.336 0 0 1-.114-.11c-.14-.143-.271-.415-.14-.568 1.37-1.457 4.191-4.305 5.955-6.046.1.132.21.258.328.376.118.123.245.237.38.341-1.706 1.75-4.488 4.564-5.98 5.994zm11.118-9.065c.002.765-.296 1.5-.832 2.048a2.861 2.861 0 0 1-4.007 0 2.992 2.992 0 0 1-.635-3.137A2.748 2.748 0 0 1 10.14 2.18a2.76 2.76 0 0 1 1.072-.214h.254L9.649 3.839v.696l1.895 1.886h.66l1.847-1.816v.258zM3.24 6.688h1.531l.705.717.678-.674-.665-.678V6.01l.057-1.649-.22-.437-2.86-1.882-.591.066-.831.849-.066.599 1.838 2.918.424.215zm-.945-3.632L4.609 4.58 4.57 5.703H3.494L2.002 3.341l.293-.285zm7.105 6.96l.674-.673 3.106 3.185a1.479 1.479 0 0 1 0 2.039 1.404 1.404 0 0 1-1.549.315 1.31 1.31 0 0 1-.437-.315l-3.142-3.203.679-.678 3.132 3.194a.402.402 0 0 0 .153.105.477.477 0 0 0 .359 0 .403.403 0 0 0 .153-.105.436.436 0 0 0 .1-.153.525.525 0 0 0 .036-.184.547.547 0 0 0-.035-.184.436.436 0 0 0-.1-.153L9.4 10.016z"
            fill="currentColor"
        />
      </g>
    </template>
  </svg>
</template>