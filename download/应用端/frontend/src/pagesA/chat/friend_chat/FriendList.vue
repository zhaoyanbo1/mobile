<template>
  <view class="page">
    <!-- top bar -->
    <view class="topbar">
      <view class="back-wrap" @click="goBack">
        <text class="back-icon"><</text>
      </view>
      <text class="title">My friends</text>
      <view class="right-spacer"></view>
    </view>

    <!-- tabs -->
    <view class="tabs">
      <view
          class="tab"
          :class="{ active: activeTab === 'friends' }"
          @click="activeTab = 'friends'"
      >
        Friend list
      </view>
      <view
          class="tab"
          :class="{ active: activeTab === 'applications' }"
          @click="activeTab = 'applications'"
      >
        Application list
        <text v-if="applicationCount" class="tab-badge">{{ applicationCount }}</text>
      </view>
    </view>

    <!-- friend list -->
    <scroll-view
        v-if="activeTab === 'friends'"
        scroll-y
        class="list"
    >
      <view
          v-for="item in friends"
          :key="item.userId"
          class="friend-card"
          @click="goChat(item)"
      >
        <view class="info">
          <text class="name">{{ item.nickname || ('User ' + item.userId) }}</text>
        </view>
        <view class="meta">
          <view v-if="getUnreadCount(item)" class="badge">{{ formatBadge(getUnreadCount(item)) }}</view>
          <view class="arrow">›</view>
        </view>
      </view>

      <view v-if="!friends.length && !friendsLoading" class="empty">
        After obtaining at least one medal, you can add friends to the medal leaderboard.
      </view>
    </scroll-view>

    <!-- application list -->
    <scroll-view
        v-else
        scroll-y
        class="list"
    >
      <view
          v-for="(item, idx) in applications"
          :key="(item.friendRequestId || item.userId || idx) + '-apply'"
          class="friend-card app-card"
      >
        <view class="info">
          <text class="name">
            {{ item.username || item.nickname || ('User ' + (item.userId || item.fromUserId)) }}
          </text>
          <text class="sub">wants to add you as a friend</text>
        </view>

        <view class="app-actions">
          <button class="app-btn ok" @click.stop="acceptApplication(item)">Accept</button>
          <button class="app-btn" @click.stop="declineApplication(item)">Decline</button>
        </view>
      </view>

      <view v-if="!applications.length && !applicationsLoading" class="empty">
        No applications
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import api from '@/api/index.js'
import useNotificationStore from '@/api/utils/notificationStore'

// change to your real api paths
import {
  getLeaderboard,
  acceptFriendRequest,
  declineFriendRequest
} from '@/api/page/medals.js'

const friends = ref([])
const friendsLoading = ref(false)

const applications = ref([])
const applicationsLoading = ref(false)

const activeTab = ref('friends')

const notifications = useNotificationStore()

const getUnreadCount = (friend) => notifications.getFriendUnread(friend?.userId)

const formatBadge = (count) => {
  if (!count) return ''
  return count > 99 ? '99+' : String(count)
}

const applicationCount = computed(() => applications.value.length)

async function loadFriends() {
  try {
    friendsLoading.value = true
    const res = await api.friends.getMyFriendList()
    const list = Array.isArray(res) ? res : []
    friends.value = list
    notifications.applyFriendList(list)
  } catch (e) {
    console.error('Failed to fetch friends:', e)
    uni.showToast({ title: 'Failed to load friends', icon: 'none' })
  } finally {
    friendsLoading.value = false
  }
}

async function loadApplications() {
  try {
    applicationsLoading.value = true
    const me = uni.getStorageSync('me') || uni.getStorageSync('userInfo') || {}
    const userId = me.userId || me.user_info_id || ''
    const resp = await getLeaderboard(userId, 1, 100)
    const list = Array.isArray(resp?.list) ? resp.list : []
    const pendingIn = list.filter(x => (x.friendStatus || '').toUpperCase() === 'PENDING_IN')
    applications.value = pendingIn
  } catch (e) {
    console.error('Failed to fetch applications:', e)
    uni.showToast({ title: 'Failed to load applications', icon: 'none' })
  } finally {
    applicationsLoading.value = false
  }
}

const goBack = () => {
  if (typeof uni !== 'undefined' && uni.navigateBack) {
    uni.navigateBack()
  } else {
    history.length > 1 ? history.back() : null
  }
}

function goChat(friend) {
  notifications.setConversationForFriend(friend?.userId, friend?.conversationId)
  notifications.markFriendRead(friend?.userId)
  if (friend?.conversationId) {
    notifications.markConversationRead(friend.conversationId)
  }
  uni.navigateTo({
    url: `/pagesA/chat/friend_chat/ChatRoom?peerId=${friend.userId}&nickname=${encodeURIComponent(friend.nickname || ('User ' + friend.userId))}${friend.conversationId ? `&conversationId=${friend.conversationId}` : ''}`
  })
}

async function acceptApplication(item) {
  try {
    await acceptFriendRequest(item.friendRequestId)
    uni.showToast({ title: 'Accepted', icon: 'success' })
    applications.value = applications.value.filter(a => a.friendRequestId !== item.friendRequestId)
    loadFriends()
  } catch (e) {
    console.error(e)
    uni.showToast({ title: 'Action failed', icon: 'none' })
  }
}

async function declineApplication(item) {
  try {
    await declineFriendRequest(item.friendRequestId)
    uni.showToast({ title: 'Declined', icon: 'success' })
    applications.value = applications.value.filter(a => a.friendRequestId !== item.friendRequestId)
  } catch (e) {
    console.error(e)
    uni.showToast({ title: 'Action failed', icon: 'none' })
  }
}

onMounted(() => {
  loadFriends()
  loadApplications()
})
onShow(() => {
  loadFriends()
  loadApplications()
})
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f7fb;
}

/* top bar */
.topbar {
  display: grid;
  grid-template-columns: 80rpx 1fr 80rpx;
  align-items: center;
  height: 112rpx;
  background: #fff;
  padding: 0 24rpx;
}
.back-wrap {
  display: flex;
  align-items: center;
  height: 100%;
}
.back-icon {
  font-size: 80rpx;
  color: #2f3d2f;
  line-height: 1;
  transform: scaleX(0.75);
  transform-origin: left center;
  padding: 0 8rpx;
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

/* tabs */
.tabs {
  display: flex;
  gap: 16rpx;
  padding: 16rpx 16rpx 0;
  background: #f5f7fb;
}
.tab {
  flex: 1;
  background: #fff;
  border-radius: 20rpx;
  text-align: center;
  padding: 16rpx 0;
  font-size: 28rpx;
  color: #4b5563;
  position: relative;
}
.tab.active {
  background: #7e8c77;
  color: #fff;
  font-weight: 600;
}
.tab-badge {
  position: absolute;
  top: -12rpx;
  right: 20rpx;
  background: #f87171;
  color: #fff;
  font-size: 22rpx;
  border-radius: 999rpx;
  padding: 2rpx 12rpx;
}

/* list body */
.list {
  flex: 1;
  padding: 16rpx;
}

/* cards */
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
.sub {
  margin-top: 6rpx;
  font-size: 24rpx;
  color: #888;
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
.arrow {
  font-size: 40rpx;
  color: #999;
}
.empty {
  text-align: center;
  color: #888;
  padding-top: 200rpx;
}

/* application actions */
.app-card {
  gap: 20rpx;
}
.app-actions {
  display: flex;
  gap: 16rpx;
}
.app-btn {
  border: none;
  background: #edf0f5;
  color: #333;
  border-radius: 20rpx;  /* 这里太圆了 */
  padding: 10rpx 20rpx;
  font-size: 26rpx;
}

.app-btn.ok {
  background: #7e8c77;
  color: #fff;
}

</style>
