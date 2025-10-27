<template>
  <div class="relative w-24 h-24" :class="['avatar-enter']">
    <div class="absolute inset-0 rounded-full bg-gradient-to-br from-[#A3B18A] to-[#7E8C77] shadow-[0_3px_15px_rgba(0,0,0,0.08)]" />
    <div class="absolute inset-0 flex items-center justify-center">
      <div class="relative w-20 h-20">
        <div
          class="absolute top-6 left-4 w-2 h-2.5 bg-white rounded-full"
          :style="{ transform: blink ? 'scaleY(0.1)' : 'scaleY(1)', transition: 'transform 0.1s' }"
        />
        <div
          class="absolute top-6 right-4 w-2 h-2.5 bg-white rounded-full"
          :style="{ transform: blink ? 'scaleY(0.1)' : 'scaleY(1)', transition: 'transform 0.1s' }"
        />
        <svg class="absolute bottom-5 left-1/2 -translate-x-1/2" width="24" height="12" viewBox="0 0 24 12" fill="none">
          <path d="M 2 2 Q 12 8 22 2" stroke="white" stroke-width="2" stroke-linecap="round" fill="none" />
        </svg>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue';

const blink = ref(false);
let timer: number | undefined;

onMounted(() => {
  timer = window.setInterval(() => {
    blink.value = true;
    window.setTimeout(() => {
      blink.value = false;
    }, 200);
  }, 3000 + Math.random() * 2000);
});

onBeforeUnmount(() => {
  if (timer) {
    window.clearInterval(timer);
  }
});
</script>

<style scoped>
.avatar-enter {
  animation: pop-in 0.4s ease-out;
}

@keyframes pop-in {
  from {
    opacity: 0;
    transform: scale(0.8);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
