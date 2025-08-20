<template>
  <base-layout>
    <view class="flex flex-col h-full p-4 bg-neutral-50">
      <!-- 消息列表 -->
      <scroll-view scroll-y class="flex-1 mb-4" :scroll-top="scrollTop" @scrolltolower="scrollToLower">
        <view v-for="(msg, index) in messages" :key="index" class="mb-3 flex" :class="msg.isUser ? 'justify-end' : 'justify-start'">
          <view class="flex items-end max-w-3/4">
            <image
              :src="msg.isUser ? userAvatar : aiAvatar"
              mode="aspectFill"
              class="w-10 h-10 rounded-full mr-2"
            />
            <view :class="['rounded-xl p-3', msg.isUser ? 'bg-primary-500 text-white' : 'bg-white text-neutral-900 shadow']">
              <text class="block font-semibold mb-1">{{ msg.isUser ? userName : aiName }}</text>
              <text class="whitespace-pre-wrap break-words">{{ msg.content }}</text>
            </view>
          </view>
        </view>
      </scroll-view>

      <!-- 输入框区域 -->
      <view class="flex items-center">
        <uni-easyinput
          v-model="inputMessage"
          type="text"
          placeholder="请输入消息"
          class="flex-1 box-border rounded-xl border border-neutral-300 px-4 py-2 mr-2"
          @change="onInputChange"
          @confirm="sendMessage"
        />
        <button
          class="bg-primary-500 text-white rounded-full px-6 py-2"
          :disabled="inputMessage.trim() === ''"
          @click="sendMessage"
        >
          发送
        </button>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
const { proxy } = getCurrentInstance();

const messages = ref([]);
const inputMessage = ref('');
const scrollTop = ref(0);

const userName = '用户';
const aiName = 'AI助手';
const userAvatar = 'https://www.codeflying.net/preview/kitty-family.jpg';
const aiAvatar = 'https://www.codeflying.net/preview/ai-robot.jpg';

// 发送消息
function sendMessage() {
  const content = inputMessage.value.trim();
  if (!content) return;

  // 用户消息入队
  messages.value.push({ content, isUser: true });
  inputMessage.value = '';
  updateScroll();

  // AI回复固定示例文本
  setTimeout(() => {
    messages.value.push({
      content: '您好，我是AI助手，很高兴为您服务！这是一个示例回复。',
      isUser: false
    });
    updateScroll();
  }, 800);
}

// 输入框内容变化
function onInputChange(e) {
  inputMessage.value = e.detail.value;
}

// 更新滚动位置
function updateScroll() {
  // 滚动到底部
  scrollTop.value = 99999999;
}

// 滚动到底部事件处理
function scrollToLower() {
  updateScroll();
}

// 页面加载时初始化消息列表为空
onLoad(() => {
  messages.value = [];
  inputMessage.value = '';
  scrollTop.value = 0;
});
</script>
