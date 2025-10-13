<template>
  <base-layout>
    <view class="win-page" :class="{ ready: !loading }" @click="handleTap">
      <image :src="medalIcon" mode="aspectFit" class="trophy" />
      <text class="title">{{ medalName }}</text>

      <view class="status">
        <template v-if="loading">
          <!-- Larger loading icon -->
          <uni-icons type="spinner-cycle" size="32" class="spin" color="#93b2a1" />
          <text class="hint">Claiming your medal…</text>
        </template>
        <template v-else-if="error">
          <uni-icons type="info" size="28" color="#d14343" />
          <text class="hint">{{ error }}</text>
        </template>
        <template v-else>
          <uni-icons type="checkmarkempty" size="28" color="#1BAA5F" />
          <text class="hint">{{ claimedText }}</text>
        </template>
      </view>

      <!-- <text class="tap-hint" v-if="!loading">Tap anywhere to view all medals</text> -->
    </view>
  </base-layout>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
import { onLoad, onShow } from '@dcloudio/uni-app'
import defaultMedalIcon from '@/static/medal/give_me_five.svg'

const { proxy } = getCurrentInstance()

/** Config */
const API_BASE = ''
const COLLECT_URL = '/api/user-medals'
const DEFAULT_MEDAL_ID = 2              // Matches backend
const DEFAULT_MEDAL_NAME = 'Daily Five' // Default name
const DEFAULT_ICON = defaultMedalIcon
const MEDAL_LIST_PAGE = '/pagesA/todo/show_modal/index'

/** State */
const loading = ref(true)
const error   = ref('')
const claimed = ref(false)
const medalId   = ref(DEFAULT_MEDAL_ID)
const medalName = ref(DEFAULT_MEDAL_NAME)
const medalIcon = ref(DEFAULT_ICON)
const claimedText = ref('Medal collected')

/** Helpers */
function getAuthHeaders () {
  const token = proxy?.$cf?.login?.getToken?.()
  return token ? { Authorization: `Bearer ${token}` } : {}
}
async function postJSON (url, data) {
  if (proxy?.$cf?.http?.post) return proxy.$cf.http.post(API_BASE + url, data)
  return new Promise((resolve, reject) => {
    uni.request({
      url: API_BASE + url,
      method: 'POST',
      header: { 'Content-Type': 'application/json', ...getAuthHeaders() },
      data,
      success: resolve,
      fail: reject
    })
  })
}

/** Claim once */
async function claimMedal () {
  loading.value = true; error.value = ''; claimed.value = false
  try {
    const userRes = await proxy?.$cf?.login?.getLoginUser?.()
    if (!userRes?.success) { error.value = 'Please login first'; return }
    const userId = userRes.data.user_info_id

    const payload = { userId, medalId: medalId.value }
    const res = await postJSON(COLLECT_URL, payload)

    const created = res?.data?.created ?? res?.data?.data?.created ?? res?.data === true
    // claimedText.value = created === false ? 'Already collected' : 'Medal collected'
    claimed.value = true
    proxy.$cf.toast?.({ message: claimedText.value, level: 'success' })
  } catch (e) {
    error.value = 'Claim failed'
  } finally {
    loading.value = false
  }
}

/** Tap anywhere to go to the medal list page */
function goShowModel () {
  const url = MEDAL_LIST_PAGE
  if (proxy?.$cf?.navigate?.to) proxy.$cf.navigate.to({ url, type: 'page', mode: 'navigate' })
  else uni.navigateTo({ url })
}
function handleTap () {
  if (loading.value) return
  goShowModel()
}

/** Optional route params: medalId/name/icon */
onLoad((opts) => {
  if (opts?.medalId) {
    const idNum = Number(opts.medalId)
    if (!Number.isNaN(idNum)) medalId.value = idNum
  }
  if (opts?.name) medalName.value = decodeURIComponent(opts.name)
  if (opts?.icon) medalIcon.value = decodeURIComponent(opts.icon)
})
onShow(() => { claimMedal() })
</script>

<style scoped>
/* Page container: a bit more padding, keep neutral background */
.win-page{
  min-height: 100vh;
  padding: 88rpx 56rpx;               /* larger inner spacing */
  background: #f8f8f8;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  text-align: center;
  cursor: pointer;                    /* H5: show pointer cursor */
}

/* Bigger trophy with stronger shadow */
.trophy{
  width: 320rpx;                      /* 200rpx → 280rpx */
  height: 320rpx;
  margin-bottom: 24rpx;               /* 16rpx → 24rpx */
  filter: drop-shadow(0 10px 22px rgba(0,0,0,.14));
}

/* Larger, bolder title */
.title{
  font-size: 60rpx;                   /* 44rpx → 56rpx */
  line-height: 1.15;
  font-weight: 900;
  letter-spacing: 0.4rpx;
  color: #111827;
}

/* Status row and hint text */
.status{
  margin-top: 16rpx;                  /* 10rpx → 16rpx */
  display: inline-flex; align-items: center; gap: 10rpx;
}
.hint{
  color: #6b7280;
  font-size: 40rpx;                   /* 28rpx → 34rpx */
  line-height: 1.4;
}

/* Spinner animation */
.spin{ animation: spin 1s linear infinite; }
@keyframes spin { from{transform: rotate(0)} to{transform: rotate(360deg)} }

/* Optional helper text (if enabled) */
.tap-hint{ margin-top: 28rpx; color: #6b7280; font-size: 30rpx; }

/* Slightly larger on bigger devices (H5/Pad) */
@media (min-width: 420px){
  .trophy{ width: 320rpx; height: 320rpx; }
  .title{ font-size: 60rpx; }
  .hint{ font-size: 36rpx; }
}
</style>
