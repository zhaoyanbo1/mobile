<template>
  <view class="page">
    <view class="header">
      <text class="title">我的好友</text>
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
        <view class="arrow">›</view>
      </view>
    </scroll-view>

    <view v-if="!friends.length" class="empty">
      暂无好友
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/index.js'

const friends = ref([])

async function load() {
  try {
    const res = await api.friends.getMyFriendList()
    friends.value = res || []
  } catch (e) {
    console.error('获取好友失败:', e)
    uni.showToast({title: '加载失败', icon: 'none'})
  }
}

function goChat(friend) {
  uni.navigateTo({
    url: `/pagesA/chat/friend_chat/ChatRoom?peerId=${friend.userId}&nickname=${friend.nickname}`
  })
}

onMounted(load)
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f7fb;
}

.header {
  padding: 24rpx;
  background: #fff;
  border-bottom: 1px solid #eee;
}

.title {
  font-weight: 700;
  font-size: 34rpx;
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
