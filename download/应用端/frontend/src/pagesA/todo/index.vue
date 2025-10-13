<!-- src/pages/calendar/index.vue —— 今日任务 + Todo 风格汇总 -->
<template>
  <base-layout>
    <view class="page p-4">
      <!-- 顶部栏 -->
      <view class="topbar">
        <image :src="icons.menu" mode="widthFix" class="icon" />
        <text class="title">Todo</text>
        <view class="right-spacer"></view>
      </view>

      <!-- ===== Todo 风格的今日汇总（进度条 + 宝箱） ===== -->
      <view class="todo-extra">
        <view class="todo-progress">
          <view class="todo-progress-row">
            <view class="todo-bar">
              <view class="todo-bar-fill" :style="{ width: progressPercent + '%' }"></view>
            </view>
            <view
                class="todo-chest"
                :class="{ ready: unlockReady }"
                role="button"
                @click="onChestClick"
            >
              <image :src="icons.chest" mode="aspectFit" class="todo-chest-img" />
            </view>
          </view>
          <view class="todo-bar-labels">
            <text class="done">{{ doneLabel }}</text>
            <text class="total">{{ totalLabel }}</text>
          </view>
        </view>

        <!-- 日期标题（直接使用今天） -->
        <view class="todo-day-title">
          <text>{{ dayTitle }}</text>
        </view>

        <!-- 今日任务（从 reminders 映射） -->
        <view
            class="todo-task-row"
            v-for="t in dayTasks"
            :key="t.id"
            @click="toggleTaskById(t.id)"
        >
          <text class="todo-time">{{ t.time }}</text>
          <view class="todo-task-card" :class="{ done: t.done }">
            <view class="todo-dot" :class="{ done: t.done }"></view>
            <text class="todo-task-title">{{ t.title }}</text>
          </view>
        </view>

        <!-- Add Task（仍保留你原来的新增任务按钮） -->
        <view class="todo-add-btn" role="button" @click="goAddTask">
          <uni-icons type="plusempty" size="22" color="#fff" />
          <text class="todo-add-btn-text">Add Task</text>
        </view>

        <!-- Medal 胶囊（保持原样） -->
        <view class="pill-btn" role="button">
          <view class="btn-icon">
            <image :src="icons.medal" mode="aspectFit" class="btn-icon-img" />
          </view>
          <view class="btn-content" role="button" @click="goShowModel">
            <text class="btn-text">Wall of Medals</text>
          </view>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
import { ref, computed, getCurrentInstance } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import menuIcon from '@/static/gg_menu-left-alt.svg'
import chestIcon from '@/static/box.svg'
import medalIcon from '@/static/safe.svg'

const { proxy } = getCurrentInstance()

/* ===== 常量：固定目标 5 ===== */
const TARGET_COUNT = 5

/* ===== 数据 ===== */
const reminders = ref([])     // 今日任务
const loading   = ref(false)

/* 图标 */
const icons = { menu: menuIcon, chest: chestIcon, medal: medalIcon }

/* 工具：今天起止时间 */
function getTodayRange () {
  const now = new Date()
  const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0, 0)
  const end   = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59, 999)
  return { start, end }
}
const toDate = (v) => new Date(String(v).replace(/-/g, '/'))

/* 仅取“今天”的任务 */
async function fetchReminders () {
  try {
    loading.value = true

    // 登录用户
    const userRes = await proxy.$cf.login.getLoginUser()
    if (!userRes?.success) { reminders.value = []; return }

    // 取该用户的任务（升序），前端筛选“今天”
    const res = await proxy.$cf.table.list({
      table_name: 'reminder_item',
      param: { user_info_user_info_id_1: userRes.data.user_info_id },
      orderby: 'reminder_time',
      sort: 'asc',
      limit: 500
    })

    const { start, end } = getTodayRange()
    const rows = res?.success ? res.data : []
    reminders.value = rows.filter(r => {
      const t = toDate(r.reminder_time)
      return t >= start && t <= end
    })
  } finally {
    loading.value = false
  }
}

/* 映射成显示项 */
const dayTasks = computed(() =>
    reminders.value.map(r => ({
      id: r.reminder_item_id,
      time: formatTime(r.reminder_time),
      title: r.title,
      done: !!r.is_completed
    }))
)

/* 完成数量（用于达标判断） */
const doneCount  = computed(() => dayTasks.value.filter(t => t.done).length)

/* 进度：按“已完成 / 固定目标(5)”计算 */
const progressPercent = computed(() => {
  const pct = TARGET_COUNT ? Math.round((doneCount.value / TARGET_COUNT) * 100) : 0
  return Math.max(0, Math.min(100, pct))
})
const unlockReady = computed(() => doneCount.value >= TARGET_COUNT)

/* 标签：左侧显示 X/5；右侧显示 Target: 5 Tasks */
const doneLabel  = computed(() => `${doneCount.value} / ${TARGET_COUNT}`)
const totalLabel = computed(() => `Target: ${TARGET_COUNT} Tasks`)

/* 标题：如 "DEC 26" */
const dayTitle = computed(() => {
  const d = new Date()
  const m = d.toLocaleString('en-US', { month: 'short' }).toUpperCase()
  const dd = String(d.getDate()).padStart(2, '0')
  return `${m} ${dd}`
})

/* 行为 */
async function handleRefresh () {
  await fetchReminders()
  uni.stopPullDownRefresh?.()
}
function toggleTaskById (id) {
  const item = reminders.value.find(r => r.reminder_item_id === id)
  if (item) toggleComplete(item)
}
async function toggleComplete (item) {
  const next = !item.is_completed
  item.is_completed = next
  await proxy.$cf.table.update({
    table_name: 'reminder_item',
    param: { reminder_item_id: item.reminder_item_id, is_completed: next }
  })
}

/* 宝箱点击：未达标 -> show_model；达标(≥5) -> win_model */
function onChestClick () {
  if (unlockReady.value) {
    goWinModel()
  } else {
    goShowModel()
  }
}

/* 跳转页（按你提供的路径） */
function goShowModel () {
  const url = '/pagesA/todo/show_modal/index'
  if (proxy?.$cf?.navigate?.to) proxy.$cf.navigate.to({ url, type: 'page', mode: 'navigate' })
  else uni.navigateTo({ url })
}
function goWinModel () {
  const url = '/pagesA/todo/win_modal/index'
  if (proxy?.$cf?.navigate?.to) proxy.$cf.navigate.to({ url, type: 'page', mode: 'navigate' })
  else uni.navigateTo({ url })
}

/* 原有“新增任务”按钮跳转，保持不变 */
function goAddTask () {
  const url = '/pagesA/todo/add_task/index'
  if (proxy?.$cf?.navigate?.to) proxy.$cf.navigate.to({ url, type: 'page', mode: 'navigate' })
  else uni.navigateTo({ url })
}

/* 格式化 */
function formatDateTime (dt) {
  if (!dt) return ''
  const d = toDate(dt)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hh = String(d.getHours()).padStart(2, '0')
  const mm = String(d.getMinutes()).padStart(2, '0')
  return `${y}-${m}-${day} ${hh}:${mm}`
}
function formatTime (dt) {
  if (!dt) return '--:--'
  const d = toDate(dt)
  const hh = String(d.getHours()).padStart(2, '0')
  const mm = String(d.getMinutes()).padStart(2, '0')
  return `${hh}:${mm}`
}

/* 初始化：直接加载今日任务 */
onLoad(() => { fetchReminders() })
</script>

<style scoped>
/* 顶部栏 */
.topbar{ display:grid; grid-template-columns:112rpx 1fr 112rpx; align-items:center; height:112rpx; background:var(--white); padding:0 24rpx; }
.icon{ width:64rpx; height:48rpx; }
.title{ text-align:center; font-size:50rpx; font-weight:700; color:var(--text); }
.right-spacer{ width:112rpx; }

/* 与问卷页统一的色板 */
.page{
  --sage:#93b2a1;
  --ink:#111827;
  --muted:#6b7280;
  --bg:#f8f8f8;
  --surface:#ffffff;
  --shadow:0 10px 22px rgba(0,0,0,.10);
  --hairline:rgba(17,24,39,.06);
  --green:#90b79e;
  --green-soft:#d7efe2;
  --gray:#bdb8b5;
  --dot-green:#3fb36f;
  --dot-gray:#5a5a5a;
  --bar-bg:#ddd7cd;
  --radius-card:28rpx;
  background:var(--bg);
}

/* ===== Todo extra section ===== */
.todo-extra{ margin-top:16px; }

/* Progress */
.todo-progress{ margin:20rpx 0; }
.todo-progress-row{ display:flex; align-items:center; gap:20rpx; }
.todo-bar{
  flex:0 0 80%; height:40rpx; background:var(--bar-bg);
  border-radius:9999rpx; overflow:hidden; position:relative;
  box-shadow:inset 0 0 0 1px rgba(0,0,0,.04);
}
.todo-bar-fill{
  position:absolute; inset:0 auto 0 0; width:0;
  background:var(--green); border-radius:9999rpx; transition:width .25s ease;
}
.todo-chest{ flex:1; display:flex; justify-content:flex-end; }
.todo-chest-img{ width:112rpx; height:112rpx; transition:transform .2s ease, filter .2s ease; }
.todo-chest.ready .todo-chest-img{ transform:scale(1.05); filter:drop-shadow(0 6px 10px rgba(0,0,0,.12)); }

.todo-bar-labels{
  margin-top:10rpx; display:flex; justify-content:space-between;
  color:#3a3a3a; font-size:30rpx;
}

/* Day title */
.todo-day-title{ margin:24rpx 0 8rpx; }
.todo-day-title text{ font-size:44rpx; font-weight:900; letter-spacing:2rpx; color:var(--ink); }

/* Task rows */
.todo-task-row{
  display:grid; grid-template-columns:120rpx 1fr;
  align-items:center; gap:20rpx; margin:16rpx 0;
}
.todo-time{ font-size:34rpx; font-weight:800; color:#202020; }

/* Task card */
.todo-task-card{
  width:100%; min-height:112rpx; padding:24rpx 28rpx;
  display:flex; align-items:center; gap:20rpx;
  background:var(--gray); color:#111;
  border:1px solid var(--hairline);
  border-radius:var(--radius-card); box-shadow:var(--shadow);
  transition:background .15s ease;
}
.todo-task-card.done{ background:var(--green-soft); }

.todo-dot{ width:20rpx; height:20rpx; border-radius:50%; background:var(--dot-gray); }
.todo-dot.done{ background:var(--dot-green); }
.todo-task-title{ font-size:36rpx; font-weight:800; }

/* Add button */
.todo-add-btn{
  margin-top:20rpx; width:100%; min-height:108rpx;
  border-radius:var(--radius-card);
  background:#839f90; color:#fff;
  display:flex; align-items:center; justify-content:center; gap:18rpx;
  box-shadow:var(--shadow);
}
.todo-add-btn-text{ font-size:40rpx; font-weight:800; }

/* Medal pill */
.pill-btn{
  margin-top:36rpx; width:100%; min-height:128rpx;
  border-radius:var(--radius-card);
  display:grid; grid-template-columns:112rpx 1fr; align-items:center;
  gap:24rpx; padding:24rpx 28rpx;
  box-shadow:var(--shadow); color:#111; background:var(--green);
}
.btn-icon{
  width:80rpx; height:80rpx; border-radius:24rpx; background:#fff;
  display:flex; align-items:center; justify-content:center; overflow:hidden;
  box-shadow:0 2px 6px rgba(0,0,0,0.06);
}
.btn-icon-img{ width:48rpx; height:48rpx; display:block; }
.btn-content{ display:flex; flex-direction:column; justify-content:center; min-height:80rpx; gap:6rpx; }
.btn-text{ font-size:40rpx; font-weight:800; line-height:1.2; }
</style>
