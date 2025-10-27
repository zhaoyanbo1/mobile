<template>
  <base-layout>
    <!-- 顶部栏 -->
    <view class="topbar">
      <image :src="icons.menu" mode="widthFix" class="icon" />
      <text class="title">Social</text>
      <view class="right-spacer"></view>
    </view>

    <view class="page bg-[#F8F9F8]">
      <!-- 顶部人物图 -->
      <view class="hero-container">
        <image src="/static/elder-avatar.png" class="hero-image" mode="aspectFill" />
        <view class="hero-gradient"></view>
      </view>

      <!-- 功能按钮区 -->
      <view class="button-area">
        <!-- AI Chat -->
        <view class="button-wrapper">
          <button class="feature-btn" @click="goAIChat">
            <view class="flex items-center gap-4">
              <view class="icon-circle from-[#A3B18A] to-[#7E8C77]">
                <MessageCircle class="w-7 h-7 text-white" :stroke-width="2" />
              </view>
              <view class="flex-1 min-w-0">
                <text class="card-title">AI Chat</text>
                <text class="card-subtitle">Talk freely with your AI companion</text>
              </view>
            </view>
          </button>
        </view>

        <!-- Friend Chat -->
        <view class="button-wrapper">
          <button class="feature-btn" @click="goFriendChat">
            <view class="flex items-center gap-4">
              <view class="icon-circle from-[#DDB892] to-[#C9A87C]">
                <Users class="w-7 h-7 text-white" :stroke-width="2" />
              </view>
              <view class="flex-1 min-w-0">
                <text class="card-title">Friend Chat</text>
                <text class="card-subtitle">Chat with your friends</text>
              </view>
            </view>
          </button>
        </view>

        <!-- Team activities -->
        <view class="button-wrapper">
          <button class="feature-btn" @click="goLinkedTasks">
            <view class="flex items-center gap-4">
              <view class="icon-circle from-[#7E8C77] to-[#588157]">
                <ListChecks class="w-7 h-7 text-white" :stroke-width="2" />
              </view>
              <view class="flex-1 min-w-0">
                <text class="card-title">Team activities</text>
                <text class="card-subtitle">View and manage linked tasks</text>
              </view>
            </view>
          </button>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup lang="ts">
import { getCurrentInstance } from 'vue'
import { MessageCircle, Users, ListChecks } from 'lucide-vue-next'
import menuIcon from '@/static/gg_menu-left-alt.svg'
const icons = { menu: menuIcon }

const navTo = (url: string) => {
  const proxy = getCurrentInstance()?.proxy
  if (proxy?.$cf?.navigate?.to) proxy.$cf.navigate.to({ url, type: 'page', mode: 'navigate' })
  else uni.navigateTo({ url })
}

const goAIChat = () => navTo('/pagesA/chat/ai_chat/index')
const goFriendChat = () => navTo('/pagesA/chat/friend_chat/FriendList')
const goLinkedTasks = () => navTo('/pagesA/activities/index')
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 顶部栏 */
.topbar {
  display: grid;
  grid-template-columns: 112rpx 1fr 112rpx;
  align-items: center;
  height: 112rpx;
  background: #fff;
  padding: 0 24rpx;
}
.icon { width: 64rpx; height: 48rpx; }
.title { text-align: center; font-size: 50rpx; font-weight: 700; color: #1d1d1d; }
.right-spacer { width: 112rpx; }

/* 顶部人物图 */
.hero-container {
  position: relative;
  width: 100%;
  height: 45vh;
  overflow: hidden;
}
.hero-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center 30%;
  transition: transform 0.5s ease;
}
.hero-container:hover .hero-image {
  transform: scale(1.03);
}
.hero-gradient {
  position: absolute;
  bottom: 0; left: 0; right: 0;
  height: 40%;
  background: linear-gradient(
      to bottom,
      rgba(255, 255, 255, 0) 0%,
      rgba(248, 249, 248, 0.3) 30%,
      #f8f9f8 100%
  );
}

/* 按钮区 */
.button-area {
  width: 100%;
  margin-top: -60rpx;
  padding: 0 0 60rpx;
  z-index: 5;
  display: flex;
  flex-direction: column;
  gap: 28rpx;
}

/* 外层容器（撑满屏幕，但内部卡片留出间距） */
.button-wrapper {
  padding: 0 30rpx; /* ✅ 两边留间距，保持圆角 */
}

/* 按钮卡片样式 */
.feature-btn {
  width: 100%;
  border: none;
  background: #fff;
  border-radius: 1.4rem; /* ✅ 保留圆角 */
  padding: 1rem 1.4rem;
  display: flex;
  align-items: center;
  text-align: left;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.06);
  transition: all 0.25s ease;
}
.feature-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
}

/* 左侧 icon 圆形 */
.icon-circle {
  width: 3rem;
  height: 3rem;
  border-radius: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(145deg, #a3b18a, #7e8c77);
}

/* 文本样式 */
.card-title {
  display: block;
  font-size: 34rpx;
  font-weight: 700;
  color: #1d1d1d;
}
.card-subtitle {
  display: block;
  font-size: 28rpx;
  color: #555;
  line-height: 1.4;
}
</style>
