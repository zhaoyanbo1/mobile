<template>
  <view class="page">
    <!-- 顶部栏 -->
    <view class="topbar">
      <view class="back-wrap" @click="goBack">
        <text class="back-icon"><</text>
      </view>
      <text class="title">My friend</text>
      <view class="right-spacer"></view>
    </view>


    <scroll-view scroll-y class="list">
      <view
          v-for="item in friends"
          :key="item.userId"
          class="friend-card"
          @click="goChat(item)"
      >
        <view class="info">
          <text class="name">{{ item.nickname || ('用户' + item.userId) }}</text>
        </view>
        <view class="meta">
          <view v-if="getUnreadCount(item)" class="badge">{{ formatBadge(getUnreadCount(item)) }}</view>
          <view class="arrow">›</view>
        </view>
      </view>
    </scroll-view>

    <view v-if="!friends.length" class="empty">
      暂无好友
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import api from '@/api/index.js'
import  useNotificationStore  from '@/api/utils/notificationStore'

const friends = ref([])
const notifications = useNotificationStore()

const getUnreadCount = (friend) => notifications.getFriendUnread(friend?.userId)

const formatBadge = (count) => {
  if (!count) return ''
  return count > 99 ? '99+' : String(count)
}

async function load() {
  try {
    const res = await api.friends.getMyFriendList()
    const list = Array.isArray(res) ? res : []
    friends.value = list
    notifications.applyFriendList(list)
  } catch (e) {
    console.error('获取好友失败:', e)
    uni.showToast({title: '加载失败', icon: 'none'})
  }
}
const goBack = () => {
  history.length > 1 ? history.back() : null
}
function goChat(friend) {
  notifications.setConversationForFriend(friend?.userId, friend?.conversationId)
  notifications.markFriendRead(friend?.userId)
  if (friend?.conversationId) {
    notifications.markConversationRead(friend.conversationId)
  }
  uni.navigateTo({
    url: `/pagesA/chat/friend_chat/ChatRoom?peerId=${friend.userId}&nickname=${encodeURIComponent(friend.nickname || ('用户' + friend.userId))}${friend.conversationId ? `&conversationId=${friend.conversationId}` : ''}`
  })
}

onMounted(load)
onShow(load)
</script>

<style scoped>

.topbar {
  display: grid;
  grid-template-columns: 80rpx 1fr 80rpx;
  align-items: center;          /* 让三列都居中 */
  height: 112rpx;
  background: #fff;
  padding: 0 24rpx;
}

/* 包一层，用 flex 真正居中 */
.back-wrap {
  display: flex;
  align-items: center;          /* 垂直居中箭头 */
  height: 100%;
}

/* 箭头本身 */
.back-icon {
  font-size: 80rpx;             /* 不要 110rpx 太大就显得掉得更厉害 */
  color: #2f3d2f;
  line-height: 1;
  transform: scaleX(0.75);      /* 瘦一点 */
  transform-origin: left center;
  padding: 0 8rpx;
  /* 微调往上提一点点，让视觉更平 */
  margin-top: -4rpx;
}
.title {
  text-align: center;
  font-size: 46rpx;
  font-weight: 700;
  color: #1d1d1d;
}
.right-spacer {
  width: 80rpx;
}


.page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f7fb;
}

.list {
  flex: 1;
  padding: 16rpx;
}

.friend-card {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 16rpx;
  padding: 24rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.04);
}

.info {
  display: flex;
  flex-direction: column;
}

.meta {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.badge {
  min-width: 48rpx;
  padding: 0 16rpx;
  height: 40rpx;
  border-radius: 999rpx;
  background: #f87171;
  color: #fff;
  font-size: 24rpx;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
}

.name {
  font-size: 30rpx;
  font-weight: 600;
}

.arrow {
  font-size: 40rpx;
  color: #999;
}

.empty {
  text-align: center;
  color: #888;
  padding-top: 200rpx;
}
</style>
