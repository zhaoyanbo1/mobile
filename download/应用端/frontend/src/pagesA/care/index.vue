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
          <!-- 健康得分 -->
          <view class="score-card">
            <text class="score-title">This Week’s Health Score</text>
            <view class="score-row">
              <text class="score-big">{{ currentScore }}</text>
              <text class="score-suffix">/ 100</text>
            </view>
            <text class="score-sub">Up by 8 points from last week</text>
          </view>

          <!-- 7日趋势折线图 -->
          <view class="trend-card">
            <text class="trend-title">7-Day Trend</text>
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

                <!-- 折线 -->
                <polyline
                    :points="linePoints"
                    fill="none"
                    stroke="url(#lineGradient)"
                    stroke-width="1.8"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                />

                <!-- 数据点 -->
                <g v-for="p in pointPositions" :key="p.day">
                  <circle :cx="p.x" :cy="p.y" r="1.8" fill="#fff" stroke="#9CAF88" stroke-width="0.4" />
                </g>

                <!-- 网格线 -->
                <line x1="0" y1="80" x2="100" y2="80" stroke="#E8EAE6" stroke-width="0.4" stroke-dasharray="2" />
                <line x1="0" y1="40" x2="100" y2="40" stroke="#E8EAE6" stroke-width="0.4" stroke-dasharray="2" />
              </svg>
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
import { getCurrentInstance, computed } from 'vue'
import menuIcon from '@/static/gg_menu-left-alt.svg'
import docIcon from '@/static/ic_baseline-feed.svg'
import checkIcon from '@/static/ic_baseline-fact-check.svg'

const icons = { menu: menuIcon, doc: docIcon, check: checkIcon }

/* 每日健康分数 */
const weeklyData = [
  { day: 'Mon', score: 72 },
  { day: 'Tue', score: 78 },
  { day: 'Wed', score: 75 },
  { day: 'Thu', score: 82 },
  { day: 'Fri', score: 85 },
  { day: 'Sat', score: 88 },
  { day: 'Sun', score: 90 },
]

const currentScore = weeklyData[weeklyData.length - 1].score

/* 自动纵轴拉伸，防止折线扁平 */
const actualMin = Math.min(...weeklyData.map(d => d.score))
const actualMax = Math.max(...weeklyData.map(d => d.score))
const range = actualMax - actualMin || 1
const minScore = actualMin - range * 0.3
const maxScore = actualMax + range * 0.3

/* 点坐标计算 */
const pointPositions = computed(() => {
  const step = weeklyData.length > 1 ? 100 / (weeklyData.length - 1) : 0
  return weeklyData.map((entry, idx) => {
    const x = idx * step
    const y = ((maxScore - entry.score) / (maxScore - minScore)) * 95 + 2
    return { ...entry, x, y }
  })
})

/* 折线 polyline */
const linePoints = computed(() => pointPositions.value.map(p => `${p.x},${p.y}`).join(' '))

/* 区域 polyline */
const areaPoints = computed(() => {
  const pts = pointPositions.value
  if (!pts.length) return ''
  const start = `0,100`
  const lines = pts.map(p => `${p.x},${p.y}`).join(' ')
  const end = `100,100`
  return `${start} ${lines} ${end}`
})

/* 跳转函数 */
const goQuestionare = () => {
  const proxy = getCurrentInstance()?.proxy
  const url = '/pagesA/care/questionare/index'
  proxy?.$cf?.navigate?.to ? proxy.$cf.navigate.to({ url }) : uni.navigateTo({ url })
}
const goReport = () => {
  const proxy = getCurrentInstance()?.proxy
  const url = '/pagesA/care/report/index'
  proxy?.$cf?.navigate?.to ? proxy.$cf.navigate.to({ url }) : uni.navigateTo({ url })
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f8f9f8;
  display: flex;
  flex-direction: column;
}
.content {
  padding: 8rpx 0 60rpx; /* 去掉多余空白 */
}
.inner {
  width: 690rpx;
  margin: 0 auto;
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
.icon {
  width: 64rpx;
  height: 48rpx;
}
.title {
  text-align: center;
  font-size: 50rpx;
  font-weight: 700;
  color: #1d1d1d;
}
.right-spacer {
  width: 112rpx;
}

/* 分数卡片 */
.score-card {
  background: linear-gradient(135deg, #A3B18A, #7E8C77);
  color: #fff;
  border-radius: 28rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.08);
}
.score-title {
  font-size: 40rpx;
  font-weight: 800;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 10rpx;
}
.score-row {
  display: flex;
  align-items: baseline;
  gap: 12rpx;
}
.score-big {
  font-size: 96rpx;
  font-weight: 800;
  line-height: 1;
}
.score-suffix {
  font-size: 36rpx;
  opacity: 0.9;
}
.score-sub {
  margin-top: 8rpx;
  opacity: 0.75;
}

/* 折线图 */
.trend-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(6px);
  border-radius: 28rpx;
  padding: 28rpx;
  box-shadow: 0 6px 14px rgba(0, 0, 0, 0.08);
  margin-bottom: 32rpx;
}
.trend-title {
  font-size: 34rpx;
  font-weight: 700;
  margin-bottom: 16rpx;
  color: #1d1d1d;
}
.chart-wrap {
  width: 100%;
  height: 520rpx;
}
.svg {
  width: 100%;
  height: 100%;
  display: block;
}

/* 按钮 */
.feature-btn {
  display: flex;
  align-items: center;
  gap: 24rpx;
  width: 100%;
  background: white;
  border-radius: 28rpx;
  padding: 28rpx 32rpx;
  margin-top: 24rpx;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.25s ease;
}
.feature-btn:hover {
  transform: translateY(-1.5px);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
}
.icon-circle {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
.soft-green {
  background: linear-gradient(145deg, #EAEDE5, #DEE2D8);
}
.soft-gold {
  background: linear-gradient(145deg, #F7EBDD, #EADDCB);
}
.card-title {
  font-size: 40rpx;
  font-weight: 700;
  color: #1d1d1d;
  display: block;
}
.card-sub {
  font-size: 28rpx;
  color: #666;
  display: block;
  margin-top: 4rpx;
}
.card-icon-img {
  width: 48rpx;
  height: 48rpx;
}
</style>
