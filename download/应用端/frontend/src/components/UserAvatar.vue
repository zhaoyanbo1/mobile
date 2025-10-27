<template>
  <view class="avatar" :style="{ width: size+'rpx', height: size+'rpx' }">
    <image v-if="src" :src="src" mode="aspectFill" />
    <text v-else class="fallback">{{ fallbackText }}</text>
  </view>
</template>

<script setup>
const props = defineProps({
  src: { type: String, default: '' },
  name: { type: String, default: '' },
  size: { type: Number, default: 80 } // rpx
});
const size = props.size;
const fallbackText = (props.name || '?').slice(0, 1).toUpperCase();
</script>

<style scoped>
.avatar { border-radius: 9999rpx; overflow: hidden; background: #e6eefc;
  display: flex; align-items: center; justify-content: center; }
image { width: 100%; height: 100%; }
.fallback { color: #2c5cff; font-weight: 600; }
</style>
