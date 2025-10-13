<!-- src/pagesA/care/index.vue -->
<template>
  <base-layout>
    <view class="page">
      <!-- Top bar -->
      <view class="topbar">
        <image :src="icons.menu" mode="widthFix" class="icon" />
        <text class="title">Care</text>
        <view class="right-spacer"></view>
      </view>

      <!-- Content -->
      <view class="content">
        <view class="inner">
          <!-- Heading -->
          <view class="h1">
            <text>Health Score</text>
            <text>Status</text>
          </view>

          <!-- Line chart placeholder (can be replaced by ECharts/Canvas later) -->
          <view class="chart-card">
            <view class="chart-grid">
              <view class="grid-line"></view>
              <view class="grid-line"></view>
              <view class="grid-line"></view>
            </view>
            <view class="area-mask"></view>
            <view class="chart-xlabels">
              <text v-for="d in days" :key="d">{{ d }}</text>
            </view>
          </view>

          <!-- Keep only these two cards -->
          <view class="card card-safe" role="button" @click="goQuestionare">
            <view class="card-icon">
              <image :src="icons.doc" mode="aspectFit" class="card-icon-img" />
            </view>
            <text class="card-title">Health examination</text>
          </view>

          <view class="card card-alert" role="button" @click="goReport">
            <view class="card-icon">
              <image :src="icons.check" mode="aspectFit" class="card-icon-img" />
            </view>
            <text class="card-title">Report</text>
          </view>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
import {getCurrentInstance, ref} from 'vue'
import menuIcon  from '@/static/gg_menu-left-alt.svg'
import docIcon   from '@/static/ic_baseline-feed.svg'          // document/feed icon
import checkIcon from '@/static/ic_baseline-fact-check.svg'    // report/checklist icon

const icons = { menu: menuIcon, doc: docIcon, check: checkIcon }
const days = ref(['Mon','Tues','Wed','Thu','Fri','Sat'])

const goQuestionare = () => {
  const proxy = getCurrentInstance()?.proxy
  const url = '/pagesA/care/questionare/index'
  if (proxy?.$cf?.navigate?.to) {
    proxy.$cf.navigate.to({ url, type: 'page', mode: 'navigate' })
  } else {
    uni.navigateTo({ url })
  }
}

const goReport = () => {
  const proxy = getCurrentInstance()?.proxy
  const url = '/pagesA/care/report/index'
  if (proxy?.$cf?.navigate?.to) {
    proxy.$cf.navigate.to({ url, type: 'page', mode: 'navigate' })
  } else {
    uni.navigateTo({ url })
  }
}


</script>

<style scoped>
:root{
  --bg:#f7f7f7;
  --text:#222;
  --muted:#666;
  --white:#fff;
  --shadow:0 6px 14px rgba(0,0,0,.08);
  /* Card palette (consistent with legend) */
  --card-alert:#eeb79b;
  --card-safe:#90b79e;
  --card-neutral:#bdb8b5;
}

/* Page container */
.page{
  min-height:100vh;
  background:var(--bg);
  display:flex;
  flex-direction:column;
}
.content{
  padding:36rpx 0 60rpx;
}
.inner{
  width:690rpx;
  margin:0 auto;
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
.icon{
  width:64rpx;
  height:48rpx;
}
.title{
  text-align:center;
  font-size:50rpx;
  font-weight:700;
  color:var(--text);
}
.right-spacer{
  width:112rpx;
}

/* Heading */
.h1{
  display:flex;
  flex-direction:column;
  gap:8rpx;
  margin:36rpx 0 20rpx;
}
.h1 text{
  font-size:60rpx;
  font-weight:800;
  color:#111;
}

/* Chart card */
.chart-card{
  position:relative;
  height:500rpx;
  background:#eaf3f1;
  border-radius:20rpx;
  overflow:hidden;
  box-shadow:var(--shadow);
}
.chart-grid{
  position:absolute;
  inset:0;
  display:flex;
  flex-direction:column;
  justify-content:space-evenly;
  padding:48rpx 36rpx 72rpx;
  pointer-events:none;
}
.grid-line{
  width:100%;
  height:2rpx;
  background:rgba(0,0,0,.06);
}
.area-mask{
  position:absolute;
  left:36rpx;
  right:36rpx;
  top:48rpx;
  bottom:84rpx;
  background:linear-gradient(to bottom, rgba(0,120,120,.18), rgba(0,120,120,.10));
  clip-path: polygon(0% 62%,16% 22%,33% 36%,50% 82%,75% 42%,100% 40%,100% 100%,0% 100%);
}
.chart-xlabels{
  position:absolute;
  left:36rpx;
  right:36rpx;
  bottom:22rpx;
  display:flex;
  justify-content:space-between;
  color:#676767;
  font-size:28rpx;
}

/* Two colored cards */
.card{
  display:grid;
  grid-template-columns:112rpx 1fr;
  align-items:center;
  gap:24rpx;
  padding:32rpx;
  border-radius:28rpx;
  overflow:hidden;
  box-shadow:0 6px 14px rgba(0,0,0,.08);
  margin-top:32rpx;
  color:#111;
}
.card-title{
  font-size:48rpx;
  font-weight:800;
}
.card-safe{
  background:var(--card-safe,  #90b79e);
}
.card-alert{
  background:var(--card-alert, #eeb79b);
}
.card-neutral{
  background:var(--card-neutral, #bdb8b5);
} /* spare */

/* Card icon white square */
.card-icon{
  width: 80rpx;
  height: 80rpx;
  border-radius: 24rpx;
  background: #fff;                  /* Pure white */
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;                  /* Clip inner image within the square */
  box-shadow: 0 2px 6px rgba(0,0,0,0.06);
}
.card-icon-img{
  width:48rpx;
  height:48rpx;
  display:block;
}
</style>
