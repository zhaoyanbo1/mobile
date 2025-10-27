<!-- src/pagesA/activities/manage.vue -->
<template>
  <base-layout>
    <view class="page">
      <view class="topbar">
        <button class="ghost-btn" @click="goBack">
          <text class="ghost-text">Back</text>
        </button>
        <text class="title">My activities</text>
        <view></view>
      </view>

      <view v-if="error" class="error-banner">
        <text>{{ error }}</text>
      </view>

      <scroll-view class="activities" scroll-y>
        <view v-if="!loading && !myActivities.length" class="empty">
          <text>No hosted activities yet. Create one to get started.</text>
        </view>

        <view
            class="activity-card"
            v-for="activity in myActivities"
            :key="activity.id"
        >
          <view class="card-header">
            <text class="card-title">{{ activity.title }}</text>
            <button class="ghost-btn small" @click="openEdit(activity)">
              <text class="ghost-text">Edit</text>
            </button>
          </view>

          <text class="card-description">{{ activity.description }}</text>
          <view class="card-info">
            <text>🗓 {{ activity.time }}</text>
            <text>📍 {{ activity.location }}</text>
            <text>👥 {{ activity.participantsCount }}/{{ activity.maxParticipants }}</text>
          </view>

          <view class="divider"></view>

          <view class="participants">
            <text class="section-title">Participants</text>
            <view class="pill" v-for="person in activity.participants" :key="person.id">
              <text>{{ person.id === currentUser.id ? 'You' : person.name }}</text>
            </view>
          </view>

          <view class="requests" v-if="activity.pendingApplicants.length">
            <text class="section-title">Pending requests</text>
            <view
                class="request"
                v-for="person in activity.pendingApplicants"
                :key="person.id"
            >
              <text class="request-name">{{ person.name }}</text>
              <view class="request-actions">
                <button class="primary-btn approve" @click="decide(activity, person, true)">Approve</button>
                <button class="ghost-btn reject" @click="decide(activity, person, false)">
                  <text class="ghost-text">Reject</text>
                </button>
              </view>
            </view>
          </view>
          <view class="requests" v-else>
            <text class="section-title muted">No new requests</text>
          </view>
        </view>
      </scroll-view>

      <view v-if="editing" class="modal-mask" @click.self="closeEdit">
        <view class="modal">
          <text class="modal-title">Edit activity</text>

          <view class="form-field">
            <text class="label">Title</text>
            <uni-easyinput
                v-model="editForm.title"
                placeholder="Activity name"
                class="input-control"
            />
          </view>

          <view class="form-field">
            <text class="label">Description</text>
            <textarea class="textarea" v-model.trim="editForm.description" />
          </view>

          <view class="form-field">
            <text class="label">Date & time</text>
            <uni-datetime-picker
                type="datetime"
                v-model="editForm.time"
                :clear-icon="false"
                class="input-control"
            />
          </view>

          <view class="form-field">
            <text class="label">Location</text>
            <uni-easyinput
                v-model="editForm.location"
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
                  :value="getParticipantIndex(editForm.minParticipants)"
                  @change="onSelectMin"
                  class="picker"
              >
                <view class="picker-display">{{ editForm.minParticipants }} people</view>
              </picker>
            </view>
            <view>
              <text class="label">Max people</text>
              <picker
                  mode="selector"
                  :range="participantOptions"
                  :value="getParticipantIndex(editForm.maxParticipants)"
                  @change="onSelectMax"
                  class="picker"
              >
                <view class="picker-display">{{ editForm.maxParticipants }} people</view>
              </picker>
            </view>
          </view>

          <text v-if="formError" class="error">{{ formError }}</text>

          <view class="modal-actions">
            <button class="ghost-btn" @click="closeEdit"><text class="ghost-text">Cancel</text></button>
            <button class="primary-btn" @click="submitEdit" :disabled="submitting">
              <text>{{ submitting ? 'Saving…' : 'Save changes' }}</text>
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

const { proxy } = getCurrentInstance()

const currentUser = ref({ id: null, name: '' })
const activities = ref([])
const loading = ref(false)
const error = ref('')
const participantOptions = Object.freeze(Array.from({ length: 9 }, (_, index) => index + 2))

const editing = ref(false)
const editingId = ref('')
const editForm = reactive({
  title: '',
  description: '',
  time: '',
  location: '',
  minParticipants: 2,
  maxParticipants: 6
})
const formError = ref('')
const submitting = ref(false)

const myActivities = computed(() => activities.value)

function mapUser (raw) {
  if (!raw) return { id: null, name: '' }
  return { id: raw.id ?? raw.userId ?? null, name: raw.name ?? raw.username ?? '' }
}

function mapActivity (raw) {
  return {
    id: raw.id,
    title: raw.title,
    description: raw.description,
    time: raw.activityTime,
    timeIso: raw.activityTimeIso || normalizePickerValue(raw.activityTime),
    location: raw.location,
    minParticipants: raw.minParticipants,
    maxParticipants: raw.maxParticipants,
    participantsCount: raw.participantsCount ?? (raw.participants?.length || 0),
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
  editForm.minParticipants = selected
  if (editForm.maxParticipants < selected) {
    editForm.maxParticipants = selected
  }
}

function onSelectMax (event) {
  const idx = Number(event?.detail?.value || participantOptions.length - 1)
  const selected = participantOptions[idx] ?? participantOptions[participantOptions.length - 1]
  editForm.maxParticipants = selected
  if (editForm.minParticipants > selected) {
    editForm.minParticipants = selected
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

function toPickerValue (value) {
  return normalizePickerValue(value)
}

function toApiDateTime (value) {
  if (!value) return ''
  const normalized = normalizePickerValue(value)
  return normalized ? normalized.slice(0, 16) : ''
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

async function loadActivities () {
  try {
    loading.value = true
    error.value = ''
    const user = await ensureUser()
    const data = await callApi('GET', '/api/team-activities/manage', null, { userId: user.id })
    activities.value = (Array.isArray(data) ? data : []).map(mapActivity)
  } catch (err) {
    error.value = err?.message || 'Failed to load activities'
    activities.value = []
  } finally {
    loading.value = false
  }
}

function goBack () {
  uni.navigateBack()
}

function openEdit (activity) {
  editingId.value = activity.id
  editForm.title = activity.title
  editForm.description = activity.description
  editForm.time = toPickerValue(activity.timeIso || activity.time)
  editForm.location = activity.location
  editForm.minParticipants = activity.minParticipants
  editForm.maxParticipants = activity.maxParticipants
  editing.value = true
  formError.value = ''
}

function closeEdit () {
  if (!submitting.value) {
    editing.value = false
    editingId.value = ''
  }
}

function validateForm () {
  if (!editForm.title || !editForm.description || !editForm.time || !editForm.location) {
    formError.value = 'Please complete all fields.'
    return false
  }
  if (editForm.minParticipants < 2) {
    formError.value = 'At least two people are required.'
    return false
  }
  if (editForm.maxParticipants > 10) {
    formError.value = 'Maximum group size is ten.'
    return false
  }
  if (editForm.minParticipants > editForm.maxParticipants) {
    formError.value = 'Min people cannot exceed max people.'
    return false
  }
  const activity = activities.value.find(act => act.id === editingId.value)
  if (activity && activity.participantsCount > editForm.maxParticipants) {
    formError.value = `Max people must be at least ${activity.participantsCount}.`
    return false
  }
  formError.value = ''
  return true
}

async function submitEdit () {
  if (!validateForm()) return
  try {
    submitting.value = true
    const user = await ensureUser()
    await callApi('PUT', `/api/team-activities/${editingId.value}`, {
      title: editForm.title,
      description: editForm.description,
      time: toApiDateTime(editForm.time),
      location: editForm.location,
      minParticipants: editForm.minParticipants,
      maxParticipants: editForm.maxParticipants,
      userId: user.id
    })
    uni.showToast({ title: 'Activity updated', icon: 'success' })
    closeEdit()
    await loadActivities()
  } catch (err) {
    formError.value = err?.message || 'Failed to update activity'
  } finally {
    submitting.value = false
  }
}

async function decide (activity, person, approve) {
  try {
    const user = await ensureUser()
    await callApi('POST', `/api/team-activities/${activity.id}/decide`, {
      applicantId: person.id,
      approve,
      userId: user.id
    })
    uni.showToast({ title: approve ? 'Approved' : 'Rejected', icon: 'success' })
    await loadActivities()
  } catch (err) {
    uni.showToast({ title: err?.message || 'Action failed', icon: 'none' })
  }
}

onShow(() => {
  loadActivities()
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

.ghost-btn.small {
  padding: 12rpx 20rpx;
  font-size: 28rpx;
}

.ghost-text {
  font-size: 34rpx;
  font-weight: 600;
  color: var(--accent);
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

.divider {
  margin: 20rpx 0;
  border-bottom: 2rpx solid rgba(19, 19, 19, 0.08);
}

.participants,
.requests {
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 700;
  color: var(--text);
  margin-bottom: 16rpx;
}

.section-title.muted {
  color: var(--muted);
  font-weight: 500;
}

.pill {
  display: inline-flex;
  align-items: center;
  padding: 10rpx 20rpx;
  margin: 0 12rpx 12rpx 0;
  border-radius: 999rpx;
  background: rgba(75, 116, 255, 0.12);
  color: var(--accent);
  font-size: 28rpx;
  font-weight: 600;
}

.request {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(243, 244, 248, 0.9);
  border-radius: 24rpx;
  padding: 20rpx 24rpx;
  margin-bottom: 16rpx;
}

.request-name {
  font-size: 32rpx;
  font-weight: 600;
  color: var(--text);
}

.request-actions {
  display: flex;
  gap: 16rpx;
}

.primary-btn {
  padding: 16rpx 28rpx;
  border-radius: 20rpx;
  font-size: 30rpx;
  font-weight: 700;
  color: #fff;
  background: linear-gradient(135deg, #4b74ff, #6a99ff);
  box-shadow: 0 12rpx 24rpx rgba(75, 116, 255, 0.2);
}

.primary-btn.approve {
  background: linear-gradient(135deg, #3fb68b, #5bd7a5);
  box-shadow: 0 12rpx 24rpx rgba(63, 182, 139, 0.24);
}

.ghost-btn.reject {
  background: rgba(255, 255, 255, 0.9);
  border: 2rpx solid rgba(209, 67, 67, 0.35);
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