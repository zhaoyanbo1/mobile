<template>
  <base-layout>
    <!-- 顶部栏 -->
    <view class="topbar">
      <!-- ✅ 点击这里退出并回登录页 -->
      <image
          :src="icons.menu"
          mode="widthFix"
          class="icon"
          @click="logoutAndGoLogin"
      />
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
            <view v-if="friendBadgeCount" class="badge-indicator">{{ friendBadgeDisplay }}</view>
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
            <view v-if="activityBadgeCount" class="badge-indicator">{{ activityBadgeDisplay }}</view>
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
import { computed, getCurrentInstance } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { MessageCircle, Users, ListChecks } from 'lucide-vue-next'
import menuIcon from '@/static/gg_menu-left-alt.svg'
import api from '@/api/index.js'
import activitiesApi from '@/api/page/activities.js'
import useNotificationStore from '@/api/utils/notificationStore'

const { proxy } = getCurrentInstance()
const icons = { menu: menuIcon }

/**
 * ⚠️ 这里是你登录页的路径，按你的项目改
 * 我先写成 /pages/login/index
 */
const LOGIN_PAGE = '/pages/login/index'

const navTo = (url: string) => {
  if (proxy?.$cf?.navigate?.to) proxy.$cf.navigate.to({ url, type: 'page', mode: 'navigate' })
  else uni.navigateTo({ url })
}

const goAIChat = () => navTo('/pagesA/chat/ai_chat/index')
const goFriendChat = () => navTo('/pagesA/chat/friend_chat/FriendList')
const goLinkedTasks = () => navTo('/pagesA/activities/index')

/* ===== 徽标 ===== */
const notifications = useNotificationStore()
const friendBadgeCount = notifications.friendUnreadTotal
const activityBadgeCount = notifications.activityPendingCount

const formatBadge = (count: number) => {
  if (!count) return ''
  return count > 99 ? '99+' : String(count)
}

const friendBadgeDisplay = computed(() => formatBadge(friendBadgeCount.value))
const activityBadgeDisplay = computed(() => formatBadge(activityBadgeCount.value))

/* ===== 刷新红点 ===== */
async function refreshFriendNotifications () {
  try {
    const res = await api.friends.getMyFriendList()
    const list = Array.isArray(res) ? res : []
    notifications.applyFriendList(list)
  } catch (error) {
    console.warn('Failed to refresh friend notifications', error)
  }
}

async function refreshActivityNotifications () {
  try {
    const uid = uni.getStorageSync('uid')
    const params = uid ? { userId: uid } : {}
    const response = await activitiesApi.overview(params)
    const activities = Array.isArray(response?.data?.activities) ? response.data.activities : []
    const pendingTotal = activities
        .filter((activity: any) => activity?.host)
        .reduce(
            (sum: number, activity: any) =>
                sum + (Array.isArray(activity?.pendingApplicants) ? activity.pendingApplicants.length : 0),
            0
        )
    notifications.setActivityPendingCount(pendingTotal)
  } catch (error) {
    console.warn('Failed to refresh activity notifications', error)
    notifications.setActivityPendingCount(0)
  }
}

async function refreshBadges () {
  await Promise.all([refreshFriendNotifications(), refreshActivityNotifications()])
}

/* ===== 退出登录并回登录页 ===== */
async function logoutAndGoLogin () {
  try {
    // 1. 尝试调后端/框架的 logout（如果有的话就能把服务端会话也清了）
    await proxy?.$cf?.login?.logout?.()
  } catch (e) {
    // 没有也没关系，本地清就行
  }

  // 2. 把本地的登录相关都清掉，跟你登录成功时存的 key 对应
  uni.removeStorageSync('token')
  uni.removeStorageSync('h5_token')
  uni.removeStorageSync('me')
  uni.removeStorageSync('user')
  uni.removeStorageSync('uid')

  // 有些项目会把当前登录用户写到全局变量里，这里也清一下，防止“自动填回去”
  try {
    await proxy?.$cf?.globalVariable?.write?.({ variableName: 'h5_token', value: '' })
  } catch (e) {}
  try {
    await proxy?.$cf?.globalVariable?.write?.({ variableName: 'currentUser', value: '' })
  } catch (e) {}

  // 3. 回到登录页，用 reLaunch 防止用户点返回又回到 Social
  uni.reLaunch({ url: LOGIN_PAGE })
}

onShow(() => {
  refreshBadges()
})
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
.button-wrapper {
  padding: 0 30rpx;
}
.feature-btn {
  width: 100%;
  border: none;
  background: #fff;
  border-radius: 1.4rem;
  padding: 1rem 1.4rem;
  display: flex;
  align-items: center;
  text-align: left;
  position: relative;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.06);
  transition: all 0.25s ease;
}
.badge-indicator {
  position: absolute;
  top: 16rpx;
  right: 24rpx;
  min-width: 42rpx;
  height: 42rpx;
  padding: 0 14rpx;
  border-radius: 999rpx;
  background: #f87171;
  color: #fff;
  font-size: 24rpx;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 10rpx rgba(248, 113, 113, 0.35);
}
.feature-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
}
.icon-circle {
  width: 3rem;
  height: 3rem;
  border-radius: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(145deg, #a3b18a, #7e8c77);
}
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
