<template>
  <view class="row" :class="{ mine }">
    <view class="bubble" :class="{ mine }">
      <text class="content">{{ text }}</text>
      <text class="time">{{ timeText }}</text>
    </view>
  </view>
</template>

<script setup>
import {computed} from 'vue';

const props = defineProps({
  mine: {type: Boolean, default: false},
  text: {type: String, required: true},
  ts: {type: Number, required: true}
});

const timeText = computed(() => {
  const d = new Date(props.ts);
  const pad = n => String(n).padStart(2, '0');
  return `${pad(d.getHours())}:${pad(d.getMinutes())}`;
});
</script>

<style scoped>
.row {
  display: flex;
  padding: 12rpx;
}

.row.mine {
  justify-content: flex-end;
}

.bubble {
  max-width: 80%;
  background: #f5f7fb;
  color: #1a1a1a;
  border-radius: 20rpx;
  padding: 16rpx 20rpx;
}

.bubble.mine {
  background: #4f8cff;
  color: #fff;
}

.content {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.5;
}

.time {
  opacity: .65;
  font-size: 22rpx;
  margin-top: 8rpx;
  display: block;
  text-align: right;
}
</style>
