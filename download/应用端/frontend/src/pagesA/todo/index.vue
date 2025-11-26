<template>
  <base-layout>
    <!-- Topbar -->
    <view class="topbar">
      <image :src="icons.menu" mode="widthFix" class="icon" />
      <text class="title">Todo</text>
      <view class="right-spacer"></view>
    </view>

    <div class="flex flex-col px-6 pt-16 pb-28 min-h-screen bg-[#F8F9F8] relative">
      <!-- ===== Top title + chest button ===== -->
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

      <!-- ===== Today's progress bar ===== -->
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

      <!-- ===== System tasks ===== -->
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

      <!-- ===== Bonus tasks ===== -->
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

      <!-- ===== Custom tasks ===== -->
      <section>
        <h4 class="mb-3 text-[#7E8C77] font-semibold text-base">
          Custom Tasks (0.5 pt each)
        </h4>
        <div v-if="loading" class="text-center text-gray-400 py-4">
          Loading...
        </div>
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

      <!-- ===== Medal wall button ===== -->
      <div
          class="mt-10 bg-white rounded-3xl p-6 text-[#1D1D1D]
               shadow-[0_6px_16px_rgba(0,0,0,0.08)]
               flex items-center justify-between cursor-pointer
               hover:-translate-y-0.5 active:scale-95 transition-all duration-300"
          @click="goShowModel"
      >
        <div class="flex items-center gap-4">
          <div class="w-14 h-14 rounded-2xl bg-[#F1F4EF] flex items-center justify-center">
            <Medal class="w-7 h-7 text-[#7E8C77]" :stroke-width="2" />
          </div>
          <div>
            <h3 class="text-xl font-semibold tracking-tight">Wall of Medals</h3>
            <p class="text-sm text-gray-500">
              View your achievements and progress history
            </p>
          </div>
        </div>
      </div>

      <!-- ===== Floating “Add” button ===== -->
      <button
          class="fixed bottom-20 right-6 w-14 h-14 rounded-full bg-gradient-to-br from-[#A3B18A] to-[#7E8C77]
               text-white text-3xl font-bold shadow-lg hover:scale-110 active:scale-95
               transition-all flex items-center justify-center"
          @click="goAddTask"
      >
        +
      </button>

      <!-- ✅ test button removed -->
    </div>
  </base-layout>
</template>

<script setup lang="ts">
import { ref, computed, getCurrentInstance, onMounted } from 'vue'
import { CheckCircle2, Circle, Medal, Sparkles } from 'lucide-vue-next'
import { LocalNotifications } from '@capacitor/local-notifications'

const { proxy } = getCurrentInstance()

/* ===== Notification related ===== */
const NOON_NOTICE_ID = 888000  // fixed id for the noon reminder

async function ensureNotifPermission() {
  try {
    const perm = await LocalNotifications.checkPermissions()
    if (perm.display !== 'granted') {
      await LocalNotifications.requestPermissions()
    }
  } catch (e) {
    console.warn('permission check failed:', e)
  }
}

function notifIdFromTaskId(id: string): number {
  let h = 0
  for (let i = 0; i < id.length; i++) h = (h * 31 + id.charCodeAt(i)) >>> 0
  return (h % 2147480000) + 1000
}

function nextFireDate(reminderISO: string): Date {
  const now = new Date()
  const t = new Date(reminderISO)
  return t.getTime() > now.getTime() ? t : new Date(now.getTime())
}

async function cancelAllPending() {
  try {
    const pending = await LocalNotifications.getPending()
    if (pending?.notifications?.length) {
      await LocalNotifications.cancel({ notifications: pending.notifications })
    }
  } catch (e) {
    console.warn('cancelAllPending failed:', e)
  }
}

async function scheduleForUnfinished(reminderRows: any[]) {
  await ensureNotifPermission()
  const unfinished = reminderRows.filter(r => !r.is_completed)
  if (!unfinished.length) return

  const notifications = unfinished.map(r => ({
    id: notifIdFromTaskId(String(r.reminder_item_id)),
    title: 'Todo Reminder',
    body: r.title + (r.description ? ` — ${r.description}` : ''),
    schedule: { at: nextFireDate(String(r.reminder_time)) },
    channelId: 'todo-reminders',
    smallIcon: 'ic_stat_icon',
    extra: { taskId: r.reminder_item_id }
  }))

  try {
    await LocalNotifications.schedule({ notifications })
  } catch (e) {
    console.warn('schedule failed:', e)
  }
}

async function cancelForTask(taskId: string) {
  try {
    await LocalNotifications.cancel({ notifications: [{ id: notifIdFromTaskId(String(taskId)) }] })
  } catch (e) {
    console.warn('cancelForTask failed:', e)
  }
}

async function handleTaskToggled(row: any) {
  if (row.is_completed) {
    await cancelForTask(row.reminder_item_id)
  } else {
    await scheduleForUnfinished([row])
  }
}

// daily 12:00 PM reminder
async function scheduleDailyNoonNotice() {
  await ensureNotifPermission()

  const now = new Date()
  const noon = new Date()
  noon.setHours(12, 0, 0, 0)
  if (noon.getTime() <= now.getTime()) {
    noon.setDate(noon.getDate() + 1)
  }

  try {
    // avoid duplicate
    await LocalNotifications.cancel({ notifications: [{ id: NOON_NOTICE_ID }] })
    await LocalNotifications.schedule({
      notifications: [
        {
          id: NOON_NOTICE_ID,
          title: 'Daily Tasks',
          body: 'Don’t forget to finish today’s system tasks and bonus tasks!',
          schedule: { at: noon },
          channelId: 'todo-reminders',
          smallIcon: 'ic_stat_icon'
        }
      ]
    })
  } catch (e) {
    console.warn('scheduleDailyNoonNotice failed:', e)
  }
}

async function initNotificationChannel() {
  try {
    await LocalNotifications.createChannel({
      id: 'todo-reminders',
      name: 'Todo Reminders',
      description: 'Daily todo reminders',
      importance: 4,
    })
  } catch (e) {
    // ignore for iOS / web
  }
}

/* ===== Data ===== */
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
    const userRes = await proxy?.$cf?.login?.getLoginUser?.()
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
    reminders.value = rows.filter((r: any) => {
      const t = toDate(r.reminder_time)
      return t >= start && t <= end
    })
  } finally {
    loading.value = false
  }

  // after entering the page:
  // clear -> schedule notifications for all unfinished tasks today -> schedule the noon reminder
  try {
    await cancelAllPending()
    await scheduleForUnfinished(reminders.value)
    await scheduleDailyNoonNotice()
  } catch (e) {
    console.warn('refresh schedule failed:', e)
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

  try {
    await handleTaskToggled(item)
  } catch (e) {
    console.warn('toggle notif failed:', e)
  }
}

type GenerationMode = 'regular' | 'bonus'

async function runAiGeneration(mode: GenerationMode) {
  const state = mode === 'bonus' ? generatingBonus : generatingAi
  if (state.value) return
  state.value = true
  const hasUni = typeof uni !== 'undefined'
  const loadingTitle = mode === 'bonus' ? 'Planning bonus...' : 'Planning...'
  const cfLoading = proxy?.$cf?.loading
  let usedCfLoading = false
  try {
    if (cfLoading?.showLoading) {
      try {
        cfLoading.showLoading({ title: loadingTitle })
        usedCfLoading = true
      } catch (e) {
        console.warn('failed to open cf loading:', e)
      }
    }
    if (!usedCfLoading && hasUni && typeof uni.showLoading === 'function') {
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
    const fallbackLabel = mode === 'bonus'
        ? (resolvedCount > 1 ? 'bonus tasks' : 'bonus task')
        : (resolvedCount > 1 ? 'tasks' : 'task')
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
    if (usedCfLoading && cfLoading?.hideLoading) {
      try {
        cfLoading.hideLoading()
      } catch (e) {
        console.warn('failed to close cf loading:', e)
      }
    }
    if (hasUni && typeof uni.hideLoading === 'function') {
      uni.hideLoading()
    }
  }
}

function generateAiTodos() { return runAiGeneration('regular') }
function generateAiBonusTodos() { return runAiGeneration('bonus') }

/* Points logic */
const totalPoints = computed(() =>
    [...highPriorityTasks.value, ...bonusTasks.value, ...customTasks.value]
        .filter(t => t.completed)
        .reduce((sum, t) => sum + t.points, 0)
)
const TARGET_POINTS = 5
const progressPercent = computed(() => Math.min(100, (totalPoints.value / TARGET_POINTS) * 100))
const unlockReady = computed(() => totalPoints.value >= TARGET_POINTS)
const doneLabel = computed(() => `${totalPoints.value} / ${TARGET_POINTS} pts`)

/* Chest logic */
async function onChestClick() {
  if (!unlockReady.value) return goShowModel()
  try {
    await addMedalRecord()
  } catch (e) {
    console.error('Failed to add medal:', e)
  } finally {
    goWinModel()
  }
}

/* Add medal */
async function addMedalRecord() {
  const userRes = await proxy.$cf.login.getLoginUser()
  if (!userRes?.success) return
  const medalData = {
    user_info_user_info_id_1: userRes.data.user_info_id,
    medal_title: 'Daily Achievement Trophy 🏆',
    medal_date: new Date().toISOString(),
    medal_points: totalPoints.value,
  }
  await proxy.$cf.table.insert({ table_name: 'medal_wall', param: medalData })
}

/* Navigation */
function goAddTask() {
  const url = '/pagesA/todo/add_task/index'
  // @ts-ignore
  return proxy?.$cf?.navigate?.to ? proxy.$cf.navigate.to({ url }) : (typeof uni !== 'undefined' ? uni.navigateTo({ url }) : void 0)
}
function goShowModel() {
  const url = '/pagesA/todo/show_modal/leaderboard/index'
  // @ts-ignore
  return proxy?.$cf?.navigate?.to ? proxy.$cf.navigate.to({ url }) : (typeof uni !== 'undefined' ? uni.navigateTo({ url }) : void 0)
}
function goWinModel() {
  const url = '/pagesA/todo/win_modal/index'
  // @ts-ignore
  return proxy?.$cf?.navigate?.to ? proxy.$cf.navigate.to({ url }) : (typeof uni !== 'undefined' ? uni.navigateTo({ url }) : void 0)
}

onMounted(async () => {
  await initNotificationChannel()
  await fetchReminders()
})
</script>

<style scoped>
.topbar {
  position: sticky;
  top: 0;
  z-index: 999;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 112rpx;
  width: 100%;
  background: #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);
}
.icon {
  width: 64rpx;
  height: 48rpx;
  margin-left: 20rpx;
}
.title {
  flex: 1;
  text-align: center;
  font-size: 50rpx;
  font-weight: 700;
  color: #1d1d1d;
}
.right-spacer {
  width: 80rpx;
}
.animate-fade-card {
  animation: fade-card 0.3s ease-out both;
}
@keyframes fade-card {
  from { opacity: 0; transform: translateY(6px); }
  to   { opacity: 1; transform: translateY(0); }
}
</style>
