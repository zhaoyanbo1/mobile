<template>
  <base-layout>
    <!-- Topbar -->
    <view class="topbar">
      <image :src="icons.menu" mode="widthFix" class="icon" />
      <text class="title">Todo</text>
      <view class="right-spacer"></view>
    </view>

    <div class="flex flex-col px-6 pt-16 pb-28 min-h-screen bg-[#F8F9F8] relative">
      <!-- ===== 顶部标题 + 宝箱按钮 ===== -->
      <div class="flex items-center justify-between mb-8">
        <h2 class="text-3xl font-extrabold text-[#1D1D1D] tracking-tight">
          Today's Tasks
        </h2>

        <button
            class="w-14 h-14 rounded-2xl bg-gradient-to-br from-[#DDB892] to-[#C9A87C]
                 flex items-center justify-center shadow-[0_4px_14px_rgba(0,0,0,0.15)]
                 hover:scale-105 active:scale-95 transition-all duration-200"
            :class="{ 'ring-4 ring-[#A3B18A]/30': unlockReady }"
            @click="onChestClick"
        >
          <img src="@/static/box.svg" class="w-8 h-8" alt="chest" />
        </button>
      </div>

      <!-- ===== 今日进度条 ===== -->
      <div class="bg-white/90 backdrop-blur-sm rounded-3xl p-6 mb-8 shadow-lg">
        <div class="flex items-center justify-between mb-3">
          <h3 class="text-lg font-semibold text-[#1D1D1D]">Today's Progress</h3>
          <span class="text-[#A3B18A] font-semibold text-lg">{{ doneLabel }}</span>
        </div>
        <div class="w-full h-3 bg-gray-200 rounded-full overflow-hidden">
          <div
              class="h-full bg-gradient-to-r from-[#A3B18A] to-[#7E8C77]
                   transition-all duration-500 rounded-full"
              :style="{ width: progressPercent + '%' }"
          ></div>
        </div>
      </div>

      <!-- ===== 系统任务 ===== -->
      <section class="mb-8">
        <h4 class="mb-3 text-[#7E8C77] font-semibold text-base">
          System Tasks (1 pt each)
        </h4>
        <div v-if="loading" class="text-center text-gray-400 py-4">Loading...</div>
        <div v-else-if="highPriorityTasks.length" class="space-y-4">
          <div
              v-for="task in highPriorityTasks"
              :key="task.id"
              class="rounded-2xl p-5 bg-white shadow-sm flex items-center justify-between
                   transition hover:-translate-y-0.5 duration-200 animate-fade-card"
              :class="task.completed ? 'opacity-60 line-through' : ''"
              @click="toggleDbTask(task.id)"
          >
            <div>
              <h4 class="text-lg font-medium">{{ task.title }}</h4>
              <p v-if="task.description" class="text-sm text-gray-500">{{ task.description }}</p>
              <p class="text-xs text-gray-400 mt-1">Due {{ task.subtitle }}</p>
            </div>
            <CheckCircle2
                v-if="task.completed"
                class="w-6 h-6 text-[#A3B18A]"
                :stroke-width="2"
            />
            <Circle v-else class="w-6 h-6 text-gray-300" :stroke-width="2" />
          </div>
        </div>
        <div v-else class="relative">
          <div class="h-36 rounded-3xl bg-white/40 backdrop-blur-sm border border-dashed border-[#A3B18A]/40"></div>
          <div class="absolute inset-0 flex items-center justify-center">
            <button
                class="px-5 h-14 rounded-full bg-white text-[#7E8C77] font-semibold shadow-lg flex items-center gap-2 border border-[#A3B18A]/40 transition-all hover:-translate-y-0.5 active:scale-95 disabled:opacity-70 disabled:cursor-not-allowed"
                @click="generateAiTodos"
                :disabled="generatingAi"
            >
              <Sparkles class="w-5 h-5" :stroke-width="2" />
              <span>{{ generatingAi ? 'Planning…' : 'AI Boost' }}</span>
            </button>
          </div>
        </div>
      </section>

      <!-- ===== Bonus 任务 ===== -->
      <section class="mb-8">
        <h4 class="mb-3 text-[#7E8C77] font-semibold text-base">
          Bonus Tasks (2 pts each)
        </h4>
        <div v-if="loading" class="text-center text-gray-400 py-4">Loading...</div>
        <div v-else-if="bonusTasks.length" class="space-y-4">
          <div
              v-for="task in bonusTasks"
              :key="task.id"
              class="rounded-2xl p-5 bg-white shadow-sm flex items-center justify-between
                   transition hover:-translate-y-0.5 duration-200 animate-fade-card"
              :class="task.completed ? 'opacity-60 line-through' : ''"
              @click="toggleDbTask(task.id)"
          >
            <div>
              <h4 class="text-lg font-medium">{{ task.title }}</h4>
              <p v-if="task.description" class="text-sm text-gray-500">{{ task.description }}</p>
              <p class="text-xs text-gray-400 mt-1">Due {{ task.subtitle }}</p>
            </div>
            <CheckCircle2
                v-if="task.completed"
                class="w-6 h-6 text-[#C9A87C]"
                :stroke-width="2"
            />
            <Circle v-else class="w-6 h-6 text-gray-300" :stroke-width="2" />
          </div>
        </div>
        <div v-else class="relative">
          <div class="h-36 rounded-3xl bg-white/40 backdrop-blur-sm border border-dashed border-[#C9A87C]/40"></div>
          <div class="absolute inset-0 flex items-center justify-center">
            <button
                class="px-5 h-14 rounded-full bg-white text-[#C9A87C] font-semibold shadow-lg flex items-center gap-2 border border-[#C9A87C]/40 transition-all hover:-translate-y-0.5 active:scale-95 disabled:opacity-70 disabled:cursor-not-allowed"
                @click="generateAiBonusTodos"
                :disabled="generatingBonus"
            >
              <Sparkles class="w-5 h-5" :stroke-width="2" />
              <span>{{ generatingBonus ? 'Planning…' : 'AI Bonus' }}</span>
            </button>
          </div>
        </div>
      </section>

      <!-- ===== 自定义任务 ===== -->
      <section>
        <h4 class="mb-3 text-[#7E8C77] font-semibold text-base">
          Custom Tasks (0.5 pt each)
        </h4>
        <div v-if="loading" class="text-center text-gray-400 py-4">Loading...</div>
        <div v-else-if="customTasks.length === 0" class="text-center text-gray-400 py-4">
          No tasks for today.
        </div>
        <div v-else class="space-y-4">
          <div
              v-for="task in customTasks"
              :key="task.id"
              class="rounded-2xl p-5 bg-white shadow-sm flex items-center justify-between
                   transition hover:-translate-y-0.5 duration-200 animate-fade-card"
              :class="task.completed ? 'opacity-60 line-through' : ''"
              @click="toggleDbTask(task.id)"
          >
            <div>
              <h4 class="text-lg font-medium">{{ task.title }}</h4>
              <p class="text-sm text-gray-500">🕒 {{ task.subtitle }}</p>
            </div>
            <CheckCircle2
                v-if="task.completed"
                class="w-6 h-6 text-[#A3B18A]"
                :stroke-width="2"
            />
            <Circle v-else class="w-6 h-6 text-gray-300" :stroke-width="2" />
          </div>
        </div>
      </section>

      <!-- ===== 勋章墙按钮（白卡片样式 + 图标分隔） ===== -->
      <div
          class="mt-10 bg-white rounded-3xl p-6 text-[#1D1D1D]
               shadow-[0_6px_16px_rgba(0,0,0,0.08)]
               flex items-center justify-between cursor-pointer
               hover:-translate-y-0.5 active:scale-95 transition-all duration-300"
          @click="goShowModel"
      >
        <div class="flex items-center gap-4">
          <!-- 图标区 -->
          <div class="w-14 h-14 rounded-2xl bg-[#F1F4EF] flex items-center justify-center">
            <Medal class="w-7 h-7 text-[#7E8C77]" :stroke-width="2" />
          </div>

          <!-- 文本区 -->
          <div>
            <h3 class="text-xl font-semibold tracking-tight">Wall of Medals</h3>
            <p class="text-sm text-gray-500">
              View your achievements and progress history
            </p>
          </div>
        </div>
      </div>


      <!-- ===== 悬浮“添加”按钮 ===== -->
      <button
          class="fixed bottom-20 right-6 w-14 h-14 rounded-full bg-gradient-to-br from-[#A3B18A] to-[#7E8C77]
               text-white text-3xl font-bold shadow-lg hover:scale-110 active:scale-95
               transition-all flex items-center justify-center"
          @click="goAddTask"
      >
        +
      </button>
    </div>
  </base-layout>
</template>

<script setup lang="ts">
import { ref, computed, getCurrentInstance, onMounted } from 'vue'
import { CheckCircle2, Circle, Medal, Sparkles } from 'lucide-vue-next'
const { proxy } = getCurrentInstance()



/* Bonus 任务 */

/* 数据库读取任务 */
const reminders = ref<any[]>([])
const loading = ref(false)
const generatingAi = ref(false)
const generatingBonus = ref(false)
function getTodayRange() {
  const now = new Date()
  const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0)
  const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59)
  return { start, end }
}


const toDate = (v: string) => new Date(String(v).replace(/-/g, '/'))
const isHighPriority = (priority: string | null | undefined) => String(priority || '').toLowerCase() === 'high'
const isSuperHighPriority = (priority: string | null | undefined) => String(priority || '').toLowerCase() === 'superhigh'

async function fetchReminders() {
  try {
    loading.value = true
    const userRes = await proxy.$cf.login.getLoginUser()
    if (!userRes?.success) return
    const res = await proxy.$cf.table.list({
      table_name: 'reminder_item',
      param: { user_info_user_info_id_1: userRes.data.user_info_id },
      orderby: 'reminder_time',
      sort: 'asc',
      limit: 500,
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
function formatTime(dt: string) {
  const d = toDate(dt)
  const hh = String(d.getHours()).padStart(2, '0')
  const mm = String(d.getMinutes()).padStart(2, '0')
  return `${hh}:${mm}`
}
const highPriorityTasks = computed(() =>
    reminders.value
        .filter(r => isHighPriority(r.priority))
        .map(r => ({
          id: r.reminder_item_id,
          title: r.title,
          description: r.description,
          subtitle: formatTime(r.reminder_time),
          points: 1,
          completed: !!r.is_completed,
        }))
)

const bonusTasks = computed(() =>
    reminders.value
        .filter(r => isSuperHighPriority(r.priority))
        .map(r => ({
          id: r.reminder_item_id,
          title: r.title,
          description: r.description,
          subtitle: formatTime(r.reminder_time),
          points: 2,
          completed: !!r.is_completed,
        }))
)

const customTasks = computed(() =>
    reminders.value
        .filter(r => !isHighPriority(r.priority) && !isSuperHighPriority(r.priority))
        .map(r => ({
          id: r.reminder_item_id,
          title: r.title,
          subtitle: formatTime(r.reminder_time),
          points: 0.5,
          completed: !!r.is_completed,
        }))
)
async function toggleDbTask(id: string) {
  const item = reminders.value.find(r => r.reminder_item_id === id)
  if (!item) return
  const next = !item.is_completed
  item.is_completed = next
  await proxy.$cf.table.update({
    table_name: 'reminder_item',
    param: { reminder_item_id: id, is_completed: next },
  })
}
type GenerationMode = 'regular' | 'bonus'

async function runAiGeneration(mode: GenerationMode) {
  const state = mode === 'bonus' ? generatingBonus : generatingAi
  if (state.value) {
    return
  }
  state.value = true
  const hasUni = typeof uni !== 'undefined'
  const loadingTitle = mode === 'bonus' ? 'Planning bonus...' : 'Planning...'
  try {
    if (hasUni && typeof uni.showLoading === 'function') {
      uni.showLoading({ title: loadingTitle })
    }
    const res = await (mode === 'bonus'
        ? proxy?.$cf?.todo?.generateAiBonusSuggestions?.()
        : proxy?.$cf?.todo?.generateAiSuggestions?.())
    if (!res?.success) {
      throw new Error(res?.message || (mode === 'bonus' ? 'Failed to generate AI bonus tasks' : 'Failed to generate AI tasks'))
    }
    const summary = res?.data?.summary
    const taskCount = Array.isArray(res?.data?.tasks) ? res.data.tasks.length : 0
    await fetchReminders()
    const defaultCount = mode === 'bonus' ? 1 : 3
    const resolvedCount = taskCount || defaultCount
    const fallbackLabel = (() => {
      if (mode === 'bonus') {
        return resolvedCount > 1 ? 'bonus tasks' : 'bonus task'
      }
      return resolvedCount > 1 ? 'tasks' : 'task'
    })()
    const successMessage = summary || `AI added ${resolvedCount} ${fallbackLabel} for today`
    if (proxy?.$cf?.toast) {
      proxy.$cf.toast({ message: successMessage, level: 'success' })
    } else if (hasUni && typeof uni.showToast === 'function') {
      uni.showToast({ title: successMessage, icon: 'success', duration: 2000 })
    }
  } catch (error: any) {
    const fallbackLabel = mode === 'bonus' ? 'AI bonus task' : 'AI tasks'
    const message = error?.message || `Failed to generate ${fallbackLabel}`
    if (proxy?.$cf?.toast) {
      proxy.$cf.toast({ message, level: 'error' })
    } else if (hasUni && typeof uni.showToast === 'function') {
      uni.showToast({ title: message, icon: 'none', duration: 2000 })
    }
  } finally {
    state.value = false
    if (hasUni && typeof uni.hideLoading === 'function') {
      uni.hideLoading()
    }
  }
}

function generateAiTodos() {
  return runAiGeneration('regular')
}

function generateAiBonusTodos() {
  return runAiGeneration('bonus')
}

/* 积分逻辑 */
const totalPoints = computed(() =>
    [...highPriorityTasks.value, ...bonusTasks.value, ...customTasks.value]
        .filter(t => t.completed)
        .reduce((sum, t) => sum + t.points, 0)
)
const TARGET_POINTS = 5
const progressPercent = computed(() => Math.min(100, (totalPoints.value / TARGET_POINTS) * 100))
const unlockReady = computed(() => totalPoints.value >= TARGET_POINTS)
const doneLabel = computed(() => `${totalPoints.value} / ${TARGET_POINTS} pts`)

/* 宝箱逻辑 */
async function onChestClick() {
  if (!unlockReady.value) return goShowModel()
  try {
    await addMedalRecord()
  } catch (e) {
    console.error('添加奖牌失败：', e)
  } finally {
    goWinModel()
  }
}

/* 添加奖章 */
async function addMedalRecord() {
  const userRes = await proxy.$cf.login.getLoginUser()
  if (!userRes?.success) return
  const medalData = {
    user_info_user_info_id_1: userRes.data.user_info_id,
    medal_title: 'Daily Achievement Trophy 🏆',
    medal_date: new Date().toISOString(),
    medal_points: totalPoints.value,
  }
  await proxy.$cf.table.insert({
    table_name: 'medal_wall',
    param: medalData,
  })
}

/* 页面跳转 */
function goAddTask() {
  const url = '/pagesA/todo/add_task/index'
  proxy?.$cf?.navigate?.to ? proxy.$cf.navigate.to({ url }) : uni.navigateTo({ url })
}
function goShowModel() {
  const url = '/pagesA/todo/show_modal/leaderboard/index'
  proxy?.$cf?.navigate?.to ? proxy.$cf.navigate.to({ url }) : uni.navigateTo({ url })
}
function goWinModel() {
  const url = '/pagesA/todo/win_modal/index'
  proxy?.$cf?.navigate?.to ? proxy.$cf.navigate.to({ url }) : uni.navigateTo({ url })
}

onMounted(fetchReminders)


</script>

<style scoped>
/* ===== 顶部栏样式 ===== */
.topbar {
  position: sticky;        /* 关键点：滚动时固定顶部 */
  top: 0;
  z-index: 999; /* 提升层级，压过底部tabbar或背景 */           /* 确保在内容之上 */
  display: flex;
  align-items: center;
  justify-content: center;
  height: 112rpx;          /* 约等于 56px */
  width: 100%;
  background: #fff;        /* 白色背景覆盖下方内容 */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04); /* 轻微阴影 */
}

/* 左边三横图标贴左 */
.icon {
  width: 64rpx;
  height: 48rpx;
  margin-left: 20rpx;
}

/* 居中标题 */
.title {
  flex: 1;
  text-align: center;
  font-size: 50rpx;
  font-weight: 700;
  color: #1d1d1d;
}

/* 右侧平衡用占位 */
.right-spacer {
  width: 80rpx;
}


.animate-fade-card {
  animation: fade-card 0.3s ease-out both;
}
@keyframes fade-card {
  from {
    opacity: 0;
    transform: translateY(6px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
