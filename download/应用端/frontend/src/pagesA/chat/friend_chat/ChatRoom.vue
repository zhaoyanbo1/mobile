<template>
  <view class="chat-page">
    <!-- 顶部标题（对方昵称） -->
    <view class="chat-header">
      <text class="title">{{ peerName || 'chat with my friend' }}</text>
    </view>

    <!-- 消息列表 -->
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
        <view class="bubble" :class="bubbleClass(m)">
          <template v-if="m.contentType === 'TEXT'">
            {{ m.content }}
          </template>
          <template v-else-if="m.contentType === 'ACTIVITY_INVITE'">
            <view class="invite-card">
              <text class="invite-title">{{ m.content?.title || '活动邀请' }}</text>
              <text class="invite-meta" v-if="m.content?.time">时间：{{ m.content.time }}</text>
              <text class="invite-meta" v-if="m.content?.location">地点：{{ m.content.location }}</text>
              <view class="invite-meta" v-if="m.content?.maxParticipants">
                <text>人数：{{ m.content.participantsCount || 0 }}/{{ m.content.maxParticipants }}</text>
              </view>
              <view class="invite-actions" v-if="shouldShowInviteActions(m)">
                <button class="btn-accept" :disabled="m._processing" @click="respondToInvite(m, 'ACCEPTED')">接受</button>
                <button class="btn-decline" :disabled="m._processing" @click="respondToInvite(m, 'DECLINED')">拒绝</button>
              </view>
              <text class="invite-status" v-else>{{ inviteStatusText(m) }}</text>
            </view>
          </template>
          <template v-else-if="m.contentType === 'ACTIVITY_INVITE_RESPONSE'">
            <text>{{ inviteResponseText(m) }}</text>
          </template>
          <template v-else>
            {{ typeof m.content === 'string' ? m.content : JSON.stringify(m.content) }}
          </template>
        </view>
      </view>
      <!-- 占位，保证滚动到底 -->
      <view style="height: 10px;"></view>
    </scroll-view>

    <!-- 底部输入栏 -->
    <view class="input-bar">
      <button class="btn-activity" @click="toggleActivityPicker">
        {{ showActivityPicker ? '收起活动' : '发活动' }}
      </button>
      <input
          class="ipt"
          v-model="inputText"
          confirm-type="send"
          @confirm="sendTextMessage"
          placeholder="输入消息…"
      />
      <button class="btn-send" @click="sendTextMessage">发送</button>
    </view>

    <view v-if="showActivityPicker" class="activity-picker">
      <view class="picker-header">
        <text class="picker-title">我的活动</text>
        <button class="btn-close" @click="toggleActivityPicker(false)">关闭</button>
      </view>
      <scroll-view class="activity-list" scroll-y>
        <view v-if="activitiesLoading" class="picker-hint">加载中…</view>
        <view v-else-if="activitiesError" class="picker-hint">{{ activitiesError }}</view>
        <view v-else-if="!myActivities.length" class="picker-hint">暂无活动，前往创建吧</view>
        <view
            v-for="activity in myActivities"
            :key="activity.id"
            class="activity-item"
            @click="sendActivityInvite(activity)"
        >
          <text class="activity-title">{{ activity.title }}</text>
          <text class="activity-meta" v-if="activity.time">时间：{{ activity.time }}</text>
          <text class="activity-meta" v-if="activity.location">地点：{{ activity.location }}</text>
          <text class="activity-meta">人数：{{ activity.participantsCount }}/{{ activity.maxParticipants || '不限' }}</text>
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

/** 当前登录用户 ID，用于左右判断 */
const myId = ref(Number(uni.getStorageSync('uid')) || 0)
const myName = ref(uni.getStorageSync('username') || '')

/** 页面状态 */
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

/** 进入页面：拿到 query 参数并加载历史 */
onLoad(async (q) => {
  if (q?.peerId) peerId.value = Number(q.peerId)
  if (q?.nickname) peerName.value = decodeURIComponent(q.nickname)

  // 如果通过聊天列表跳转时已经带了会话ID，直接用；否则确保会话存在
  if (q?.conversationId) {
    conversationId.value = Number(q.conversationId)
  } else if (peerId.value) {
    // 确保/创建 1v1 会话（若你的 im.js 方法名不同，请改成你的）
    const r = await imApi.ensureDm(peerId.value)
    conversationId.value = r?.data ?? r ?? null
  }

  syncConversationMapping()

  await loadMessages()
  scrollToBottom(true)
})
function parseContent(contentType, rawContent) {
  if (contentType === 'ACTIVITY_INVITE' || contentType === 'ACTIVITY_INVITE_RESPONSE') {
    if (rawContent && typeof rawContent === 'object') {
      return { ...rawContent }
    }
    try {
      return JSON.parse(rawContent)
    } catch (err) {
      return {}
    }
  }
  if (typeof rawContent === 'string') return rawContent
  if (rawContent == null) return ''
  return String(rawContent)
}

/** 统一消息结构 */
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
    createdAtEpochMs: m.createdAtEpochMs ?? Date.now()
  }
}
/** 根据回复重新整理邀请状态 */
function recomputeInviteStatuses() {
  const statusMap = new Map()
  messages.value.forEach(msg => {
    if (msg.contentType === 'ACTIVITY_INVITE_RESPONSE') {
      const data = msg.content || {}
      if (data.inviteId) {
        statusMap.set(data.inviteId, (data.status || 'PENDING').toUpperCase())
      }
    }
  })

  messages.value = messages.value.map(msg => {
    if (msg.contentType !== 'ACTIVITY_INVITE') return msg
    const invite = msg.content && typeof msg.content === 'object' ? { ...msg.content } : {}
    const status = statusMap.get(invite.inviteId) || (invite.status || 'PENDING')
    invite.status = status.toUpperCase()
    return { ...msg, content: invite }
  })
}

/** 滚动到底部 */
function scrollToBottom(immediate = false) {
  nextTick(() => {
    if (immediate) {
      scrollTop.value = 999999
    } else {
      // 触发一点点差值让 scroll-view 动画更自然
      scrollTop.value = scrollTop.value + 1
    }
  })
}

/** 拉取历史 */
// async function loadMessages() {
//   if (!conversationId.value) return
//   const res = await imApi.getMessages(conversationId.value, null, 20)
//   // 兼容返回结构：可能是 {data:{list:[]}} 或 {list:[]} 或直接 []
//   const list = (res?.data?.list ?? res?.list ?? res ?? []).map(normalizeMsg)
//   list.sort((a, b) => a.createdAtEpochMs - b.createdAtEpochMs)
//
//   // messages.value = [...list, ...messages.value]
//
//   messages.value = list
//
//   scrollToBottom(true)
// }
/** 拉取历史 */
async function loadMessages() {
  if (!conversationId.value) return
  const res = await imApi.getMessages(conversationId.value, null, 20)
  const list = (res?.data?.list ?? res?.list ?? res ?? []).map(normalizeMsg)

  // 排序（旧 → 新）
  messages.value = list
  sortMessages()

  // 加载后自动滚动到底部
  recomputeInviteStatuses()
  scrollToBottom(true)

  const latest = messages.value.length ? messages.value[messages.value.length - 1] : null
  syncConversationMapping()
  markMessagesAsRead(latest?.messageId)
}
/** 加载我创建的活动 */
async function loadMyActivities() {
  if (!myId.value) return
  activitiesLoading.value = true
  activitiesError.value = ''
  try {
    const res = await activitiesApi.manage({ userId: myId.value })
    const list = Array.isArray(res?.data) ? res.data : Array.isArray(res) ? res : []
    myActivities.value = list.map(mapActivity)
  } catch (err) {
    console.warn('加载活动失败', err)
    activitiesError.value = err?.message || '加载活动失败'
    myActivities.value = []
  } finally {
    activitiesLoading.value = false
  }
}

function mapActivity(raw) {
  if (!raw) return {
    id: null,
    title: '',
    time: '',
    timeIso: '',
    location: '',
    minParticipants: 0,
    maxParticipants: 0,
    participantsCount: 0
  }
  const timeDisplay = raw.activityTime || raw.time || ''
  const timeIso = raw.activityTimeIso || raw.timeIso || ''
  return {
    id: raw.id,
    title: raw.title || '未命名活动',
    time: timeDisplay,
    timeIso,
    location: raw.location || '',
    minParticipants: raw.minParticipants ?? 0,
    maxParticipants: raw.maxParticipants ?? 0,
    participantsCount: raw.participantsCount ?? (raw.participants?.length || 0)
  }
}
/** 发送文本消息：先本地追加“临时消息”，成功后用服务端返回覆盖 */
function toggleActivityPicker(force) {
  const next = typeof force === 'boolean' ? force : !showActivityPicker.value
  showActivityPicker.value = next
  if (next && !myActivities.value.length && !activitiesLoading.value) {
    loadMyActivities()
  }
}

function buildInviteId() {
  return `invite-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`
}

async function sendTextMessage() {
  const text = inputText.value.trim()
  if (!text) return
  inputText.value = ''
  try {
    await sendMessagePayload({ contentType: 'TEXT', content: text })
  } catch (err) {
    console.warn('发送消息失败', err)
  }
}

async function sendMessagePayload({ contentType, content }) {
  if (!conversationId.value && !peerId.value) {
    uni.showToast({ title: '缺少会话信息', icon: 'none' })
    throw new Error('missing conversation info')
  }

  inputText.value = ''

  // 1) 本地先追加一条临时消息（右侧）
  const tempId = Date.now()
  const now = Date.now()
  const isStructured = contentType === 'ACTIVITY_INVITE' || contentType === 'ACTIVITY_INVITE_RESPONSE'
  const rawContent = typeof content === 'string' ? content : JSON.stringify(content ?? {})
  const localContent = isStructured && content && typeof content === 'object'
      ? { ...content }
      : (typeof content === 'string' ? content : rawContent)

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
    _pending: true
  }
  messages.value.push(tempMsg)
  sortMessages()
  recomputeInviteStatuses()
  scrollToBottom()

  // 2) 调用后端发送
  try {
    const resp = await imApi.sendMessage({
      conversationId: conversationId.value, // 若为空，服务端会使用 peerId 创建会话（取决于你的实现）
      peerId: peerId.value,
      contentType,
      content: rawContent
    })

    // 3) 用服务端返回的值覆盖临时消息（避免重复）
    const saved = normalizeMsg(resp?.data ?? resp)
    // 如果后端可能返回新的 conversationId，这里接收并更新
    if (!conversationId.value && saved.conversationId) {
      conversationId.value = saved.conversationId
    }

    const idx = messages.value.findIndex(m => m.messageId === tempId)
    if (idx > -1) {
      messages.value[idx] = saved
    } else {
      messages.value.push(saved)
    }

    sortMessages()
    recomputeInviteStatuses()
    scrollToBottom()
    syncConversationMapping()
    markMessagesAsRead(saved?.messageId)
    return saved
  } catch (error) {
    messages.value = messages.value.filter(m => m.messageId !== tempId)
    uni.showToast({ title: '发送失败', icon: 'none' })
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
    return message.senderId === myId.value ? '对方已接受邀请' : '你已加入该活动'
  }
  if (status === 'DECLINED') {
    return message.senderId === myId.value ? '对方已拒绝邀请' : '你已拒绝邀请'
  }
  return message.senderId === myId.value ? '等待对方回应' : '请选择是否加入'
}

function inviteResponseText(message) {
  const data = message?.content || {}
  const status = (data.status || '').toUpperCase()
  const responderName = data.responderName || (message.senderId === myId.value ? '你' : (peerName.value || '对方'))
  const title = data.title ? `「${data.title}」` : '活动'
  if (status === 'ACCEPTED') {
    return `${responderName}已加入${title}`
  }
  if (status === 'DECLINED') {
    return `${responderName}拒绝加入${title}`
  }
  return `${responderName}回复了${title}`
}

function bubbleClass(message) {
  if (message.contentType === 'ACTIVITY_INVITE') {
    return 'bubble-card'
  }
  return ''
}

async function sendActivityInvite(activity) {
  if (!activity) return
  try {
    const inviteId = buildInviteId()
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
      status: 'PENDING'
    }
    await sendMessagePayload({ contentType: 'ACTIVITY_INVITE', content: payload })
    uni.showToast({ title: '已发送邀请', icon: 'success' })
    toggleActivityPicker(false)
  } catch (err) {
    console.warn('发送活动邀请失败', err)
  }
}

async function respondToInvite(message, decision) {
  if (!message || message.contentType !== 'ACTIVITY_INVITE') return
  if (message._processing) return
  const invite = message.content || {}
  if (!invite.activityId) {
    uni.showToast({ title: '活动信息缺失', icon: 'none' })
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
      responderName: myName.value || ''
    }
    await sendMessagePayload({ contentType: 'ACTIVITY_INVITE_RESPONSE', content: responsePayload })
    message.content = { ...invite, status: decision }
    recomputeInviteStatuses()
    if (decision === 'ACCEPTED') {
      uni.showToast({ title: '已加入活动', icon: 'success' })
    } else {
      uni.showToast({ title: '已拒绝邀请', icon: 'none' })
    }
  } catch (err) {
    console.warn('处理邀请失败', err)
    uni.showToast({ title: '操作失败', icon: 'none' })
  } finally {
    message._processing = false
  }
}

/** 按时间排序：旧消息在上，新消息在下 */
function sortMessages() {
  messages.value.sort((a, b) => {
    const ta = a.createdAtEpochMs ?? 0
    const tb = b.createdAtEpochMs ?? 0
    if (ta !== tb) return ta - tb
    const ia = Number(a.messageId ?? 0)
    const ib = Number(b.messageId ?? 0)
    return ia - ib
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
    imApi.markRead(conversationId.value, latestMessageId).catch(err => {
      console.warn('标记已读失败', err)
    })
  }
}



</script>

<style scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f7f8fb;
}

/* 顶部标题 */
.chat-header {
  height: 48px;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
}
.title {
  font-size: 18px;
  font-weight: 700;
  color: #0b0b0c;
}

/* 消息区域 */
.chat-body {
  flex: 1;
  padding: 10px 8px;
}

.msg-item {
  display: flex;
  margin: 8px 6px;
}

.msg-item.me {
  justify-content: flex-end;
}
.msg-item.other {
  justify-content: flex-start;
}

.bubble {
  max-width: 70%;
  padding: 8px 12px;
  border-radius: 12px;
  font-size: 15px;
  line-height: 1.4;
  word-break: break-word;
  background: #fff;
  color: #111;
  border-bottom-left-radius: 2px; /* 左侧小尖角 */
  box-shadow: 0 1px 2px rgba(0,0,0,.03);
}

.msg-item.me .bubble {
  background: #4f8cff;
  color: #fff;
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 2px; /* 右侧小尖角 */
}

/* 底部输入栏 */
.input-bar {
  padding: 8px;
  display: flex;
  gap: 8px;
  background: #fff;
  border-top: 1px solid #eee;
}
.input-bar .ipt {
  flex: 1;
  height: 36px;
  padding: 0 10px;
  border-radius: 8px;
  background: #fff;
  border: 1px solid #eaeaea;
  font-size: 14px;
}
.input-bar .btn-send {
  height: 36px;
  padding: 0 14px;
  border-radius: 8px;
  background: #4f8cff;
  color: #fff;
  font-size: 14px;
}
.input-bar .btn-activity {
  height: 36px;
  padding: 0 12px;
  border-radius: 8px;
  background: #f0f4ff;
  color: #2f54eb;
  font-size: 14px;
  border: 1px solid #adc6ff;
}

.bubble-card {
  padding: 0;
  background: transparent;
  box-shadow: none;
}

.msg-item.me .bubble-card {
  background: transparent;
}

.invite-card {
  min-width: 220px;
  background: #fff;
  border-radius: 12px;
  border: 1px solid #e6f0ff;
  padding: 12px;
  box-shadow: 0 4px 12px rgba(79, 140, 255, 0.12);
  color: #1f1f1f;
}

.invite-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 6px;
  display: block;
}

.invite-meta {
  display: block;
  font-size: 13px;
  color: #5b6b8b;
  margin-bottom: 4px;
}

.invite-actions {
  display: flex;
  gap: 8px;
  margin-top: 10px;
}

.invite-actions button {
  flex: 1;
  height: 32px;
  border-radius: 6px;
  font-size: 14px;
}

.btn-accept {
  background: #52c41a;
  color: #fff;
}

.btn-decline {
  background: #f5f5f5;
  color: #8c8c8c;
}

.invite-status {
  display: block;
  margin-top: 10px;
  font-size: 13px;
  color: #8c8c8c;
}

.activity-picker {
  height: 33vh;
  background: #fff;
  border-top: 1px solid #e6eaf2;
  box-shadow: 0 -4px 10px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
}

.picker-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
}

.picker-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f1f1f;
}

.btn-close {
  padding: 4px 10px;
  border-radius: 6px;
  background: #f5f5f5;
  font-size: 13px;
  color: #595959;
}

.activity-list {
  flex: 1;
  padding: 0 16px 16px;
}

.picker-hint {
  color: #8c8c8c;
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}

.activity-item {
  background: linear-gradient(135deg, rgba(79, 140, 255, 0.12), rgba(144, 202, 249, 0.12));
  border-radius: 12px;
  padding: 12px;
  margin-top: 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.activity-item:first-of-type {
  margin-top: 0;
}

.activity-title {
  font-size: 15px;
  font-weight: 600;
  color: #1f1f1f;
}

.activity-meta {
  font-size: 13px;
  color: #5b6b8b;
}
</style>
