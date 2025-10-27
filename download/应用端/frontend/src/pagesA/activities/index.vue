<!-- src/pagesA/activities/index.vue -->
<template>
  <base-layout>
    <view class="page">
      <view class="topbar">
        <button class="ghost-btn" @click="openCreateModal">
          <image :src="icons.plus" mode="aspectFit" class="ghost-icon" />
          <text class="ghost-text">Create</text>
        </button>
        <text class="title">Team activities</text>
        <button class="ghost-btn right" @click="goManage">
          <text class="ghost-text">My activities</text>
        </button>
      </view>

      <view v-if="error" class="error-banner">
        <text>{{ error }}</text>
      </view>

      <view class="schedule" v-if="mySchedule.length">
        <text class="schedule-title">Upcoming reminders</text>
        <view class="schedule-list">
          <view class="schedule-item" v-for="item in mySchedule" :key="`${item.id}-${item.userId}`">
            <text class="schedule-name">{{ item.title }}</text>
            <text class="schedule-time">{{ item.time }} · {{ item.location }}</text>
            <text class="schedule-meta">{{ item.userId === currentUser.id ? 'You' : item.userName }}</text>
          </view>
        </view>
      </view>

      <scroll-view class="activities" scroll-y>
        <view
            class="activity-card"
            v-for="activity in activities"
            :key="activity.id"
        >
          <view class="card-header">
            <text class="card-title">{{ activity.title }}</text>
            <text class="card-creator">Host · {{ activity.creator.id === currentUser.id ? 'You' : activity.creator.name }}</text>
          </view>
          <text class="card-description">{{ activity.description }}</text>
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

      <view v-if="showCreate" class="modal-mask" @click.self="closeCreateModal">
        <view class="modal">
          <text class="modal-title">Create activity</text>

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
            <textarea class="textarea" v-model.trim="createForm.description" placeholder="What will you do?" />
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

          <view class="form-field columns">
            <view>
              <text class="label">Min people</text>
              <picker
                  mode="selector"
                  :range="participantOptions"
                  :value="getParticipantIndex(createForm.minParticipants)"
                  @change="onSelectMin"
                  class="picker"
              >
                <view class="picker-display">{{ createForm.minParticipants }} people</view>
              </picker>
            </view>
            <view>
              <text class="label">Max people</text>
              <picker
                  mode="selector"
                  :range="participantOptions"
                  :value="getParticipantIndex(createForm.maxParticipants)"
                  @change="onSelectMax"
                  class="picker"
              >
                <view class="picker-display">{{ createForm.maxParticipants }} people</view>
              </picker>
            </view>
          </view>

          <text v-if="formError" class="error">{{ formError }}</text>

          <view class="modal-actions">
            <button class="ghost-btn" @click="closeCreateModal"><text class="ghost-text">Cancel</text></button>
            <button class="primary-btn" @click="submitCreate" :disabled="submitting">
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
  if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(raw)) {
    return raw
  }
  if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}$/.test(raw)) {
    return `${raw}:00`
  }
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
  const base = ''
  const q = new URLSearchParams(query).toString()
  const full = base + url + (q ? `?${q}` : '')
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
  if (isApplyDisabled(activity)) {
    return
  }
  try {
    const user = await ensureUser()
    await callApi('POST', `/api/team-activities/${activity.id}/apply`, null, { userId: user.id })
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
:root {
  --bg: #f6f7fb;
  --text: #131313;
  --muted: #5f6677;
  --accent: #4b74ff;
  --safe: #3fb68b;
  --pending: #f4a259;
  --disabled: #ccd2dc;
  --card: #ffffff;
  --shadow: 0 12rpx 32rpx rgba(15, 15, 40, 0.08);
}

.page {
  min-height: 100vh;
  background: var(--bg);
  display: flex;
  flex-direction: column;
}

.topbar {
  display: grid;
  grid-template-columns: 220rpx 1fr 220rpx;
  align-items: center;
  padding: 32rpx 30rpx 16rpx;
}

.title {
  text-align: center;
  font-size: 56rpx;
  font-weight: 800;
  color: var(--text);
}

.error-banner {
  margin: 0 36rpx;
  padding: 16rpx 20rpx;
  border-radius: 18rpx;
  background: rgba(241, 92, 92, 0.12);
  color: #d14343;
  font-size: 30rpx;
}

.ghost-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  padding: 16rpx 24rpx;
  border-radius: 999rpx;
  border: 2rpx solid rgba(75, 116, 255, 0.22);
  background: rgba(255, 255, 255, 0.86);
  box-shadow: 0 4rpx 10rpx rgba(15, 15, 40, 0.08);
}

.ghost-btn.right {
  justify-self: end;
}

.ghost-icon {
  width: 36rpx;
  height: 36rpx;
}

.ghost-text {
  font-size: 34rpx;
  font-weight: 600;
  color: var(--accent);
}

.schedule {
  padding: 0 36rpx 10rpx;
}

.schedule-title {
  font-size: 34rpx;
  font-weight: 700;
  color: var(--text);
  margin-bottom: 16rpx;
}

.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.schedule-item {
  padding: 24rpx;
  background: var(--card);
  border-radius: 26rpx;
  box-shadow: var(--shadow);
  display: flex;
  flex-direction: column;
  gap: 6rpx;
}

.schedule-name {
  font-size: 34rpx;
  font-weight: 700;
  color: var(--text);
}

.schedule-time {
  font-size: 28rpx;
  color: var(--muted);
}

.schedule-meta {
  font-size: 26rpx;
  color: var(--muted);
}

.activities {
  flex: 1;
  padding: 24rpx 36rpx 120rpx;
  box-sizing: border-box;
}

.activity-card {
  background: var(--card);
  border-radius: 32rpx;
  padding: 32rpx;
  margin-bottom: 32rpx;
  box-shadow: var(--shadow);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.card-title {
  font-size: 44rpx;
  font-weight: 800;
  color: var(--text);
}

.card-creator {
  font-size: 28rpx;
  color: var(--muted);
}

.card-description {
  font-size: 32rpx;
  color: var(--text);
  line-height: 1.5;
  margin-bottom: 18rpx;
}

.card-info {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
  font-size: 30rpx;
  color: var(--text);
}

.info-row {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.status-tags {
  display: flex;
  gap: 16rpx;
  margin: 16rpx 0;
}

.tag {
  padding: 6rpx 18rpx;
  border-radius: 999rpx;
  font-size: 28rpx;
  font-weight: 600;
}

.tag-safe {
  background: rgba(63, 182, 139, 0.15);
  color: #2f8f67;
}

.tag-pending {
  background: rgba(244, 162, 89, 0.18);
  color: #b36524;
}

.tag-muted {
  background: rgba(127, 136, 148, 0.16);
  color: #6b7280;
}

.primary-btn {
  width: 100%;
  margin-top: 12rpx;
  padding: 24rpx 0;
  border-radius: 20rpx;
  font-size: 36rpx;
  font-weight: 700;
  color: #fff;
  background: linear-gradient(135deg, #4b74ff, #6a99ff);
  box-shadow: 0 16rpx 26rpx rgba(75, 116, 255, 0.22);
}

.primary-btn.disabled {
  opacity: 0.6;
  box-shadow: none;
}

.empty {
  margin-top: 80rpx;
  text-align: center;
  font-size: 32rpx;
  color: var(--muted);
}

.modal-mask {
  position: fixed;
  inset: 0;
  background: rgba(12, 16, 40, 0.52);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx;
  z-index: 20;
}

.modal {
  width: 100%;
  max-width: 720rpx;
  background: var(--card);
  border-radius: 36rpx;
  padding: 40rpx;
  box-shadow: 0 24rpx 44rpx rgba(15, 15, 40, 0.18);
}

.modal-title {
  font-size: 44rpx;
  font-weight: 800;
  margin-bottom: 24rpx;
}

.form-field {
  display: flex;
  flex-direction: column;
  margin-bottom: 24rpx;
}

.form-field.columns {
  flex-direction: row;
  gap: 24rpx;
}

.form-field.columns view {
  flex: 1;
}

.label {
  font-size: 28rpx;
  color: var(--muted);
  margin-bottom: 12rpx;
}

.input,
.textarea {
  background: #f3f4f8;
  border-radius: 20rpx;
  padding: 24rpx;
  font-size: 32rpx;
  color: var(--text);
}

.textarea {
  min-height: 160rpx;
}

.input-control {
  width: 100%;
}

.picker {
  width: 100%;
}

.picker-display {
  background: #f3f4f8;
  border-radius: 20rpx;
  padding: 24rpx;
  font-size: 32rpx;
  color: var(--text);
}

:deep(.uni-easyinput__content),
:deep(.uni-date-editor) {
  background: #f3f4f8 !important;
  border-radius: 20rpx;
  min-height: 80rpx;
  padding: 0 24rpx;
  border: none;
}

:deep(.uni-easyinput__content-input),
:deep(.uni-date-editor input) {
  font-size: 32rpx;
  color: var(--text);
}

:deep(.uni-easyinput__placeholder-class),
:deep(.uni-date-editor .uni-input-placeholder) {
  color: var(--muted) !important;
}

.error {
  color: #d14343;
  font-size: 28rpx;
  margin-bottom: 20rpx;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 18rpx;
}
</style>