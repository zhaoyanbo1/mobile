<template>
  <base-layout>
    <view class="page p-4">
      <view class="topbar"><text class="title">My Medals</text></view>

      <view v-if="loading" class="state">
        <uni-icons type="spinner-cycle" size="28" class="spin" color="#93b2a1" />
        <text class="hint">Loading medals…</text>
      </view>

      <view v-else-if="error" class="state">
        <uni-icons type="info" size="24" color="#d14343" />
        <text class="hint">{{ error }}</text>
        <button class="retry" @click="fetchMedals">Retry</button>
      </view>

      <view v-else-if="!medals.length" class="state">
        <uni-icons type="trophy" size="42" color="#BDBDBD" />
        <text class="hint">No medals yet</text>
      </view>

      <!-- Single column, one card per row filling full width -->
      <view v-else class="grid">
        <view v-for="m in medals" :key="m._key" class="card">
          <image :src="m.iconUrl || fallbackIcon" mode="aspectFit" class="icon"/>
          <view class="meta">
            <!-- Name fixed as Daily Five -->
            <text class="name">Daily Five</text>
            <text class="time" v-if="m.earnedAtFmt">{{ m.earnedAtFmt }}</text>
          </view>
        </view>
      </view>

      <!-- Full-width pill button (same color, larger font) -->
      <view class="refresh-hint">
        <button class="btn" @click="fetchMedals">Refresh</button>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import medalFallback from '@/static/medal/give_me_five.svg'

const { proxy } = getCurrentInstance()

const API_BASE = ''
const QUERY_URL = '/api/user-medals'

const medals  = ref([])
const loading = ref(false)
const error   = ref('')
const fallbackIcon = medalFallback

function getAuthHeaders () {
  const token = proxy?.$cf?.login?.getToken?.()
  return token ? { Authorization: `Bearer ${token}` } : {}
}
async function getJSON (url, params = {}) {
  const q = new URLSearchParams(params).toString()
  const full = API_BASE + url + (q ? `?${q}` : '')
  if (proxy?.$cf?.http?.get) return proxy.$cf.http.get(full)
  return new Promise((resolve, reject) => {
    uni.request({ url: full, method: 'GET', header: { ...getAuthHeaders() }, success: resolve, fail: reject })
  })
}
function fmtTime (v) {
  if (!v) return ''
  const d = new Date(String(v).replace(/-/g, '/'))
  if (isNaN(d)) return ''
  const y = d.getFullYear(), m = String(d.getMonth()+1).padStart(2,'0'), day = String(d.getDate()).padStart(2,'0')
  const hh = String(d.getHours()).padStart(2,'0'), mm = String(d.getMinutes()).padStart(2,'0')
  return `${y}-${m}-${day} ${hh}:${mm}`
}
function mapMedal (raw, i) {
  const iconUrl = raw.iconUrl || raw.icon || raw.cover || ''
  const earnedAt = raw.earnedAt || raw.earned_at || raw.createdAt || raw.createTime || raw.gmtCreate
  return { ...raw, _key: raw.id ?? raw.medalId ?? i, iconUrl, earnedAtFmt: fmtTime(earnedAt) }
}
async function fetchMedals () {
  loading.value = true; error.value = ''; medals.value = []
  try {
    const userRes = await proxy?.$cf?.login?.getLoginUser?.()
    if (!userRes?.success) { error.value = 'Please login first'; return }
    const userId = userRes.data.user_info_id
    const res = await getJSON(QUERY_URL, { userId })
    const payload = res?.data?.data ?? res?.data
    const arr = Array.isArray(payload) ? payload : (Array.isArray(payload?.list) ? payload.list : [])
    medals.value = arr.map(mapMedal)
  } catch (e) {
    error.value = 'Failed to load medals'
  } finally { loading.value = false }
}
onShow(() => { fetchMedals() })
</script>

<style scoped>
.page{
  --bg:#f8f8f8; --surface:#fff; --ink:#111827; --muted:#6b7280;
  --shadow:0 10px 22px rgba(0,0,0,.10);
  background:var(--bg);
}

/* Top bar */
.topbar{ display:flex; align-items:center; justify-content:center; height:112rpx; }
.title{ font-size:44rpx; font-weight:900; color:var(--ink); }

/* States (loading / error / empty) */
.state{ margin-top:32rpx; display:flex; flex-direction:column; align-items:center; gap:12rpx; }
.state .hint{ color:var(--muted); }
.spin{ animation: spin 1s linear infinite; }
@keyframes spin{ from{transform:rotate(0)} to{transform:rotate(360deg)} }
.retry{ margin-top:8px; padding:10px 16px; border:1px solid #ddd; border-radius:20px; }

/* Single column: one per row */
.grid{ margin-top:20rpx; display:grid; grid-template-columns: 1fr; gap: 20rpx; }

/* Taller card: large icon + larger text */
.card{
  width:100%; background:var(--surface);
  border-radius:24rpx; box-shadow:var(--shadow);
  padding: 60rpx 28rpx; min-height: 180rpx;
  display:flex; gap:24rpx; align-items:center;
}
.icon{
  width: 200rpx; height: 200rpx;
  border-radius:16rpx; background:#fff; flex: 0 0 auto;
}
.meta{ display:flex; flex-direction:column; gap:8rpx; }
/* Bigger, bolder name */
.name{ font-size: 44rpx; font-weight: 900; color:#111; }
/* Larger date */
.time{ font-size: 32rpx; color:var(--muted); }

/* Full-width pill button (same color, larger font) */
.refresh-hint{ margin-top:28rpx; display:flex; justify-content:center; }
.btn{
  width:100%; max-width: 720rpx; height: 120rpx;
  border-radius: 9999rpx; background:#839f90;
  color:#fff; font-weight:800; font-size:40rpx;
  display:flex; align-items:center; justify-content:center;
  box-shadow:0 6px 16px rgba(131,159,144,.35);
}
.btn:active{ transform: translateY(.5px) scale(.99); }
</style>
