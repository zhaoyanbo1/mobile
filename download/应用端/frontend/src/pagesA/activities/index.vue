<!-- src/pagesA/activities/index.vue -->
<template>
  <base-layout>
    <view class="page">
      <!-- 顶部栏：跟 Add Task 一致 -->
      <view class="topbar">
        <view class="back-wrap" @click="goBack">
          <text class="back-icon"><</text>
        </view>
        <text class="title">Team activities</text>
        <view class="right-spacer"></view>
      </view>

      <!-- 顶部工具行：Create / My activities -->
      <view class="toolbar">
        <button class="pill-btn primary" @click="openCreateModal">
          <image :src="icons.plus" mode="aspectFit" class="pill-icon" />
          <text class="pill-text">Create</text>
        </button>
        <button class="pill-btn ghost" @click="goManage">
          <text class="pill-text">My activities</text>
        </button>
      </view>

      <!-- 错误提示 -->
      <view v-if="error" class="error-banner">
        <text>{{ error }}</text>
      </view>

      <!-- 即将到来的提醒 -->
      <view class="section-card" v-if="mySchedule.length">
        <text class="section-title">Upcoming reminders</text>
        <view class="schedule-list">
          <view
              class="schedule-item"
              v-for="item in mySchedule"
              :key="`${item.id}-${item.userId}`"
          >
            <text class="schedule-name">{{ item.title }}</text>
            <text class="schedule-time">{{ item.time }} · {{ item.location }}</text>
            <text class="schedule-meta">
              {{ item.userId === currentUser.id ? 'You' : item.userName }}
            </text>
          </view>
        </view>
      </view>

      <!-- 活动列表 -->
      <scroll-view class="activities" scroll-y>
        <view
            class="activity-card"
            v-for="activity in activities"
            :key="activity.id"
        >
          <view class="card-header">
            <text class="card-title">{{ activity.title }}</text>
            <text class="card-creator">
              Host · {{ activity.creator.id === currentUser.id ? 'You' : activity.creator.name }}
            </text>
          </view>

          <text class="card-description" v-if="activity.description">
            {{ activity.description }}
          </text>

          <view class="card-info">
            <text class="info-row">🗓 {{ activity.time }}</text>
            <text class="info-row">📍 {{ activity.location }}</text>
            <text class="info-row">👥 {{ activity.participantsCount }}/{{ activity.maxParticipants }}</text>
          </view>

          <view class="status-tags">
            <text v-if="activity.joined" class="tag tag-safe">Joined</text>
            <text v-else-if="activity.pending" class="tag tag-pending">Pending</text>
            <text v-if="activity.full" class="tag tag-muted">Full</text>
          </view>

          <!-- 主按钮：和 Add Task 的 Save 一致配色 -->
          <button
              class="primary-btn"
              :class="{ disabled: isApplyDisabled(activity) }"
              :disabled="isApplyDisabled(activity)"
              @click="apply(activity)"
          >
            <text v-if="activity.host">Manage</text>
            <text v-else-if="activity.joined">Joined</text>
            <text v-else-if="activity.pending">Pending approval</text>
            <text v-else-if="activity.full">Full</text>
            <text v-else>Apply to join</text>
          </button>
        </view>

        <view class="empty" v-if="!loading && !activities.length">
          <text>No activities yet. Tap create to start one.</text>
        </view>
      </scroll-view>

      <!-- 创建弹窗 -->
      <view v-if="showCreate" class="modal-mask" @click.self="closeCreateModal">
        <view class="modal">
          <view class="modal-head">
            <text class="modal-title">Create activity</text>
            <text class="modal-close" @click="closeCreateModal">×</text>
          </view>

          <view class="form-field">
            <text class="label">Title</text>
            <uni-easyinput
                v-model="createForm.title"
                placeholder="Activity name"
                class="input-control"
            />
          </view>

          <view class="form-field">
            <text class="label">Description</text>
            <textarea
                class="textarea"
                v-model.trim="createForm.description"
                placeholder="What will you do?"
            />
          </view>

          <view class="form-field">
            <text class="label">Date & time</text>
            <uni-datetime-picker
                type="datetime"
                v-model="createForm.time"
                :clear-icon="false"
                class="input-control"
            />
          </view>

          <view class="form-field">
            <text class="label">Location</text>
            <uni-easyinput
                v-model="createForm.location"
                placeholder="Where to meet?"
                class="input-control"
            />
          </view>

          <view class="form-field row">
            <view class="col">
              <text class="label">Min people</text>
              <picker
                  mode="selector"
                  :range="participantOptions"
                  :value="getParticipantIndex(createForm.minParticipants)"
                  @change="onSelectMin"
              >
                <view class="picker-display">{{ createForm.minParticipants }} people</view>
              </picker>
            </view>
            <view class="col">
              <text class="label">Max people</text>
              <picker
                  mode="selector"
                  :range="participantOptions"
                  :value="getParticipantIndex(createForm.maxParticipants)"
                  @change="onSelectMax"
              >
                <view class="picker-display">{{ createForm.maxParticipants }} people</view>
              </picker>
            </view>
          </view>

          <text v-if="formError" class="error">{{ formError }}</text>

          <view class="modal-actions">
            <button class="pill-btn ghost" @click="closeCreateModal">
              <text class="pill-text">Cancel</text>
            </button>
            <button class="pill-btn primary" @click="submitCreate" :disabled="submitting">
              <text>{{ submitting ? 'Saving…' : 'Create' }}</text>
            </button>
          </view>
        </view>
      </view>
    </view>
  </base-layout>
</template>

<script setup>
import { computed, reactive, ref, getCurrentInstance } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import plusIcon from '@/static/tianjia.png'

const icons = { plus: plusIcon }
const { proxy } = getCurrentInstance()

const showCreate = ref(false)
const createForm = reactive({
  title: '',
  description: '',
  time: '',
  location: '',
  minParticipants: 2,
  maxParticipants: 6
})
const formError = ref('')
const submitting = ref(false)
const loading = ref(false)
const error = ref('')

const currentUser = ref({ id: null, name: '' })
const activities = ref([])
const schedule = ref([])
const participantOptions = Object.freeze(Array.from({ length: 9 }, (_, index) => index + 2))

const mySchedule = computed(() => schedule.value)

function goBack () {
  history.length > 1 ? history.back() : uni.switchTab({ url: '/pages/index/index' })
}

function resetCreateForm () {
  createForm.title = ''
  createForm.description = ''
  createForm.time = ''
  createForm.location = ''
  createForm.minParticipants = 2
  createForm.maxParticipants = 6
  formError.value = ''
}

function openCreateModal () {
  showCreate.value = true
  formError.value = ''
}

function closeCreateModal () {
  if (!submitting.value) {
    showCreate.value = false
    resetCreateForm()
  }
}

function mapUser (raw) {
  if (!raw) return { id: null, name: '' }
  return { id: raw.id ?? raw.userId ?? null, name: raw.name ?? raw.username ?? '' }
}

function mapActivity (raw) {
  return {
    id: raw.id,
    title: raw.title,
    description: raw.description,
    time: raw.activityTime || formatIso(raw.activityTimeIso),
    timeIso: raw.activityTimeIso || normalizePickerValue(raw.activityTime),
    location: raw.location,
    minParticipants: raw.minParticipants,
    maxParticipants: raw.maxParticipants,
    participantsCount: raw.participantsCount ?? (raw.participants?.length || 0),
    host: !!raw.host,
    joined: !!raw.joined,
    pending: !!raw.pending,
    full: !!raw.full,
    creator: mapUser(raw.creator),
    participants: (raw.participants || []).map(mapUser),
    pendingApplicants: (raw.pendingApplicants || []).map(mapUser)
  }
}

function getParticipantIndex (value) {
  const idx = participantOptions.indexOf(Number(value))
  return idx === -1 ? 0 : idx
}

function onSelectMin (event) {
  const idx = Number(event?.detail?.value || 0)
  const selected = participantOptions[idx] ?? participantOptions[0]
  createForm.minParticipants = selected
  if (createForm.maxParticipants < selected) {
    createForm.maxParticipants = selected
  }
}

function onSelectMax (event) {
  const idx = Number(event?.detail?.value || participantOptions.length - 1)
  const selected = participantOptions[idx] ?? participantOptions[participantOptions.length - 1]
  createForm.maxParticipants = selected
  if (createForm.minParticipants > selected) {
    createForm.minParticipants = selected
  }
}

function normalizePickerValue (value) {
  if (!value) return ''
  const raw = String(value).replace('T', ' ').replace(/Z$/, '').trim()
  if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(raw)) return raw
  if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}$/.test(raw)) return `${raw}:00`
  const parsed = new Date(value)
  if (!Number.isNaN(parsed.getTime())) {
    const y = parsed.getFullYear()
    const m = String(parsed.getMonth() + 1).padStart(2, '0')
    const d = String(parsed.getDate()).padStart(2, '0')
    const hh = String(parsed.getHours()).padStart(2, '0')
    const mm = String(parsed.getMinutes()).padStart(2, '0')
    const ss = String(parsed.getSeconds()).padStart(2, '0')
    return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
  }
  return raw
}

function toApiDateTime (value) {
  if (!value) return ''
  const normalized = normalizePickerValue(value)
  return normalized ? normalized.slice(0, 16) : ''
}

function mapReminder (raw) {
  return {
    id: raw.reminderId ?? raw.id ?? `${raw.activityId || ''}-${raw.userId || ''}`,
    activityId: raw.activityId ?? null,
    userId: raw.userId ?? null,
    userName: raw.userName ?? (raw.user?.name ?? currentUser.value.name),
    title: raw.title ?? '',
    time: raw.time ?? formatIso(raw.timeIso),
    timeIso: raw.timeIso ?? '',
    location: raw.location ?? ''
  }
}

function formatIso (value) {
  if (!value) return ''
  const d = new Date(String(value).replace(/-/g, '/'))
  if (Number.isNaN(d.getTime())) return ''
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hh = String(d.getHours()).padStart(2, '0')
  const mm = String(d.getMinutes()).padStart(2, '0')
  return `${y}-${m}-${day} ${hh}:${mm}`
}

function getAuthHeaders () {
  const token = proxy?.$cf?.login?.getToken?.()
  return token ? { Authorization: `Bearer ${token}` } : {}
}

async function callApi (method, url, data, query = {}) {
  const base = 'http://40.82.192.142'
  const q = new URLSearchParams(query).toString()
  const full = `${base}${url}${q ? `?${q}` : ''}`
  const headers = { 'Content-Type': 'application/json', ...getAuthHeaders() }

  return new Promise((resolve, reject) => {
    uni.request({
      url: full,
      method,
      data,
      header: headers,
      success (res) {
        const payload = res.data
        if (payload?.code === 0) resolve(payload.data)
        else reject(new Error(payload?.message || 'Request failed'))
      },
      fail (err) {
        reject(err)
      }
    })
  })
}

async function ensureUser () {
  if (currentUser.value.id != null) return currentUser.value
  const res = await proxy?.$cf?.login?.getLoginUser?.()
  if (!res?.success) {
    throw new Error('Please login first')
  }
  currentUser.value = {
    id: res.data.user_info_id,
    name: res.data.username || ''
  }
  return currentUser.value
}

function validateForm () {
  if (!createForm.title || !createForm.description || !createForm.time || !createForm.location) {
    formError.value = 'Please complete all fields.'
    return false
  }
  if (createForm.minParticipants < 2) {
    formError.value = 'At least two people are required.'
    return false
  }
  if (createForm.maxParticipants > 10) {
    formError.value = 'Maximum group size is ten.'
    return false
  }
  if (createForm.minParticipants > createForm.maxParticipants) {
    formError.value = 'Min people cannot exceed max people.'
    return false
  }
  formError.value = ''
  return true
}

async function loadOverview () {
  try {
    loading.value = true
    error.value = ''
    const user = await ensureUser()
    const data = await callApi('GET', '/api/team-activities', null, { userId: user.id })
    const list = Array.isArray(data?.activities) ? data.activities : []
    activities.value = list.map(mapActivity)
    const reminders = Array.isArray(data?.schedule) ? data.schedule : []
    schedule.value = reminders.map(mapReminder)
    if (data?.currentUser) {
      currentUser.value = mapUser(data.currentUser)
    }
  } catch (err) {
    error.value = err?.message || 'Failed to load activities'
    activities.value = []
    schedule.value = []
  } finally {
    loading.value = false
  }
}

async function submitCreate () {
  if (!validateForm()) return
  try {
    submitting.value = true
    const user = await ensureUser()
    await callApi('POST', '/api/team-activities', {
      title: createForm.title,
      description: createForm.description,
      time: toApiDateTime(createForm.time),
      location: createForm.location,
      minParticipants: createForm.minParticipants,
      maxParticipants: createForm.maxParticipants,
      userId: user.id
    })
    uni.showToast({ title: 'Activity published', icon: 'success' })
    showCreate.value = false
    resetCreateForm()
    await loadOverview()
  } catch (err) {
    formError.value = err?.message || 'Failed to create activity'
  } finally {
    submitting.value = false
  }
}

function isApplyDisabled (activity) {
  if (activity.host) return false
  if (activity.joined || activity.pending) return true
  return activity.full
}

async function apply (activity) {
  if (activity.host) {
    goManage()
    return
  }
  if (isApplyDisabled(activity)) return
  try {
    const user = await ensureUser()
    await callApi('POST', `/api/team-activities/${activity.id}/apply`, null, {
      userId: user.id
    })
    uni.showToast({ title: 'Application sent', icon: 'success' })
    await loadOverview()
  } catch (err) {
    uni.showToast({ title: err?.message || 'Failed to apply', icon: 'none' })
  }
}

function goManage () {
  uni.navigateTo({ url: '/pagesA/activities/manage' })
}

onShow(() => {
  loadOverview()
})
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f8f8f8;
  display: flex;
  flex-direction: column;
}

/* 顶部栏 */
.topbar {
  display: grid;
  grid-template-columns: 80rpx 1fr 80rpx;
  align-items: center;
  height: 120rpx;
  background: #fff;
  padding: 0 24rpx;
  border-bottom: 1px solid rgba(0, 0, 0, 0.03);
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

/* 顶部两个按钮：并排展开，圆角收一点 */
.toolbar {
  display: flex;
  gap: 16rpx;
  padding: 18rpx 24rpx 12rpx;
}
.pill-btn {
  flex: 1;                  /* 关键：并排展开 */
  border: none;
  border-radius: 22rpx;     /* 不要 9999rpx 那么圆 */
  padding: 18rpx 22rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  box-shadow: 0 8rpx 18rpx rgba(0, 0, 0, 0.04);
}
.pill-btn.primary {
  background: #839f90;
  color: #fff;
}
.pill-btn.ghost {
  background: #fff;
  color: #1d1d1d;
  border: 1px solid rgba(131, 159, 144, 0.25);
}
.pill-icon {
  width: 28rpx;
  height: 28rpx;
}
.pill-text {
  font-size: 30rpx;
  font-weight: 600;
}

/* 错误提示 */
.error-banner {
  margin: 0 24rpx 12rpx;
  padding: 18rpx 20rpx;
  border-radius: 18rpx;
  background: rgba(241, 92, 92, 0.12);
  color: #d14343;
  font-size: 28rpx;
}

/* 提醒卡片 */
.section-card {
  background: #fff;
  border-radius: 20rpx;
  margin: 0 24rpx 16rpx;
  padding: 20rpx 20rpx 10rpx;
  box-shadow: 0 10rpx 22rpx rgba(0, 0, 0, 0.06);
}
.section-title {
  font-size: 34rpx; /* bigger */
  font-weight: 700;
  color: #1d1d1d;
  margin-bottom: 12rpx;
}
.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 14rpx;
}
.schedule-item {
  background: #f7faf8;
  border-radius: 16rpx;
  padding: 14rpx 14rpx 12rpx;
}
.schedule-name {
  font-size: 30rpx;
  font-weight: 700;
  color: #1d1d1d;
}
.schedule-time {
  font-size: 28rpx;
  color: #6b7280;
}
.schedule-meta {
  font-size: 26rpx;
  color: #9ca3af;
}

/* 活动列表 */
.activities {
  flex: 1;
  padding: 0 24rpx 120rpx;
  box-sizing: border-box;
}
.activity-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 22rpx 20rpx 20rpx;
  margin-top: 18rpx;
  box-shadow: 0 10rpx 22rpx rgba(0, 0, 0, 0.03);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 8rpx;
}
.card-title {
  font-size: 38rpx; /* bigger */
  font-weight: 700;
  color: #1d1d1d;
}
.card-creator {
  font-size: 28rpx;
  color: #6b7280;
}
.card-description {
  font-size: 30rpx; /* bigger */
  color: #374151;
  line-height: 1.5;
  margin-bottom: 12rpx;
}
.card-info {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
  font-size: 28rpx; /* bigger */
  color: #1f2937;
}
.info-row {
  display: flex;
  align-items: center;
  gap: 6rpx;
}

/* 状态 tag */
.status-tags {
  display: flex;
  gap: 12rpx;
  margin: 14rpx 0 10rpx;
}
.tag {
  padding: 6rpx 14rpx;
  border-radius: 999rpx;
  font-size: 26rpx;
  font-weight: 600;
}
.tag-safe {
  background: rgba(131, 159, 144, 0.28);
  color: #2f3d2f;
}
.tag-pending {
  background: rgba(244, 162, 89, 0.18);
  color: #b36524;
}
.tag-muted {
  background: rgba(107, 114, 128, 0.12);
  color: #6b7280;
}

/* 主按钮：圆角缩小一点 */
.primary-btn {
  width: 100%;
  margin-top: 14rpx;
  height: 90rpx;
  border-radius: 22rpx;
  background: #839f90;
  color: #fff;
  font-weight: 700;
  font-size: 30rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 12rpx 22rpx rgba(0, 0, 0, 0.06);
}
.primary-btn.disabled {
  background: rgba(131, 159, 144, 0.5);
  box-shadow: none;
}

/* 空态 */
.empty {
  text-align: center;
  padding: 70rpx 0 140rpx;
  color: #9ca3af;
  font-size: 28rpx;
}

/* 弹窗 */
.modal-mask {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.35);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx 24rpx;
  z-index: 50;
}
.modal {
  width: 100%;
  max-width: 720rpx;
  background: #fff;
  border-radius: 24rpx;
  padding: 28rpx 24rpx 26rpx;
  box-shadow: 0 20rpx 40rpx rgba(0, 0, 0, 0.12);
}
.modal-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}
.modal-title {
  font-size: 36rpx;
  font-weight: 700;
  color: #1d1d1d;
}
.modal-close {
  font-size: 50rpx;
  line-height: 1;
  color: #6b7280;
}
.form-field {
  display: flex;
  flex-direction: column;
  margin-bottom: 18rpx;
}
.form-field.row {
  flex-direction: row;
  gap: 14rpx;
}
.form-field .col {
  flex: 1;
}
.label {
  font-size: 26rpx;
  color: #4b5563;
  margin-bottom: 8rpx;
}
.input-control {
  width: 100%;
}
:deep(.uni-easyinput__content),
:deep(.uni-date-editor) {
  background: #f3f4f6 !important;
  border-radius: 16rpx !important;
  border: 1px solid #e5e7eb !important;
  min-height: 80rpx;
  padding: 0 24rpx;
}
:deep(.uni-easyinput__content-input),
:deep(.uni-date-editor input) {
  font-size: 30rpx;
  color: #1f2937;
}
.textarea {
  background: #f3f4f6;
  border-radius: 16rpx;
  min-height: 140rpx;
  padding: 16rpx 18rpx;
  font-size: 28rpx;
  border: 1px solid #e5e7eb;
}
.picker-display {
  background: #f3f4f6;
  border-radius: 16rpx;
  min-height: 80rpx;
  padding: 0 20rpx;
  display: flex;
  align-items: center;
  font-size: 30rpx;
  color: #1f2937;
}
.error {
  color: #d14343;
  font-size: 26rpx;
  margin-bottom: 16rpx;
}
.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12rpx;
}
</style>
