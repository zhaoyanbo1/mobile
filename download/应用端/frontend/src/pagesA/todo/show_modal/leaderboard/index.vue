<template>
  <view class="page">
    <!-- Header（无返回箭头） -->
    <view class="header">
      <view class="header-inner">
        <view class="title-left">
          <text class="title-text">Daily Leaderboard</text>
        </view>
        <view class="badge">
          <text class="badge-icon">👥</text>
          <text class="badge-text">{{ memberCount }} members</text>
        </view>
      </view>
    </view>

    <view class="content">
      <!-- 加载中 / 空 / 错误 -->
      <view v-if="loading" class="state-tip">Loading…</view>
      <view v-else-if="err" class="state-tip error">Failed: {{ err }}</view>
      <view v-else-if="!rows || !rows.length" class="state-tip">No rankings yet.</view>

      <!-- Podium（有数据才渲染） -->
      <view class="podium" v-if="rows && rows.length">
        <!-- 2 -->
        <view class="slot silver" v-if="rows[1]">
          <view class="person">
            <view class="avatar-wrap">
              <image class="avatar-big" :src="getAvatarByRank(2)" />
              <view class="badge-circle silver-bg">🥈</view>

              <!-- 头像右下角加好友按钮 -->
              <view
                  v-if="!isMe(rows[1]) && !isFriend(rows[1])"
                  class="friend-fab"
                  :class="{ on: statusOf(rows[1]) !== 'NONE' }"
                  @tap.stop="onPlusTap(rows[1])"
              >
                <text class="friend-fab-icon">
                  {{ statusOf(rows[1]) === 'FRIEND' ? '✓' : '+' }}
                </text>
              </view>
            </view>

            <text class="person-name">{{ rows[1].username }}</text>
            <view class="person-value">
              <text>{{ rows[1].medalCount }}</text>
              <text class="medal-icon">🏅</text>
            </view>
          </view>
          <view class="stand stand-2"><text class="rank-large">2</text></view>
        </view>

        <!-- 1 -->
        <view class="slot gold" v-if="rows[0]">
          <view class="person">
            <view class="avatar-wrap">
              <image class="avatar-hero" :src="getAvatarByRank(1)" />
              <view class="badge-circle gold-bg">🥇</view>

              <!-- 头像右下角加好友按钮 -->
              <view
                  v-if="!isMe(rows[0]) && !isFriend(rows[0])"
                  class="friend-fab"
                  :class="{ on: statusOf(rows[0]) !== 'NONE' }"
                  @tap.stop="onPlusTap(rows[0])"
              >
                <text class="friend-fab-icon">
                  {{ statusOf(rows[0]) === 'FRIEND' ? '✓' : '+' }}
                </text>
              </view>
            </view>

            <text class="person-name">{{ rows[0].username }}</text>
            <view class="person-value">
              <text>{{ rows[0].medalCount }}</text>
              <text class="medal-icon">🏅</text>
            </view>
          </view>
          <view class="stand stand-1"><text class="rank-large">1</text></view>
        </view>

        <!-- 3 -->
        <view class="slot bronze" v-if="rows[2]">
          <view class="person">
            <view class="avatar-wrap">
              <image class="avatar-big" :src="getAvatarByRank(3)" />
              <view class="badge-circle bronze-bg">🥉</view>

              <!-- 头像右下角加好友按钮 -->
              <view
                  v-if="!isMe(rows[2]) && !isFriend(rows[2])"
                  class="friend-fab"
                  :class="{ on: statusOf(rows[2]) !== 'NONE' }"
                  @tap.stop="onPlusTap(rows[2])"
              >
                <text class="friend-fab-icon">
                  {{ statusOf(rows[2]) === 'FRIEND' ? '✓' : '+' }}
                </text>
              </view>
            </view>

            <text class="person-name">{{ rows[2].username }}</text>
            <view class="person-value">
              <text>{{ rows[2].medalCount }}</text>
              <text class="medal-icon">🏅</text>
            </view>
          </view>
          <view class="stand stand-3"><text class="rank-large">3</text></view>
        </view>
      </view>

      <!-- 其他排名 -->
      <view class="list" v-if="rows && rows.length">
        <text class="list-title">Other Rankings</text>
        <view
            v-for="(item, idx) in rows.slice(3)"
            :key="item.userId + '-' + idx"
            class="list-item"
        >
          <view class="rank-box">{{ idx + 4 }}</view>

          <!-- 头像包一层用于放右下角按钮 -->
          <view class="avatar-cell">
            <image class="list-avatar" :src="getAvatarByRank(idx + 4)" />
            <view
                v-if="!isMe(item) && !isFriend(item)"
                class="friend-fab small"
                :class="{ on: statusOf(item) !== 'NONE' }"
                @tap.stop="onPlusTap(item)"
            >
              <text class="friend-fab-icon">
                {{ statusOf(item) === 'FRIEND' ? '✓' : '+' }}
              </text>
            </view>
          </view>

          <view class="list-info">
            <text class="list-name">{{ item.username }}</text>
            <text class="streak">Streak: {{ item.streak || 0 }} days</text>
          </view>
          <view class="list-right">
            <text class="list-score">{{ item.medalCount }} 🏅</text>
          </view>
        </view>
      </view>
    </view>

    <!-- ✅ 固定底部 My Rank Card -->
    <view v-if="me" class="my-rank-fixed">
      <view class="my-rank-card">
        <view class="left">
          <image class="avatar" :src="getAvatarByRank(me.rank)" />
          <view class="info">
            <text class="info-label">My Rank</text>
            <text class="rank-text">#{{ me.rank }}</text>
          </view>
        </view>
        <view class="right">
          <text class="right-label">Medals </text>
          <text class="right-value">{{ me.medalCount }} 🏅</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/api/page/medals'
// 这里引一次总的 api，用它拿好友列表
import baseApi from '@/api/index.js'
import {
  createFriendRequest,
  acceptFriendRequest,
  declineFriendRequest,
} from '@/api/page/medals'

export default {
  data() {
    return {
      rows: [],
      me: null,
      page: 1,
      size: 20,
      total: 0,
      loading: false,
      err: '',
      // 我自己的好友 id 集合
      friendIds: [],
      avatarPool: [
        'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200&h=200&fit=crop',
        'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200&h=200&fit=crop',
        'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=200&fit=crop',
        'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&h=200&fit=crop',
        'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200&h=200&fit=crop',
        'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&h=200&fit=crop',
        'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&h=200&fit=crop',
        'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&h=200&fit=crop',
        'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200&h=200&fit=crop',
        'https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=200&h=200&fit=crop',
      ],
    }
  },
  computed: {
    memberCount() {
      return this.total || (this.rows ? this.rows.length : 0)
    },
  },
  methods: {
    /* 工具 */
    getAvatarByRank(rank) {
      const idx = (rank - 1) % this.avatarPool.length
      return this.avatarPool[idx]
    },
    isMe(item) {
      return (
          this.me &&
          (item.userId === this.me.userId || item.userId === this.me.user_info_id)
      )
    },
    // 新增：判断这个榜单用户是不是我好友
    isFriend(item) {
      if (!item) return false
      const uid = item.userId || item.user_info_id
      if (!uid) return false
      return this.friendIds.includes(String(uid))
    },
    statusOf(item) {
      return item.friendStatus || 'NONE'
    },

    // 右下角按钮点击
    async onPlusTap(item) {
      const status = this.statusOf(item)
      try {
        if (status === 'NONE') {
          const resp = await createFriendRequest(item.userId)
          item.friendStatus = 'PENDING_OUT'
          item.friendRequestId = resp?.id
          uni.showToast({ title: 'Application sent', icon: 'none' })
        } else if (status === 'PENDING_IN') {
          await acceptFriendRequest(item.friendRequestId)
          item.friendStatus = 'FRIEND'
          uni.showToast({ title: 'Has become a friend', icon: 'none' })
        } else if (status === 'FRIEND') {
          await declineFriendRequest(item.friendRequestId)
          item.friendStatus = 'NONE'
          item.friendRequestId = null
          uni.showToast({ title: 'Friendship relationship has been cancelled.', icon: 'none' })
        } else if (status === 'PENDING_OUT') {
          uni.showToast({ title: '已发送申请，待对方同意', icon: 'none' })
        }
      } catch (e) {
        uni.showToast({ title: e?.data?.message || '操作失败', icon: 'none' })
      }
    },

    // 拉好友列表，只为了“进去就知道谁是好友”
    async loadMyFriends() {
      try {
        const res = await baseApi.friends.getMyFriendList()
        const list = Array.isArray(res) ? res : []
        // 统一存成字符串，后面好比
        this.friendIds = list
            .map((f) => f.userId || f.user_info_id)
            .filter(Boolean)
            .map((x) => String(x))
      } catch (e) {
        // 拉失败也不要影响榜单显示
        this.friendIds = []
      }
    },

    /* 数据加载 */
    async fetchData(initial = false) {
      try {
        this.loading = true
        const resp = await api.getLeaderboard(undefined, this.page, this.size)
        const list = resp.list ?? resp.records ?? resp.data ?? []
        this.rows = initial ? list : this.rows.concat(list)
        this.total = resp.total ?? resp.totalCount ?? this.rows.length

        if (resp.me || resp.self) {
          this.me = resp.me ?? resp.self
        } else {
          const localUser = uni.getStorageSync('user')
          if (localUser && localUser.userId) {
            const foundIdx = list.findIndex(
                (x) =>
                    x.userId === localUser.userId ||
                    x.user_info_id === localUser.userId
            )

            if (foundIdx !== -1) {
              const found = list[foundIdx]
              this.me = {
                userId: localUser.userId,
                username: found.username || localUser.username,
                medalCount: found.medalCount || 0,
                rank: foundIdx + 1,
                avatarUrl: localUser.avatarUrl,
              }
            } else {
              this.me = {
                userId: localUser.userId,
                username: localUser.username,
                medalCount: 0,
                rank: '-',
                avatarUrl: localUser.avatarUrl,
              }
            }
          } else {
            this.me = null
          }
        }
      } catch (e) {
        this.err = String(e?.message || e || 'fetch error')
      } finally {
        this.loading = false
      }
    },
  },
  async onLoad() {
    // 先把好友拉出来
    await this.loadMyFriends()
    // 再拉榜单
    this.fetchData(true)
  },
}
</script>

<style scoped>
.page { background:#f7f5f0; min-height:100vh; }

/* Header */
.header{
  background:linear-gradient(to bottom,#a3b18a,#7e8c77);
  padding:40rpx 48rpx 32rpx;
  border-bottom-left-radius:48rpx; border-bottom-right-radius:48rpx;
}
.header-inner{ display:flex; justify-content:space-between; align-items:flex-start; }
.title-left{ display:flex; align-items:flex-start; }
.title-text{ font-size:44rpx; font-weight:700; color:#f7f5f0; line-height:1.2; }
.badge{ background:#fff; border-radius:9999rpx; padding:8rpx 20rpx; display:flex; align-items:center; }
.badge-icon{ font-size:28rpx; margin-right:6rpx; }
.badge-text{ color:#7e8c77; font-weight:600; }

/* My rank */
.my-rank-card{
  background:#fff; border:2rpx solid #a3b18a; border-radius:36rpx;
  box-shadow:0 8rpx 20rpx rgba(0,0,0,.08);
  margin:-36rpx 32rpx 40rpx; padding:36rpx;
  display:flex; justify-content:space-between; align-items:center;
}
.my-rank-card .avatar{ width:120rpx; height:120rpx; border-radius:50%; border:6rpx solid #a3b18a; object-fit:cover; }
.left{ display:flex; align-items:center; gap:24rpx; }
.info-label{ font-size:32rpx; color:#a3b18a; font-weight:700; }
.rank-text{ font-size:36rpx; color:#a3b18a; font-weight:800; }
.right{ text-align:right; }
.right-label{ font-size:30rpx; color:#7e8c77; font-weight:700; }
.right-value{ font-size:38rpx; color:#ddb892; font-weight:800; }

/* 状态 */
.state-tip{ padding:32rpx; color:#7e8c77; text-align:left; }
.state-tip.error{ color:#b94a48; }

/* 名字 */
.person-name {
  margin-top: 16rpx;
  font-size: 28rpx;
  font-weight: 700;
  color: #333;
  max-width: 180rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
  display: block;
}

/* Podium */
.podium{ display:flex; justify-content:center; align-items:flex-end; gap:48rpx; margin:72rpx 24rpx; }
.slot{ flex:1; text-align:center; }
.avatar-wrap{ position:relative; display:inline-block; padding:10rpx; border-radius:9999rpx; background:#fff; box-shadow:0 6rpx 16rpx rgba(0,0,0,.15); }
.avatar-hero{ width:180rpx; height:180rpx; border-radius:50%; object-fit:cover; }
.avatar-big{ width:150rpx; height:150rpx; border-radius:50%; object-fit:cover; }

.badge-circle{ position:absolute; top:-20rpx; right:-20rpx; width:72rpx; height:72rpx; border-radius:50%; color:#fff;
  display:flex; align-items:center; justify-content:center; font-size:34rpx; font-weight:800;
}
.gold-bg{ background:linear-gradient(145deg,#ffd700,#e6be8a); }
.silver-bg{ background:linear-gradient(145deg,#c0c0c0,#a9a9a9); }
.bronze-bg{ background:linear-gradient(145deg,#cd7f32,#8b4513); }

.stand{ border-radius:28rpx 28rpx 0 0; color:#fff; font-weight:800; display:flex; justify-content:center; align-items:center; box-shadow:0 8rpx 18rpx rgba(0,0,0,.08); }
.rank-large{ font-size:48rpx; font-weight:900; }
.stand-1{ height:200rpx; background:linear-gradient(to bottom,#e6c791,#ddb892); }
.stand-2{ height:160rpx; background:linear-gradient(to bottom,#98a18d,#7e8c77); }
.stand-3{ height:130rpx; background:linear-gradient(to bottom,#b0bb9f,#a3b18a); }

/* 头像右下角的加好友圆形按钮 */
.friend-fab{
  position:absolute;
  right:-20rpx;
  bottom:-20rpx;
  width:72rpx;
  height:72rpx;
  border-radius:50%;
  background:linear-gradient(145deg,#a3b18a,#7e8c77);
  color:#fff;
  display:flex; align-items:center; justify-content:center;
  box-shadow:0 8rpx 16rpx rgba(0,0,0,.15);
}
.friend-fab.small{
  width:56rpx; height:56rpx; right:-14rpx; bottom:-14rpx;
}
.friend-fab.on{
  background:linear-gradient(145deg,#7e8c77,#5f6a5f);
}
.friend-fab-icon{
  font-size:40rpx; font-weight:900; line-height:1;
}

/* 其他排名列表 */
.list{ padding-bottom:80rpx; }
.list-title{ font-size:28rpx; color:#7e8c77; margin:8rpx 0 24rpx 32rpx; }
.list-item{
  background:#fff; border-radius:28rpx; margin:16rpx 32rpx; padding:24rpx 28rpx;
  display:flex; align-items:center; gap:20rpx; border:1rpx solid rgba(126,140,119,.3); box-shadow:0 3rpx 8rpx rgba(0,0,0,.05);
}
.rank-box{ width:64rpx; height:64rpx; border-radius:24rpx; background:#eae9e3; color:#7e8c77; font-weight:700; display:flex; align-items:center; justify-content:center; }
.avatar-cell{ position:relative; display:inline-block; }
.list-avatar{ width:90rpx; height:90rpx; border-radius:50%; object-fit:cover; }
.list-info{ flex:1; }
.list-name{ color:#6b806a; font-size:30rpx; font-weight:700; }
.streak{ color:#7e8c7780; font-size:26rpx; margin-top:6rpx; }
.list-right{ text-align:right; }
.list-score{ font-size:34rpx; color:#ddb892; font-weight:800; }

/* 固定底部 My Rank Card 样式 */
.my-rank-fixed {
  position: fixed;
  bottom: 24rpx;
  left: 0;
  right: 0;
  z-index: 100;
  display: flex;
  justify-content: center;
  background: transparent;
}
.my-rank-card {
  width: 92%;
  max-width: 700rpx;
  background: #ffffff;
  border: 6rpx solid #a3b18a;
  border-radius: 28rpx;
  box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.08);
  padding: 24rpx 36rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.my-rank-card .avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  border: 4rpx solid #a3b18a;
  object-fit: cover;
}
.my-rank-card .left {
  display: flex;
  align-items: center;
  gap: 20rpx;
}
.my-rank-card .info-label {
  font-size: 30rpx;
  color: #a3b18a;
  font-weight: 700;
}
.my-rank-card .rank-text {
  font-size: 34rpx;
  color: #a3b18a;
  font-weight: 800;
}
.my-rank-card .right {
  text-align: right;
}
.my-rank-card .right-label {
  font-size: 28rpx;
  color: #7e8c77;
  font-weight: 700;
}
.my-rank-card .right-value {
  font-size: 34rpx;
  color: #ddb892;
  font-weight: 800;
}
</style>
