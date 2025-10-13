<!-- src/pagesA/chat/index.vue  （如果这是首页，就保留；若你的首页在 ai_chat/index.vue，把本文件名改成对应页面即可） -->
<template>
  <base-layout>
    <view class="page">
      <!-- Top bar -->
      <view class="topbar">
        <image :src="icons.menu" mode="widthFix" class="icon" />
        <text class="title">Chat</text>
        <view class="right-spacer"></view>
      </view>

      <!-- Content section -->
      <view class="content">
        <!-- 老头头像：点击跳转到聊天页 -->
        <view class="avatar-wrap" @click="goChat">
          <image :src="icons.avatar" mode="aspectFit" class="avatar" />
        </view>

        <!-- Card: Health Score -->
        <view class="card card-alert">
          <view class="card-icon">
            <image :src="icons.health" mode="aspectFit" class="card-icon-img" />
          </view>
          <view class="card-text">
            <text class="card-title">Health Score</text>
          </view>
          <text class="card-value">{{ healthScore }}</text>
        </view>

        <!-- Card: Today Goals -->
        <view class="card card-safe">
          <view class="card-icon">
            <image :src="icons.goals" mode="aspectFit" class="card-icon-img" />
          </view>
          <view class="card-text">
            <text class="card-title">Today Goals</text>
          </view>
          <text class="card-value">{{ goalsDone }}/{{ goalsTotal }}</text>
        </view>

        <!-- Card: Notice -->
        <view class="card card-neutral">
          <view class="card-icon">
            <image :src="icons.notice" mode="aspectFit" class="card-icon-img" />
          </view>
          <view class="card-text">
            <text class="card-title">Notice</text>
          </view>
          <text class="card-value">{{ noticeCount }}</text>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
import menuIcon from '@/static/gg_menu-left-alt.svg'
import grandfather from '@/static/free-icon-grandfather-2829817.svg'
import health_alert from '@/static/alert.svg'
import safe from '@/static/safe.svg'
import notice from '@/static/Notice.svg'

/** 计数展示 */
const healthScore = ref(40)
const goalsDone = ref(2)
const goalsTotal = ref(5)
const noticeCount = ref(2)

/** 统一图标 */
const icons = {
  menu: menuIcon,
  avatar: grandfather,
  health: health_alert,
  goals: safe,
  notice: notice
}

/** 点击头像跳转 */
const goChat = () => {
  const proxy = getCurrentInstance()?.proxy
  const target = 'ai_chat/index' // ← 目标页面路径（已在 pages.json 的 subPackages: {root:"pagesA"} 里注册）
  if (proxy?.$cf?.navigate?.to) {
    proxy.$cf.navigate.to({ url: target, type: 'page', mode: 'navigate' })
  } else {
    uni.navigateTo({ url: target }) // 若目标是原生 tabBar 页，改成 uni.switchTab({ url: target })
  }
}
</script>

<style scoped>
/* Theme variables */
:root{
  --bg:#f7f7f7;
  --text:#222;
  --muted:#666;
  --card-alert:#eeb79b;
  --card-safe:#90b79e;
  --card-neutral:#bdb8b5;
  --white:#fff;
  --shadow:0 6px 14px rgba(0,0,0,.08);
  --radius-2xl:24rpx;
}

/* Page container */
.page{
  min-height:100vh;
  background:var(--bg, #f7f7f7);
  display:flex;
  flex-direction:column;
}

/* Top bar */
.topbar{
  display:grid;
  grid-template-columns:112rpx 1fr 112rpx;
  align-items:center;
  height:112rpx;
  background:var(--white);
  padding:0 24rpx;
}
.icon{ width:64rpx; height:48rpx; }
.title{
  text-align:center;
  font-size:50rpx;
  font-weight:700;
  color:var(--text);
}
.right-spacer{ width:112rpx; }

/* Content area */
.content{
  padding:36rpx 36rpx 220rpx;
  box-sizing:border-box;
}
.avatar-wrap{
  display:flex;
  justify-content:center;
  margin:36rpx 0 16rpx;
  cursor: pointer; /* H5 提示可点击 */
}
.avatar{
  width:520rpx;
  height:520rpx;
  border-radius:48rpx;
}

/* Info card */
.card{
  display: grid;
  grid-template-columns: 112rpx 1fr auto;
  align-items: center;
  gap: 24rpx;
  padding: 32rpx;
  border-radius: var(--radius-2xl);
  margin-top: 32rpx;
  box-shadow: var(--shadow);
  color: #111;
  overflow: hidden;
}
.card{ border-radius: 28rpx; box-shadow: 0 6px 14px rgba(0,0,0,0.08); }
.card-alert{ background:var(--card-alert, #eeb79b); }
.card-safe{ background:var(--card-safe, #90b79e); }
.card-neutral{ background:var(--card-neutral, #bdb8b5); }

/* White icon box */
.card-icon{
  width: 80rpx;
  height: 80rpx;
  border-radius: 24rpx;
  background: #fff;
  display: flex; align-items: center; justify-content: center;
  overflow: hidden;
  box-shadow: 0 2px 6px rgba(0,0,0,0.06);
}
.card-title{ font-size:48rpx; font-weight:700; color:#1d1d1d; }
.card-value{ font-size:40rpx; font-weight:800; }
.card-icon-img{ width: 44rpx; height: 44rpx; display: block; }
</style>
