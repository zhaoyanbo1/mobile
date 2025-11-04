<template>
  <base-layout>
    <view class="page">
      <!-- 顶部栏 -->
      <view class="topbar">
        <image :src="icons.menu" mode="widthFix" class="icon" />
        <text class="title">Care</text>
        <view class="right-spacer"></view>
      </view>

      <!-- 内容区域 -->
      <view class="content">
        <view class="inner">
          <!-- 7日趋势折线图 -->
          <view class="trend-card">
            <text class="trend-title">7-Times Trend</text>
            <view class="chart-wrap">
              <svg viewBox="0 0 100 100" preserveAspectRatio="none" class="svg">
                <defs>
                  <linearGradient id="lineGradient" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stop-color="#A9BE9F" />
                    <stop offset="100%" stop-color="#8BA989" />
                  </linearGradient>
                  <linearGradient id="fillGradient" x1="0%" y1="0%" x2="0%" y2="100%">
                    <stop offset="0%" stop-color="rgba(170,190,160,0.25)" />
                    <stop offset="100%" stop-color="rgba(183,200,162,0)" />
                  </linearGradient>
                </defs>

                <!-- 区域填充 -->
                <polyline :points="areaPoints" fill="url(#fillGradient)" stroke="none" />

                <!-- 折线（分数越高→越不健康→越低） -->
                <polyline
                    :points="linePoints"
                    fill="none"
                    stroke="url(#lineGradient)"
                    stroke-width="1.8"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                />

                <!-- 数据点 -->
                <g v-for="p in computedPoints" :key="p.day + '-' + p.x">
                  <circle :cx="p.x" :cy="p.y" r="1.8" fill="#fff" stroke="#9CAF88" stroke-width="0.4" />
                </g>

                <!-- 网格线 -->
                <line x1="0" y1="80" x2="100" y2="80" stroke="#E8EAE6" stroke-width="0.4" stroke-dasharray="2" />
                <line x1="0" y1="40" x2="100" y2="40" stroke="#E8EAE6" stroke-width="0.4" stroke-dasharray="2" />
              </svg>

              <!-- 日期标签 / 空态 -->
              <view class="date-labels" v-if="weeklyData.length">
                <text v-for="(d, idx) in weeklyData" :key="idx" class="date-text">{{ d.day }}</text>
              </view>
              <view v-else class="empty-tip">No data yet</view>
            </view>
          </view>

          <!-- 功能按钮 -->
          <view class="feature-btn" @click="goQuestionare">
            <view class="icon-circle soft-green">
              <image :src="icons.doc" mode="aspectFit" class="card-icon-img" />
            </view>
            <view class="flex-1">
              <text class="card-title">Health Examination</text>
              <text class="card-sub">Take your routine health check</text>
            </view>
          </view>

          <view class="feature-btn" @click="goReport">
            <view class="icon-circle soft-gold">
              <image :src="icons.check" mode="aspectFit" class="card-icon-img" />
            </view>
            <view class="flex-1">
              <text class="card-title">Report</text>
              <text class="card-sub">View your weekly report summary</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
import { ref, getCurrentInstance, computed, onMounted } from 'vue'
import menuIcon from '@/static/gg_menu-left-alt.svg'
import docIcon from '@/static/ic_baseline-feed.svg'
import checkIcon from '@/static/ic_baseline-fact-check.svg'
import service from '@/utils/request'

const icons = { menu: menuIcon, doc: docIcon, check: checkIcon }

/* ============== 数据状态 ============== */
const weeklyData = ref([]) // [{ day:'MM/DD', score:number }]
const { proxy } = getCurrentInstance()

onMounted(fetchHealthData)

/* ============== 直连通用表接口，绕过拦截器 ============== */
// async function fetchHealthData () {
//   try {
//     const userRes = await proxy.$cf.login.getLoginUser()
//     const userId = userRes?.data?.user_info_id
//     console.log('login uid =', userId)
//     if (!userId) { weeklyData.value = []; return }
//
//     // ✅ 完全沿用 Todo 页已验证可行的表查询封装
//     const res = await proxy.$cf.table.list({
//       table_name: 'health_questionnaire',
//       param: { user_info_user_info_id_1: userId },   // 和 Todo 页保持一致的筛选键
//       orderby: 'creation_time',                      // 排序字段
//       sort: 'desc',                                  // 倒序 -> 最新在前
//       limit: 7                                       // 只要最近7条
//     })
//
//     const rows = res?.success ? (res.data || []) : []
//
//     // 为画折线改成“从旧到新”的顺序
//     const ordered = [...rows].reverse()
//
//     weeklyData.value = ordered.map(r => ({
//       day: formatDate(r.creation_time ?? r.creationTime),
//       score: Number(r.total_score ?? r.totalScore ?? 0),
//     }))
//   } catch (e) {
//     console.warn('fetchHealthData error:', e?.response?.status, e?.response?.data || e)
//     weeklyData.value = []
//   }
// }
async function fetchHealthData () {
  try {
    const userRes = await proxy.$cf.login.getLoginUser();
    const userId = userRes?.data?.user_info_id;
    if (!userId) { weeklyData.value = []; return; }

    const r = await service.get('/care/weekly', { params: { userId, limit: 7 } });
    const rows = r?.data ?? r?.records ?? r ?? []; // 兼容你的 R 结构

    weeklyData.value = rows.map(p => ({
      day: p.day,
      score: Number(p.score || 0)
    }));
  } catch (e) {
    console.warn('fetchHealthData error:', e?.response?.status, e?.response?.data || e);
    weeklyData.value = [];
  }
}








/* 统一的 uni.request Promise 封装（不抛错，返回原始 data） */
function uniRequest(opts) {
  return new Promise((resolve, reject) => {
    uni.request({
      ...opts,
      success: ({ data }) => resolve(data),
      fail: (err) => reject(err)
    })
  })
}

/* —— 工具：智能解包数组 —— */
function unwrapArray(res) {
  if (!res) return []
  if (Array.isArray(res)) return res
  if (Array.isArray(res.data)) return res.data
  if (res.data && Array.isArray(res.data.records)) return res.data.records
  if (Array.isArray(res.rows)) return res.rows
  if (Array.isArray(res.result)) return res.result
  if (res.data && Array.isArray(res.data.list)) return res.data.list
  return []
}

/* —— 工具：从对象里优先取存在的字段 —— */
function pick(obj, ...keys) {
  for (const k of keys) if (obj && obj[k] != null) return obj[k]
  return undefined
}

/* —— 时间解析：兼容毫秒/秒时间戳 & ISO —— */
function toTime(t) {
  if (t == null) return 0
  if (typeof t === 'number') return t > 1e12 ? t : t * 1000
  const d = new Date(t)
  const ms = d.getTime()
  return Number.isNaN(ms) ? 0 : ms
}
function formatDate(ts) {
  const ms = toTime(ts)
  if (!ms) return ''
  const d = new Date(ms)
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${m}/${day}`
}

/* ============== 折线图（高分越低） ============== */
const minScore = 0
const maxScore = 34

const computedPoints = computed(() => {
  const data = weeklyData.value
  if (!data.length) return []
  const step = data.length > 1 ? 100 / (data.length - 1) : 0
  return data.map((entry, idx) => {
    const x = idx * step
    // 分数越高越不健康 → 图上越低（0 在上，34 在下）
    const y = ((entry.score - minScore) / (maxScore - minScore)) * 95 + 2
    return { ...entry, x, y }
  })
})

const linePoints = computed(() =>
    computedPoints.value.map(p => `${p.x},${p.y}`).join(' ')
)

const areaPoints = computed(() => {
  const pts = computedPoints.value
  if (!pts.length) return ''
  const start = `0,100`
  const lines = pts.map(p => `${p.x},${p.y}`).join(' ')
  const end = `100,100`
  return `${start} ${lines} ${end}`
})

/* ============== 跳转 ============== */
const goQuestionare = () => {
  const url = '/pagesA/care/questionare/index'
  proxy?.$cf?.navigate?.to ? proxy.$cf.navigate.to({ url }) : uni.navigateTo({ url })
}
const goReport = () => {
  const url = '/pagesA/care/report/index'
  proxy?.$cf?.navigate?.to ? proxy.$cf.navigate.to({ url }) : uni.navigateTo({ url })
}
</script>

<style scoped>
.page { min-height: 100vh; background: #f8f9f8; display: flex; flex-direction: column; }
.content { padding: 8rpx 0 60rpx; }
.inner { width: 690rpx; margin: 0 auto; }

/* 顶部栏 */
.topbar { display: grid; grid-template-columns: 112rpx 1fr 112rpx; align-items: center; height: 112rpx; background: #fff; padding: 0 24rpx; }
.icon { width: 64rpx; height: 48rpx; }
.title { text-align: center; font-size: 50rpx; font-weight: 700; color: #1d1d1d; }
.right-spacer { width: 112rpx; }

/* 折线图 */
.trend-card { background: rgba(255, 255, 255, 0.95); backdrop-filter: blur(6px); border-radius: 28rpx; padding: 28rpx; box-shadow: 0 6px 14px rgba(0,0,0,.08); margin-bottom: 32rpx; margin-top: 12rpx; }
.trend-title { font-size: 34rpx; font-weight: 700; margin-bottom: 16rpx; color: #1d1d1d; }
.chart-wrap { position: relative; width: 100%; height: 520rpx; }
.svg { width: 100%; height: 100%; display: block; }

/* 日期标签 & 空数据 */
.date-labels { display: flex; justify-content: space-between; position: absolute; bottom: -30rpx; left: 0; right: 0; }
.date-text { font-size: 24rpx; color: #666; }
.empty-tip{ position:absolute; left:0; right:0; top:50%; transform: translateY(-50%); text-align:center; color:#9aa1a8; font-size:26rpx; }

/* 按钮 */
.feature-btn { display: flex; align-items: center; gap: 24rpx; width: 100%; background: white; border-radius: 28rpx; padding: 28rpx 32rpx; margin-top: 24rpx; box-shadow: 0 4px 12px rgba(0,0,0,.06); transition: all .25s ease; }
.feature-btn:hover { transform: translateY(-1.5px); box-shadow: 0 6px 18px rgba(0,0,0,.1); }
.icon-circle { width: 80rpx; height: 80rpx; border-radius: 20rpx; display: flex; align-items: center; justify-content: center; }
.soft-green { background: linear-gradient(145deg, #EAEDE5, #DEE2D8); }
.soft-gold { background: linear-gradient(145deg, #F7EBDD, #EADDCB); }
.card-title { font-size: 40rpx; font-weight: 700; color: #1d1d1d; display: block; }
.card-sub { font-size: 28rpx; color: #666; display: block; margin-top: 4rpx; }
.card-icon-img { width: 48rpx; height: 48rpx; }
</style>
