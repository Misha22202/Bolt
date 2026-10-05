<script setup>
import {computed, onBeforeMount, onBeforeUnmount, onMounted, ref} from 'vue'
import ArrowIcon from "@/components/icons/ArrowIcon.vue";

const props = defineProps({
  images: { type: Array, default: () => [] },
})

const current = ref(0)
const isMobile = ref(false)

const visibleCount = computed(() => (isMobile.value ? 1 : 3))
const maxIndex = computed(() => Math.max(0, props.images.length - visibleCount.value))

const trackStyle = computed(() => ({
  transform: `translateX(-${current.value * (100 / visibleCount.value)}%)`
}))

function updateViewPort() {
  isMobile.value = window.innerWidth <= 768

  if (current.value > maxIndex.value) {
    current.value = maxIndex.value
  }
}

function next() {
  if (!props.images.length) return

  current.value =
      current.value >= maxIndex.value
          ? 0
          : current.value + 1
}
function prev() {
  if (!props.images.length) return

  current.value =
      current.value =
          current.value <= 0
              ? maxIndex.value
              : current.value - 1
}
function goTo(index) {
  current.value = Math.min(index, maxIndex.value)
}

onMounted(() => {
  updateViewPort()
  window.addEventListener('resize', updateViewPort)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', updateViewPort)
})
</script>

<template>
  <div class="slider">
    <div v-if="images.length" class="slider-viewport">
      <div class="slider-track" :style="trackStyle">
        <article
            v-for="(image, index) in images"
            :key="image.src || index"
            class="slide"
        >
          <img
              :src="image.src"
              :alt="image.alt || `Фото мастерской ${index + 1}`"
              class="slider-image"
              loading="lazy"
          />
        </article>
      </div>

      <template v-if="images.length > visibleCount">
        <button
            class="slider-arrow arrow-prev"
            type="button"
            aria-label="Предыдущее фото"
            @click="prev"
        >
          <ArrowIcon :size="20" color="white" :rotate="180" />
        </button>

        <button
            class="slider-arrow arrow-next"
            type="button"
            aria-label="Следующее фото"
            @click="next"
        >
          <ArrowIcon :size="20" color="white" :rotate="0" />
        </button>
      </template>
    </div>

    <div v-if="maxIndex > 0" class="slider-dots">
      <button
          v-for="index in maxIndex + 1"
          :key="index"
          type="button"
          class="dot"
          :class="{ 'dot-active': current === index - 1 }"
          :aria-label="`Перейти к позиции ${index}`"
          @click="goTo(index - 1)"
      ></button>
    </div>
  </div>
</template>

<style scoped>
.slider {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.slider-viewport {
  position: relative;
  overflow: hidden;
  padding: 0 1px;
}

.slider-track {
  display: flex;
  align-items: flex-start;
  transition: transform 0.35s ease;
  will-change: transform;
}

.slide {
  width: 33.333333%;
  min-width: 33.333333%;
  padding: 0 8px;
}

.slider-image {
  display: block;
  width: 100%;
  height: auto;
  border: 1px solid var(--line);
  border-radius: var(--radius-sm);
  background: var(--card);
}

.slider-arrow {
  position: absolute;
  top: 50%;
  z-index: 1;
  width: 42px;
  height: 42px;
  display: grid;
  place-items: center;
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 50%;
  background: rgba(17, 17, 19, 0.76);
  color: #fff;
  font-size: 28px;
  line-height: 1;
  cursor: pointer;
  transform: translateY(-50%);
  transition: background 0.2s, border-color 0.2s, color 0.2s;
}

.slider-arrow:hover {
  border-color: var(--accent);
  background: var(--accent);
  color: var(--accent-dark);
}

.arrow-prev {
  left: 16px;
}

.arrow-next {
  right: 16px;
}

.slider-dots {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px;
}

.dot {
  width: 8px;
  height: 8px;
  padding: 0;
  border: 0;
  border-radius: 50%;
  background: var(--line);
  cursor: pointer;
  transition: transform 0.2s, background 0.2s;
}

.dot-active {
  background: var(--accent);
  transform: scale(1.3);
}

@media screen and (max-width: 768px) {
  .slide {
    width: 100%;
    min-width: 100%;
    padding: 0;
  }

  .slider-viewport {
    padding: 0;
  }

  .slider-arrow {
    width: 38px;
    height: 38px;
    font-size: 25px;
  }

  .arrow-prev {
    left: 12px;
  }

  .arrow-next {
    right: 12px;
  }
}
</style>