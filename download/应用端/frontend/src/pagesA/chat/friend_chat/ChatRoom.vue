<template>
  <view class="chat-page">
    <!-- top bar -->
    <view class="topbar">
      <view class="back-wrap" @click="goBack">
        <text class="back-icon"><</text>
      </view>
      <text class="title">{{ peerName || 'Chat with my friend' }}</text>
      <view class="right-spacer"></view>
    </view>

    <!-- messages -->
    <scroll-view
        class="chat-body"
        :scroll-y="true"
        :scroll-with-animation="true"
        :scroll-top="scrollTop"
    >
      <view
          v-for="m in messages"
          :key="m.messageId"
          class="msg-item"
          :class="m.senderId === myId ? 'me' : 'other'"
      >
        <view
            class="bubble"
            :class="[
            m.senderId === myId ? 'bubble-me' : 'bubble-other',
            m.contentType === 'ACTIVITY_INVITE' ? 'bubble-card' : ''
          ]"
        >
          <!-- plain text -->
          <template v-if="m.contentType === 'TEXT'">
            {{ m.content }}
          </template>

          <!-- activity invite -->
          <template v-else-if="m.contentType === 'ACTIVITY_INVITE'">
            <view class="invite-card">
              <text class="invite-title">
                {{ m.content?.title || 'Activity invitation' }}
              </text>
              <text v-if="m.content?.time" class="invite-meta">
                Time: {{ m.content.time }}
              </text>
              <text v-if="m.content?.location" class="invite-meta">
                Location: {{ m.content.location }}
              </text>
              <text v-if="m.content?.maxParticipants" class="invite-meta">
                Participants: {{ m.content?.participantsCount || 0 }}/{{ m.content.maxParticipants }}
              </text>

              <view v-if="shouldShowInviteActions(m)" class="invite-actions">
                <button
                    class="invite-btn primary"
                    :disabled="m._processing"
                    @click="respondToInvite(m, 'ACCEPTED')"
                >
                  Accept
                </button>
                <button
                    class="invite-btn secondary"
                    :disabled="m._processing"
                    @click="respondToInvite(m, 'DECLINED')"
                >
                  Decline
                </button>
              </view>
              <text v-else class="invite-status">
                {{ inviteStatusText(m) }}
              </text>
            </view>
          </template>

          <!-- invite response -->
          <template v-else-if="m.contentType === 'ACTIVITY_INVITE_RESPONSE'">
            <text>{{ inviteResponseText(m) }}</text>
          </template>

          <!-- fallback -->
          <template v-else>
            {{ typeof m.content === 'string' ? m.content : JSON.stringify(m.content) }}
          </template>
        </view>
      </view>

      <!-- scroll spacer -->
      <view style="height: 12vh;"></view>
    </scroll-view>

    <!-- bottom composer -->
    <view class="composer">
      <view class="composer-surface">
        <button class="action-button" @click="toggleActivityPicker">
          {{ showActivityPicker ? 'Hide' : 'Activity' }}
        </button>
        <input
            class="composer-input"
            v-model="inputText"
            confirm-type="send"
            @confirm="sendTextMessage"
            placeholder="Type a message..."
        />
        <button class="send-button" @click="sendTextMessage">
          Send
        </button>
      </view>
    </view>

    <!-- activity picker -->
    <view v-if="showActivityPicker" class="activity-picker">
      <view class="picker-header">
        <text class="picker-title">My activities</text>
        <button class="picker-close" @click="toggleActivityPicker(false)">Close</button>
      </view>
      <scroll-view class="activity-list" scroll-y>
        <view v-if="activitiesLoading" class="picker-hint">Loading…</view>
        <view v-else-if="activitiesError" class="picker-hint">{{ activitiesError }}</view>
        <view v-else-if="!myActivities.length" class="picker-hint">
          No activities yet
        </view>
        <view
            v-for="activity in myActivities"
            :key="activity.id"
            class="activity-item"
            @click="sendActivityInvite(activity)"
        >
          <text class="activity-title">{{ activity.title }}</text>
          <text v-if="activity.time" class="activity-meta">Time: {{ activity.time }}</text>
          <text v-if="activity.location" class="activity-meta">Location: {{ activity.location }}</text>
          <text class="activity-meta">
            Participants: {{ activity.participantsCount }}/{{ activity.maxParticipants || '∞' }}
          </text>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import imApi from '@/api/page/im.js'
import activitiesApi from '@/api/page/activities.js'
import useNotificationStore from '@/api/utils/notificationStore'

const myId = ref(Number(uni.getStorageSync('uid')) || 0)
const myName = ref(uni.getStorageSync('username') || '')

const peerId = ref(null)
const peerName = ref('')
const conversationId = ref(null)
const messages = ref([])
const inputText = ref('')
const scrollTop = ref(0)

const showActivityPicker = ref(false)
const myActivities = ref([])
const activitiesLoading = ref(false)
const activitiesError = ref('')

const notifications = useNotificationStore()

onLoad(async (q) => {
  if (q?.peerId) peerId.value = Number(q.peerId)
  if (q?.nickname) peerName.value = decodeURIComponent(q.nickname)

  if (q?.conversationId) {
    conversationId.value = Number(q.conversationId)
  } else if (peerId.value) {
    const r = await imApi.ensureDm(peerId.value)
    conversationId.value = r?.data ?? r ?? null
  }

  syncConversationMapping()
  await loadMessages()
  scrollToBottom(true)
})

function parseContent(contentType, rawContent) {
  if (contentType === 'ACTIVITY_INVITE' || contentType === 'ACTIVITY_INVITE_RESPONSE') {
    if (rawContent && typeof rawContent === 'object') return { ...rawContent }
    try {
      return JSON.parse(rawContent)
    } catch (_) {
      return {}
    }
  }
  if (typeof rawContent === 'string') return rawContent
  if (rawContent == null) return ''
  return String(rawContent)
}

function normalizeMsg(m) {
  const type = m.contentType || 'TEXT'
  const raw = m.content ?? ''
  const parsed = parseContent(type, raw)
  if (type === 'ACTIVITY_INVITE' && parsed && typeof parsed === 'object' && !parsed.status) {
    parsed.status = 'PENDING'
  }
  return {
    messageId: m.messageId ?? Date.now(),
    senderId: m.senderId,
    contentType: type,
    content: parsed,
    rawContent: typeof raw === 'string' ? raw : JSON.stringify(raw),
    createdAtEpochMs: m.createdAtEpochMs ?? Date.now(),
  }
}

async function loadMessages() {
  if (!conversationId.value) return
  const res = await imApi.getMessages(conversationId.value, null, 20)
  const list = (res?.data?.list ?? res?.list ?? res ?? []).map(normalizeMsg)
  messages.value = list
  sortMessages()
  recomputeInviteStatuses()
  scrollToBottom(true)

  const latest = messages.value.length ? messages.value[messages.value.length - 1] : null
  syncConversationMapping()
  markMessagesAsRead(latest?.messageId)
}

function sortMessages() {
  messages.value.sort((a, b) => {
    const ta = a.createdAtEpochMs ?? 0
    const tb = b.createdAtEpochMs ?? 0
    if (ta !== tb) return ta - tb
    return Number(a.messageId ?? 0) - Number(b.messageId ?? 0)
  })
}

function scrollToBottom(immediate = false) {
  nextTick(() => {
    if (immediate) {
      scrollTop.value = 999999
    } else {
      scrollTop.value = scrollTop.value + 1
    }
  })
}

function toggleActivityPicker(force) {
  const next = typeof force === 'boolean' ? force : !showActivityPicker.value
  showActivityPicker.value = next
  if (next && !myActivities.value.length && !activitiesLoading.value) {
    loadMyActivities()
  }
}

async function loadMyActivities() {
  if (!myId.value) return
  activitiesLoading.value = true
  activitiesError.value = ''
  try {
    const res = await activitiesApi.manage({ userId: myId.value })
    const list = Array.isArray(res?.data) ? res.data : Array.isArray(res) ? res : []
    myActivities.value = list.map(mapActivity)
  } catch (err) {
    activitiesError.value = err?.message || 'Failed to load'
    myActivities.value = []
  } finally {
    activitiesLoading.value = false
  }
}

function mapActivity(raw) {
  if (!raw) return {}
  const timeDisplay = raw.activityTime || raw.time || ''
  const timeIso = raw.activityTimeIso || raw.timeIso || ''
  return {
    id: raw.id,
    title: raw.title || 'Untitled activity',
    time: timeDisplay,
    timeIso,
    location: raw.location || '',
    minParticipants: raw.minParticipants ?? 0,
    maxParticipants: raw.maxParticipants ?? 0,
    participantsCount: raw.participantsCount ?? (raw.participants?.length || 0),
  }
}

async function sendTextMessage() {
  const text = inputText.value.trim()
  if (!text) return
  inputText.value = ''
  await sendMessagePayload({ contentType: 'TEXT', content: text })
}

async function sendMessagePayload({ contentType, content }) {
  if (!conversationId.value && !peerId.value) {
    uni.showToast({ title: 'missing conversation', icon: 'none' })
    throw new Error('missing conversation')
  }

  const tempId = Date.now()
  const now = Date.now()
  const isStructured =
      contentType === 'ACTIVITY_INVITE' || contentType === 'ACTIVITY_INVITE_RESPONSE'
  const rawContent = typeof content === 'string' ? content : JSON.stringify(content ?? {})
  const localContent =
      isStructured && content && typeof content === 'object'
          ? { ...content }
          : typeof content === 'string'
              ? content
              : rawContent

  if (isStructured && localContent && typeof localContent === 'object' && !localContent.status) {
    localContent.status = 'PENDING'
  }

  const tempMsg = {
    messageId: tempId,
    senderId: myId.value,
    contentType,
    content: localContent,
    rawContent,
    createdAtEpochMs: now,
    _pending: true,
  }
  messages.value.push(tempMsg)
  sortMessages()
  recomputeInviteStatuses()
  scrollToBottom()

  try {
    const resp = await imApi.sendMessage({
      conversationId: conversationId.value,
      peerId: peerId.value,
      contentType,
      content: rawContent,
    })
    const saved = normalizeMsg(resp?.data ?? resp)
    if (!conversationId.value && saved.conversationId) {
      conversationId.value = saved.conversationId
    }
    const idx = messages.value.findIndex((m) => m.messageId === tempId)
    if (idx > -1) messages.value[idx] = saved
    else messages.value.push(saved)

    sortMessages()
    recomputeInviteStatuses()
    scrollToBottom()
    syncConversationMapping()
    markMessagesAsRead(saved?.messageId)
    return saved
  } catch (error) {
    messages.value = messages.value.filter((m) => m.messageId !== tempId)
    uni.showToast({ title: 'send failed', icon: 'none' })
    throw error
  }
}

function shouldShowInviteActions(message) {
  if (!message || message.contentType !== 'ACTIVITY_INVITE') return false
  if (message.senderId === myId.value) return false
  const status = (message.content?.status || 'PENDING').toUpperCase()
  return status === 'PENDING'
}

function inviteStatusText(message) {
  const status = (message?.content?.status || 'PENDING').toUpperCase()
  if (status === 'ACCEPTED') {
    return message.senderId === myId.value ? 'The other person accepted' : 'You joined this activity'
  }
  if (status === 'DECLINED') {
    return message.senderId === myId.value ? 'The other person declined' : 'You declined'
  }
  return message.senderId === myId.value ? 'Waiting for response' : 'Please choose to join or decline'
}

function inviteResponseText(message) {
  const data = message?.content || {}
  const status = (data.status || '').toUpperCase()
  const responderName =
      data.responderName || (message.senderId === myId.value ? 'You' : peerName.value || 'Friend')
  const title = data.title ? ` "${data.title}"` : ' the activity'
  if (status === 'ACCEPTED') return `${responderName} joined${title}`
  if (status === 'DECLINED') return `${responderName} declined${title}`
  return `${responderName} responded to${title}`
}

async function sendActivityInvite(activity) {
  if (!activity) return
  const inviteId = `invite-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`
  const payload = {
    inviteId,
    activityId: activity.id,
    title: activity.title,
    time: activity.time,
    timeIso: activity.timeIso,
    location: activity.location,
    maxParticipants: activity.maxParticipants,
    participantsCount: activity.participantsCount,
    hostId: myId.value,
    status: 'PENDING',
  }
  await sendMessagePayload({ contentType: 'ACTIVITY_INVITE', content: payload })
  uni.showToast({ title: 'Invite sent', icon: 'success' })
  toggleActivityPicker(false)
}

async function respondToInvite(message, decision) {
  if (!message || message.contentType !== 'ACTIVITY_INVITE') return
  if (message._processing) return
  const invite = message.content || {}
  if (!invite.activityId) {
    uni.showToast({ title: 'Missing activity id', icon: 'none' })
    return
  }
  message._processing = true
  try {
    if (decision === 'ACCEPTED') {
      await activitiesApi.acceptInvite(invite.activityId, { userId: myId.value })
    }
    const responsePayload = {
      inviteId: invite.inviteId,
      activityId: invite.activityId,
      title: invite.title,
      status: decision,
      responderId: myId.value,
      responderName: myName.value || '',
    }
    await sendMessagePayload({
      contentType: 'ACTIVITY_INVITE_RESPONSE',
      content: responsePayload,
    })
    message.content = { ...invite, status: decision }
    recomputeInviteStatuses()
    uni.showToast({ title: decision === 'ACCEPTED' ? 'Joined' : 'Declined', icon: 'none' })
  } finally {
    message._processing = false
  }
}

function recomputeInviteStatuses() {
  const statusMap = new Map()
  messages.value.forEach((msg) => {
    if (msg.contentType === 'ACTIVITY_INVITE_RESPONSE') {
      const data = msg.content || {}
      if (data.inviteId) {
        statusMap.set(data.inviteId, (data.status || 'PENDING').toUpperCase())
      }
    }
  })
  messages.value = messages.value.map((msg) => {
    if (msg.contentType !== 'ACTIVITY_INVITE') return msg
    const invite = msg.content && typeof msg.content === 'object' ? { ...msg.content } : {}
    const status =
        statusMap.get(invite.inviteId) || (invite.status || 'PENDING')
    invite.status = status.toUpperCase()
    return { ...msg, content: invite }
  })
}

function syncConversationMapping() {
  if (peerId.value && conversationId.value) {
    notifications.setConversationForFriend(peerId.value, conversationId.value)
  }
}

function markMessagesAsRead(latestMessageId) {
  if (peerId.value) {
    notifications.markFriendRead(peerId.value)
  }
  if (conversationId.value) {
    notifications.markConversationRead(conversationId.value)
  }
  if (conversationId.value && latestMessageId) {
    imApi.markRead(conversationId.value, latestMessageId).catch(() => {})
  }
}

function goBack() {
  history.length > 1 ? history.back() : null
}
</script>

<style scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  /* 让它不要比屏幕宽 */
  width: 100vw;
  max-width: 100vw;
  overflow-x: hidden;
  background: #f8f9f8;
  box-sizing: border-box;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial,
  "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
}

/* 固定在顶部，不跟着滚动 */
.topbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw; /* 或 100% 也行 */
  display: grid;
  grid-template-columns: 80rpx 1fr 80rpx;
  align-items: center;
  height: 112rpx;
  background: #fff;
  padding: 0 24rpx;
  box-shadow: 0 6rpx 18rpx rgba(0, 0, 0, 0.03);
  z-index: 100; /* 保证在最上面 */
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

/* 聊天主体要让出顶部的112rpx，不然会被顶栏盖住 */
.chat-body {
  flex: 1;
  padding: 24rpx 20rpx 0;
  /* 顶部留出 topbar 的高度 */
  margin-top: 112rpx;
  /* 最多占满屏幕，不横向滚动 */
  max-width: 100vw;
  box-sizing: border-box;
}
.msg-item {
  display: flex;
  margin-bottom: 20rpx;
}
.msg-item.me {
  justify-content: flex-end;
}
.msg-item.other {
  justify-content: flex-start;
}
.bubble {
  max-width: 78%;
  padding: 22rpx 26rpx;
  border-radius: 32rpx;
  box-shadow: 0 5rpx 16rpx rgba(0, 0, 0, 0.03);
  line-height: 1.5;
  font-size: 30rpx;
  word-break: break-word;
}
.bubble-other {
  background: #e6efe1;
  color: #314131;
  border-top-left-radius: 16rpx;
}
.bubble-me {
  background: #dde4d9;
  color: #2f3d2f;
  border-top-right-radius: 16rpx;
}

/* 底部输入区保持在底部 */
.composer {
  position: sticky;
  bottom: 0;
  padding: 0 22rpx 32rpx;
  background: linear-gradient(180deg, rgba(248, 249, 248, 0), #f8f9f8 55%, #f8f9f8 100%);
  z-index: 20;
  /* 不要超出屏幕宽度 */
  max-width: 100vw;
  box-sizing: border-box;
}
.composer-surface {
  display: flex;
  align-items: center;
  gap: 16rpx;
  background: #fff;
  border-radius: 26rpx;
  padding: 14rpx 18rpx;
  box-shadow: 0 12rpx 32rpx rgba(0, 0, 0, 0.04);
}
.composer-input {
  flex: 1;
  border: none;
  background: transparent;
  font-size: 30rpx;
  outline: none;
  color: #314131;
}
.composer-input::placeholder {
  color: rgba(49, 65, 49, 0.5);
}
.action-button {
  min-width: 150rpx;
  height: 72rpx;
  border-radius: 20rpx;
  border: none;
  background: rgba(122, 143, 119, 0.12);
  color: #7a8f77;
  font-size: 26rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
.send-button {
  min-width: 140rpx;
  height: 72rpx;
  border-radius: 20rpx;
  border: none;
  background: #7a8f77;
  color: #fff;
  font-size: 28rpx;
  font-weight: 600;
  box-shadow: 0 14rpx 28rpx rgba(122, 143, 119, 0.35);
}

/* 活动卡片 */
.bubble-card {
  background: transparent;
  box-shadow: none;
  padding: 0;
}
.invite-card {
  background: #f1f5ee;
  border: 1px solid rgba(122, 143, 119, 0.15);
  border-radius: 24rpx;
  padding: 20rpx;
  min-width: 420rpx;
  box-shadow: 0 6rpx 18rpx rgba(122, 143, 119, 0.12);
  /* 不要超屏 */
  max-width: 100%;
  box-sizing: border-box;
}
.invite-title {
  font-size: 32rpx;
  font-weight: 700;
  color: #2f3d2f;
  margin-bottom: 8rpx;
}
.invite-meta {
  display: block;
  font-size: 26rpx;
  color: rgba(47, 61, 47, 0.7);
  margin-bottom: 4rpx;
}
.invite-actions {
  display: flex;
  gap: 16rpx;
  margin-top: 16rpx;
}
.invite-btn {
  flex: 1;
  border: none;
  border-radius: 20rpx;
  height: 64rpx;
  font-size: 26rpx;
}
.invite-btn.primary {
  background: #7a8f77;
  color: #fff;
}
.invite-btn.secondary {
  background: rgba(122, 143, 119, 0.12);
  color: #2f3d2f;
}
.invite-status {
  display: block;
  margin-top: 14rpx;
  color: rgba(47, 61, 47, 0.7);
  font-size: 26rpx;
}

/* 底部活动选择器 */
.activity-picker {
  height: 34vh;
  background: #fff;
  border-top: 1px solid rgba(122, 143, 119, 0.12);
  box-shadow: 0 -12rpx 24rpx rgba(0, 0, 0, 0.04);
  display: flex;
  flex-direction: column;
  max-width: 100vw;
  box-sizing: border-box;
}
.picker-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18rpx 22rpx 12rpx;
}
.picker-title {
  font-size: 30rpx;
  font-weight: 700;
  color: #2f3d2f;
}
.picker-close {
  border: none;
  background: rgba(122, 143, 119, 0.1);
  border-radius: 16rpx;
  padding: 8rpx 20rpx;
  font-size: 26rpx;
  color: #2f3d2f;
}
.activity-list {
  flex: 1;
  padding: 0 22rpx 22rpx;
}
.picker-hint {
  text-align: center;
  color: rgba(47, 61, 47, 0.6);
  margin-top: 20rpx;
  font-size: 28rpx;
}
.activity-item {
  background: #f3f7ef;
  border: 1px solid rgba(122, 143, 119, 0.12);
  border-radius: 22rpx;
  padding: 18rpx 16rpx;
  margin-top: 14rpx;
}
.activity-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #2f3d2f;
}
.activity-meta {
  font-size: 26rpx;
  color: rgba(47, 61, 47, 0.6);
  margin-top: 4rpx;
}
</style>

