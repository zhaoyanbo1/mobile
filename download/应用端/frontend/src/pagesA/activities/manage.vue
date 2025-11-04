<!-- src/pagesA/activities/manage.vue -->
<template>
  <base-layout>
    <view class="page">
      <!-- 顶部栏 -->
      <view class="topbar">
        <view class="back-wrap" @click="goBack">
          <text class="back-icon"><</text>
        </view>
        <text class="title">Manage</text>
        <view class="right-spacer"></view>
      </view>

      <!-- 错误提示 -->
      <view v-if="error" class="error-banner">
        <text>{{ error }}</text>
      </view>

      <!-- 活动列表 -->
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
            <!-- 绿色 + 顶右 -->
            <button class="pill-btn primary small header-action" @click="openEdit(activity)">
              <text class="pill-text">Edit</text>
            </button>
          </view>

          <text class="card-description" v-if="activity.description">
            {{ activity.description }}
          </text>

          <view class="card-info">
            <text class="info-row">🗓 {{ activity.time }}</text>
            <text class="info-row">📍 {{ activity.location }}</text>
            <text class="info-row">👥 {{ activity.participantsCount }}/{{ activity.maxParticipants }}</text>
          </view>

          <view class="divider"></view>

          <!-- 已参加 -->
          <view class="participants">
            <text class="section-title">Participants</text>
            <view
                class="pill-chip"
                v-for="person in activity.participants"
                :key="person.id"
            >
              <text>{{ person.id === currentUser.id ? 'You' : person.name }}</text>
            </view>
          </view>

          <!-- 待审核 -->
          <view class="requests" v-if="activity.pendingApplicants.length">
            <text class="section-title">Pending requests</text>
            <view
                class="request"
                v-for="person in activity.pendingApplicants"
                :key="person.id"
            >
              <text class="request-name">{{ person.name }}</text>
              <view class="request-actions">
                <button class="pill-btn primary xs" @click="decide(activity, person, true)">
                  <text class="pill-text">Approve</text>
                </button>
                <button class="pill-btn ghost xs danger" @click="decide(activity, person, false)">
                  <text class="pill-text">Reject</text>
                </button>
              </view>
            </view>
          </view>
          <view class="requests" v-else>
            <text class="section-title muted">No new requests</text>
          </view>
        </view>
      </scroll-view>

      <!-- 编辑弹窗 -->
      <view v-if="editing" class="modal-mask" @click.self="closeEdit">
        <view class="modal">
          <view class="modal-head">
            <text class="modal-title">Edit activity</text>
            <text class="modal-close" @click="closeEdit">×</text>
          </view>

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

          <view class="form-field row">
            <view class="col">
              <text class="label">Min people</text>
              <picker
                  mode="selector"
                  :range="participantOptions"
                  :value="getParticipantIndex(editForm.minParticipants)"
                  @change="onSelectMin"
              >
                <view class="picker-display">{{ editForm.minParticipants }} people</view>
              </picker>
            </view>
            <view class="col">
              <text class="label">Max people</text>
              <picker
                  mode="selector"
                  :range="participantOptions"
                  :value="getParticipantIndex(editForm.maxParticipants)"
                  @change="onSelectMax"
              >
                <view class="picker-display">{{ editForm.maxParticipants }} people</view>
              </picker>
            </view>
          </view>

          <text v-if="formError" class="error">{{ formError }}</text>

          <!-- 并排铺满 -->
          <view class="modal-actions">
            <button class="pill-btn ghost w-1-2" @click="closeEdit">
              <text class="pill-text">Cancel</text>
            </button>
            <button class="pill-btn primary w-1-2" @click="submitEdit" :disabled="submitting">
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
const participantOptions = Object.freeze(Array.from({ length: 9 }, (_, i) => i + 2))

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

function normalizePickerValue (value) {
  if (!value) return ''
  const raw = String(value).replace('T', ' ').replace(/Z$/, '').trim()
  if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(raw)) return raw
  if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}$/.test(raw)) return `${raw}:00`
  const d = new Date(value)
  if (!Number.isNaN(d.getTime())) {
    const y = d.getFullYear()
    const m = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    const hh = String(d.getHours()).padStart(2, '0')
    const mm = String(d.getMinutes()).padStart(2, '0')
    const ss = String(d.getSeconds()).padStart(2, '0')
    return `${y}-${m}-${day} ${hh}:${mm}:${ss}`
  }
  return raw
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

function onSelectMin (e) {
  const idx = Number(e?.detail?.value || 0)
  const selected = participantOptions[idx] ?? participantOptions[0]
  editForm.minParticipants = selected
  if (editForm.maxParticipants < selected) {
    editForm.maxParticipants = selected
  }
}

function onSelectMax (e) {
  const idx = Number(e?.detail?.value || participantOptions.length - 1)
  const selected = participantOptions[idx] ?? participantOptions[participantOptions.length - 1]
  editForm.maxParticipants = selected
  if (editForm.minParticipants > selected) {
    editForm.minParticipants = selected
  }
}

function toPickerValue (v) {
  return normalizePickerValue(v)
}

function toApiDateTime (v) {
  if (!v) return ''
  const n = normalizePickerValue(v)
  return n ? n.slice(0, 16) : ''
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
  const act = activities.value.find(a => a.id === editingId.value)
  if (act && act.participantsCount > editForm.maxParticipants) {
    formError.value = `Max people must be at least ${act.participantsCount}.`
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

/* 错误提示 */
.error-banner {
  margin: 0 24rpx 12rpx;
  padding: 18rpx 20rpx;
  border-radius: 18rpx;
  background: rgba(241, 92, 92, 0.12);
  color: #d14343;
  font-size: 28rpx;
}

/* 列表 */
.activities {
  flex: 1;
  height: calc(100vh - 120rpx);
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
  align-items: center;
  justify-content: flex-start;  /* 不再用 space-between */
  gap: 12rpx;
  margin-bottom: 10rpx;
}

.header-action {
  margin-left: auto;
  margin-right: 0;
}

.card-title {
  font-size: 36rpx;
  font-weight: 700;
  color: #1d1d1d;
}
.card-description {
  font-size: 28rpx;
  color: #374151;
  line-height: 1.5;
  margin-bottom: 12rpx;
}
.card-info {
  display: flex;
  flex-direction: column;
  gap: 6rpx;
  font-size: 26rpx;
  color: #1f2937;
}
.info-row {
  display: flex;
  align-items: center;
  gap: 6rpx;
}
.divider {
  height: 1px;
  background: rgba(0, 0, 0, 0.04);
  margin: 16rpx 0 14rpx;
}

/* participants / requests */
.section-title {
  font-size: 28rpx;
  font-weight: 700;
  color: #1d1d1d;
  margin-bottom: 10rpx;
}
.section-title.muted {
  font-weight: 500;
  color: #9ca3af;
}
.pill-chip {
  display: inline-flex;
  align-items: center;
  padding: 8rpx 16rpx;
  margin: 0 12rpx 12rpx 0;
  border-radius: 999rpx;
  background: rgba(131, 159, 144, 0.18);
  color: #2f3d2f;
  font-size: 24rpx;
  font-weight: 500;
}
.request {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f2f4f3;
  border-radius: 16rpx;
  padding: 14rpx 14rpx 14rpx 20rpx;
  margin-bottom: 14rpx;
}
.request-name {
  font-size: 28rpx;
  color: #1d1d1d;
}
.request-actions {
  display: flex;
  gap: 10rpx;
}

/* 按钮统一 */
.pill-btn {
  border: none;
  border-radius: 22rpx;
  padding: 13rpx 22rpx;
  display: flex;
  align-items: center;
  gap: 8rpx;
  box-shadow: 0 6rpx 14rpx rgba(0, 0, 0, 0.03);
}
.pill-btn.small {
  padding: 10rpx 20rpx;
}
.pill-btn.xs {
  padding: 8rpx 16rpx;
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
.pill-btn.ghost.danger {
  border-color: rgba(209, 67, 67, 0.25);
  color: #d14343;
}
.pill-text {
  font-size: 26rpx;
  font-weight: 600;
}
.w-1-2 {
  flex: 1;
}

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
  gap: 12rpx;
  margin-top: 4rpx;
}
</style>
