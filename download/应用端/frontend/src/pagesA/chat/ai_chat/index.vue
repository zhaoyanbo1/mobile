<template>
  <div class="chat-wrap">
    <!-- 顶部栏 -->
    <view class="topbar">
      <button class="back-button" @click="goBack" aria-label="Back">
        <span aria-hidden="true">←</span>
      </button>
      <text class="topbar-title">LiveWell Coach</text>
<!--      <button-->
<!--          class="topbar-action"-->
<!--          @click="newConversation"-->
<!--          aria-label="New"-->
<!--          title="New"-->
<!--      >-->
<!--        <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true">-->
<!--          <path d="M12 5v14M5 12h14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>-->
<!--        </svg>-->
<!--      </button>-->
    </view>

    <!-- 消息区 -->
    <main ref="scrollRef" class="messages" @scroll.passive="handleScroll">
      <div v-if="historyLoading" class="history-loading">Loading history...</div>
      <div v-for="m in messages" :key="m.id" class="message-row" :class="m.role">
        <template v-if="m.role === 'assistant'">
          <img class="avatar assistant-avatar" :src="avatarUrl" alt="assistant" />
          <div class="bubble assistant-bubble" :class="{ 'is-error': m.status === 'ERROR', 'is-streaming': m.streaming }">
            <div class="bubble-text">
              <template v-if="m.text">
                {{ formatAssistantText(m.text) }}
              </template>
              <template v-else>&nbsp;</template>
              <span v-if="m.streaming" class="cursor"></span>
            </div>
            <div v-if="m.status === 'ERROR' && m.errorMessage" class="error-text">{{ m.errorMessage }}</div>
          </div>
        </template>
        <template v-else>
          <img class="avatar user-avatar" :src="userAvatarUrl" alt="user" />
          <div class="bubble user-bubble">
            <div class="bubble-text">{{ m.text }}</div>
          </div>
        </template>
      </div>

      <!-- 正在流式生成时的临时气泡 -->
      <!--      <div v-if="streamingChunk" class="row assistant">-->
      <!--        <img class="avatar" :src="avatarUrl" alt="assistant" />-->
      <!--        <div class="bubble assistant-bubble">{{ streamingChunk }}</div>-->
      <!--      </div>-->
    </main>

    <!-- 底部输入栏 -->
    <footer class="composer">
      <div class="composer-surface">
        <button
            class="icon-button mic-button"
            title="Voice input"
            aria-label="Voice input"
            @click="toggleRecording"
            :class="{ active: isRecording }"
        >
          <svg viewBox="0 0 24 24" width="22" height="22" aria-hidden="true">
            <path d="M12 3a3 3 0 0 0-3 3v6a3 3 0 0 0 6 0V6a3 3 0 0 0-3-3z" fill="none" stroke="currentColor" stroke-width="2"/>
            <path d="M19 11a7 7 0 0 1-14 0M12 19v2" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </button>
        <input
            class="composer-input"
            v-model="input"
            :disabled="loading"
            placeholder="What would you like to talk about?"
            @keydown.enter.exact.prevent="send"
        />
        <button
            class="icon-button send-action"
            :disabled="loading || !input.trim()"
            @click="send"
            title="Send"
            aria-label="Send"
        >
          <svg viewBox="0 0 24 24" width="20" height="20" aria-hidden="true">
            <path d="M4 4l16 8-16 8 4-8-4-8z" fill="currentColor"/>
          </svg>
        </button>
      </div>
    </footer>
    <view v-if="pendingToolCall" class="tool-modal-backdrop" role="dialog" aria-modal="true">
      <view class="tool-modal">
        <button class="modal-close" aria-label="Close" @click="!toolSubmitting && declineToolCall()">×</button>
        <text class="modal-title">Add this to your todo list?</text>
        <view class="modal-summary" v-if="pendingToolCall.sanitizedText">
          <text class="modal-summary-text">{{ pendingToolCall.sanitizedText }}</text>
        </view>
        <view class="modal-row">
          <text class="modal-label">Type</text>
          <text class="modal-value">{{ describeToolType(pendingToolCall.request?.type) }}</text>
        </view>
        <view class="modal-row">
          <text class="modal-label">When</text>
          <text class="modal-value">{{ formatDueAt(pendingToolCall.request?.dueAt) }}</text>
        </view>
        <view class="modal-row" v-if="pendingToolCall.request?.title">
          <text class="modal-label">Title</text>
          <text class="modal-value">{{ pendingToolCall.request.title }}</text>
        </view>
        <view class="modal-row" v-if="pendingToolCall.request?.notes">
          <text class="modal-label">Notes</text>
          <text class="modal-value">{{ pendingToolCall.request.notes }}</text>
        </view>
        <view class="modal-row" v-if="pendingToolCall.request?.dosage">
          <text class="modal-label">Dosage</text>
          <text class="modal-value">{{ pendingToolCall.request.dosage }}</text>
        </view>
        <view class="modal-row">
          <text class="modal-label">Priority</text>
          <text class="modal-value">{{ (pendingToolCall.request?.priority || 'medium').toUpperCase() }}</text>
        </view>
        <text v-if="toolError" class="modal-error">{{ toolError }}</text>
        <view class="modal-actions">
          <button class="modal-btn secondary" :disabled="toolSubmitting" @click="declineToolCall">No thanks</button>
          <button class="modal-btn primary" :disabled="toolSubmitting" @click="confirmToolCall">
            {{ toolSubmitting ? 'Saving…' : 'Add to list' }}
          </button>
        </view>
      </view>
    </view>
  </div>
</template>

<script setup>
// import {ref, nextTick, onBeforeUnmount, getCurrentInstance} from 'vue'
import {ref, nextTick, onBeforeUnmount, getCurrentInstance, onMounted, watch} from 'vue'
import { fetchEventSource } from '@microsoft/fetch-event-source'
import { marked } from 'marked'
import { voiceBus } from '@/voice/bus'

// 头像：随便用一张本地图、CDN、或 emoji 占位
const avatarUrl = 'https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/svg/1f9d1-1f3fb-200d-1f9af.svg'
const userAvatarUrl = 'https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/svg/1f464.svg'

marked.setOptions({
  mangle: false,
  headerIds: false,
  breaks: true,
})

let domParser = null

const ensureDomParser = () => {
  if (typeof window === 'undefined') {
    return null
  }
  if (!domParser) {
    domParser = new DOMParser()
  }
  return domParser
}

const formatAssistantText = (text) => {
  const source = typeof text === 'string' ? text : ''
  if (!source) {
    return ''
  }
  try {
    const html = marked.parse(source)
    const parser = ensureDomParser()
    if (parser) {
      const doc = parser.parseFromString(html, 'text/html')
      return doc.body?.textContent || ''
    }
    return html.replace(/<[^>]*>/g, ' ')
  } catch (error) {
    return source
  }
}

const { proxy } = getCurrentInstance()

const input = ref('')
const messages = ref([
  // 可选：默认欢迎语
  // { role: 'assistant', text: 'Hi! Ask me anything.' }
])
//const streamingChunk = ref('')
const loading = ref(false)
const scrollRef = ref(null)
// let es = null
const conversationId = ref('')
const conversationCache = ref(null)
const CONVERSATION_CACHE_KEY = 'ai_chat_conversation'
const CONVERSATION_MAX_AGE_MS = 12 * 60 * 60 * 1000 // 12 hours
// const HISTORY_LIMIT = 10
// let ctrl = null
const streamingMessage = ref(null)
const historyCursor = ref(null)
const hasMoreHistory = ref(true)
const historyLoading = ref(false)
const userId = ref('')
const MESSAGE_PAGE_SIZE = 20
const API_PREFIX = (import.meta.env.VITE_APP_BASE_API || '').replace(/\/$/, '')

const isRecording = ref(false)
const voiceMode = ref(false)
let recognition = null
let currentUtteranceId = null
let awaitingResponse = false
let ctrl = null

const pendingToolCall = ref(null)
const toolSubmitting = ref(false)
const toolError = ref('')


const createLocalId = (prefix) => `${prefix}-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`

const scrollToBottom = async () => {
  await nextTick()
  const el = scrollRef.value
  if (el) {
    el.scrollTop = el.scrollHeight
  }
}

const makeUrl = (path) => `${API_PREFIX}${path}`

const buildAuthHeaders = () => {
  const headers = {
    'Content-Type': 'application/json',
    Accept: 'text/event-stream',
    APP_ID: import.meta.env.VITE_APP_ID,
    APP_TYPE: import.meta.env.VITE_APP_TYPE,
    X_Env: import.meta.env.VITE_APP_ENV,
  }
  try {
    const token = uni.getStorageSync('h5_token')
    if (token) {
      headers.Authorization = `Bearer ${token}`
    }
  } catch (e) {
    // ignore storage errors
  }
  return headers
}

const mapMessage = (record) => ({
  id: record.messageId ?? createLocalId('server'),
  role: record.role === 'assistant' ? 'assistant' : 'user',
  text: record.content || '',
  status: record.status || 'FINAL',
  streaming: record.status === 'STREAMING',
  errorMessage: record.errorMessage || '',
})

const ensureUser = async () => {
  if (userId.value) {
    return userId.value
  }
  const res = await proxy?.$cf?.login?.getLoginUser?.()
  if (res?.success && res.data?.user_info_id) {
    userId.value = res.data.user_info_id
    return userId.value
  }
  throw new Error(res?.message || '未能获取用户信息')
}

const loadHistory = async ({ reset = false, prepend = false, scroll = false } = {}) => {
  if (!conversationId.value || !userId.value || historyLoading.value) {
    return 0
  }
  if (prepend && !hasMoreHistory.value) {
    return 0
  }
  historyLoading.value = true
  try {
    const res = await proxy?.$cf?.chat?.fetchMessages({
      conversationId: conversationId.value,
      userId: userId.value,
      cursor: prepend ? historyCursor.value : undefined,
      limit: MESSAGE_PAGE_SIZE,
    })
    if (!res?.success) {
      proxy?.$cf?.toast?.({ message: res?.message || '加载消息失败', level: 'error' })
      return 0
    }
    const data = res.data || {}
    const records = Array.isArray(data.records) ? data.records.map(mapMessage) : []
    if (reset) {
      messages.value = records
    } else if (prepend) {
      messages.value = [...records, ...messages.value]
    } else {
      messages.value = [...messages.value, ...records]
    }
    historyCursor.value = data.nextCursor ?? null
    hasMoreHistory.value = Boolean(data.hasMore)
    if (scroll) {
      await scrollToBottom()
    }
    return records.length
  } catch (error) {
    proxy?.$cf?.toast?.({ message: '加载消息失败', level: 'error' })
    return 0
  } finally {
    historyLoading.value = false
  }
}

const handleScroll = async (event) => {
  if (!hasMoreHistory.value || historyLoading.value) {
    return
  }
  const el = event.target
  if (el && el.scrollTop <= 24) {
    const previousHeight = el.scrollHeight
    const previousTop = el.scrollTop
    const loaded = await loadHistory({ prepend: true })
    if (loaded > 0) {
      await nextTick()
      const newHeight = el.scrollHeight
      el.scrollTop = newHeight - previousHeight + previousTop
    }
  }
}

const finalizeStream = ({ errorMessage, cancelled } = {}) => {
  const current = streamingMessage.value
  if (!current) {
    return
  }
  current.streaming = false
  if (errorMessage) {
    current.status = 'ERROR'
    if (!current.text) {
      current.text = ''
    }
    current.errorMessage = errorMessage
    currentUtteranceId = null
  } else if (cancelled) {
    current.status = 'FINAL'
    if (!current.text) {
      const idx = messages.value.indexOf(current)
      if (idx !== -1) {
        messages.value.splice(idx, 1)
      }
    }
    currentUtteranceId = null
  } else {
    current.status = 'FINAL'
    if (voiceMode.value && current.text) {
      currentUtteranceId = Date.now().toString()
      voiceBus.emit('voice:play', currentUtteranceId, current.text)
    } else {
      currentUtteranceId = null
    }
  }
  streamingMessage.value = null
  awaitingResponse = false
}

const stopStream = () => {
  if (ctrl) {
    ctrl.abort()
    ctrl = null
  }
  loading.value = false
}
const readConversationCache = () => {
  if (typeof window === 'undefined') {
    return null
  }
  try {
    const raw = localStorage.getItem(CONVERSATION_CACHE_KEY)
    if (!raw) {
      return null
    }
    const parsed = JSON.parse(raw)
    if (!parsed?.id || !parsed?.createdAt) {
      return null
    }
    const createdAt = new Date(parsed.createdAt)
    if (Number.isNaN(createdAt.getTime())) {
      return null
    }
    if (Date.now() - createdAt.getTime() > CONVERSATION_MAX_AGE_MS) {
      return null
    }
    return { id: parsed.id, createdAt: parsed.createdAt }
  } catch (error) {
    console.warn('Failed to read cached conversation', error)
    return null
  }
}

const isConversationFresh = () => {
  if (!conversationCache.value?.createdAt) {
    return false
  }
  const createdAt = new Date(conversationCache.value.createdAt)
  if (Number.isNaN(createdAt.getTime())) {
    return false
  }
  return Date.now() - createdAt.getTime() <= CONVERSATION_MAX_AGE_MS
}


const ensureConversation = async (titleHint) => {
  if (conversationId.value && isConversationFresh()) {
    return conversationId.value
  }
  if (conversationId.value && !isConversationFresh()) {
    conversationId.value = ''
  }
  const res = await proxy?.$cf?.chat?.createConversation({
    userId: userId.value,
    title: titleHint,
  })
  if (res?.success && res.data?.conversationId) {
    conversationId.value = res.data.conversationId
    return conversationId.value
  }
  throw new Error(res?.message || '创建会话失败')
}

const handleStreamError = (message) => {
  const text = message || '生成失败，请稍后重试'
  proxy?.$cf?.toast?.({ message: text, level: 'error' })
  finalizeStream({ errorMessage: text })
  stopStream()
}

const send = async () => {
  const q = input.value.trim()
  if (!q || loading.value) {
    return
  }
  try {
    await ensureUser()
  } catch (error) {
    proxy?.$cf?.toast?.({ message: error.message || '请先登录后再试', level: 'error' })
    return
  }

  if (voiceMode.value) {
    awaitingResponse = true
  }

  finalizeStream({ cancelled: true })
  stopStream()

  try {
    await ensureConversation(q.slice(0, 40))
  } catch (error) {
    proxy?.$cf?.toast?.({ message: error.message || '创建会话失败', level: 'error' })
    awaitingResponse = false
    return
  }

  const userMessage = {
    id: createLocalId('user'),
    role: 'user',
    text: q,
    status: 'FINAL',
    streaming: false,
    errorMessage: '',
  }
  messages.value.push(userMessage)
  input.value = ''

  const assistantMessage = {
    id: createLocalId('assistant'),
    role: 'assistant',
    text: '',
    status: 'STREAMING',
    streaming: true,
    errorMessage: '',
  }
  messages.value.push(assistantMessage)
  streamingMessage.value = assistantMessage
  loading.value = true
  await scrollToBottom()

  voiceBus.emit('voice:stop', currentUtteranceId)
  currentUtteranceId = null

  const payload = {
    query: q,
    conversationId: conversationId.value,
    userId: userId.value,
  }
  try {
    const tzOptions = typeof Intl !== 'undefined' ? Intl.DateTimeFormat().resolvedOptions() : null
    if (tzOptions?.timeZone) {
      payload.timezone = tzOptions.timeZone
    }
    const offsetMinutes = new Date().getTimezoneOffset()
    if (!Number.isNaN(offsetMinutes)) {
      payload.utcOffsetMinutes = offsetMinutes
    }
  } catch (error) {
    console.warn('Failed to capture timezone info', error)
  }

  ctrl = new AbortController()
  fetchEventSource(makeUrl('/chat/send'), {
    method: 'POST',
    body: JSON.stringify(payload),
    headers: buildAuthHeaders(),
    signal: ctrl.signal,
    onopen(response) {
      if (response.ok) {
        return
      }
      handleStreamError(`请求失败(${response.status})`)
      throw new Error(`HTTP ${response.status}`)
    },
    onmessage(event) {
      if (event.event === 'conversation') {
        conversationId.value = event.data
        return
      }
      if (event.event === 'tool_call') {
        handleToolCallEvent(event.data)
        return
      }
      if (event.event === 'message') {
        if (streamingMessage.value) {
          streamingMessage.value.text += event.data
        }
        scrollToBottom()
        return
      }
      if (event.event === 'done') {
        finalizeStream()
        stopStream()
        scrollToBottom()
        return
      }
      if (event.event === 'error') {
        handleStreamError(event.data)
      }
    },
    onerror(err) {
      handleStreamError(err?.message)
      throw err
    },
  }).catch(() => {
    // errors handled in onerror
  })
}

const newConversation = () => {
  finalizeStream({ cancelled: true })
  stopStream()
  conversationId.value = ''
  conversationCache.value = null
  messages.value = []
  historyCursor.value = null
  hasMoreHistory.value = true
  input.value = ''
  voiceBus.emit('voice:stop', currentUtteranceId)
  currentUtteranceId = null
  closeToolDialog()
}

const goBack = () => {
  history.length > 1 ? history.back() : null
}
const appendAssistantMessage = (text) => {
  if (!text) {
    return
  }
  const message = {
    id: createLocalId('assistant'),
    role: 'assistant',
    text,
    status: 'FINAL',
    streaming: false,
    errorMessage: '',
  }
  messages.value.push(message)
  scrollToBottom()
  if (voiceMode.value) {
    const utteranceId = Date.now().toString()
    voiceBus.emit('voice:play', utteranceId, text)
    currentUtteranceId = utteranceId
  }
}

const handleToolCallEvent = (raw) => {
  if (!raw) {
    return
  }
  let payload = null
  try {
    payload = typeof raw === 'string' ? JSON.parse(raw) : raw
  } catch (error) {
    console.warn('Failed to parse tool call payload', error)
    return
  }
  if (!payload || !payload.actionLogId) {
    return
  }
  const sanitized = typeof payload.sanitizedText === 'string' ? payload.sanitizedText : ''
  if (sanitized) {
    const lastAssistant = [...messages.value].reverse().find((m) => m.role === 'assistant')
    if (lastAssistant) {
      lastAssistant.text = sanitized
      lastAssistant.streaming = false
      lastAssistant.status = 'FINAL'
    }
  }
  pendingToolCall.value = {
    actionLogId: payload.actionLogId,
    conversationId: payload.conversationId || conversationId.value,
    request: payload.request || {},
    sanitizedText: sanitized,
  }
  toolError.value = ''
}

const closeToolDialog = () => {
  pendingToolCall.value = null
  toolError.value = ''
}

const formatDueAt = (value) => {
  if (!value) {
    return 'Any time'
  }
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }
  return date.toLocaleString()
}

const describeToolType = (type) => {
  if (type === 'medication') {
    return 'Medication'
  }
  if (type === 'activity') {
    return 'Activity'
  }
  return type || 'Task'
}

const confirmToolCall = async () => {
  if (!pendingToolCall.value || toolSubmitting.value) {
    return
  }
  toolSubmitting.value = true
  toolError.value = ''
  try {
    const body = {
      conversationId: pendingToolCall.value.conversationId || conversationId.value,
      actionLogId: pendingToolCall.value.actionLogId,
    }
    const res = await proxy?.$cf?.chat?.executeTodoTool?.(body)
    if (!res?.success) {
      throw new Error(res?.message || 'Failed to add reminder')
    }
    const data = res.data || {}
    if (data.assistantMessage) {
      appendAssistantMessage(data.assistantMessage)
    }
    if (data.todo) {
      uni.$emit?.('todo:created', data.todo)
    }
    proxy?.$cf?.toast?.({ message: 'Added to your todo list', level: 'success' })
    closeToolDialog()
  } catch (error) {
    toolError.value = error?.message || 'Failed to add reminder'
  } finally {
    toolSubmitting.value = false
  }
}

const declineToolCall = async () => {
  if (!pendingToolCall.value || toolSubmitting.value) {
    return
  }
  toolSubmitting.value = true
  toolError.value = ''
  try {
    const body = {
      conversationId: pendingToolCall.value.conversationId || conversationId.value,
      actionLogId: pendingToolCall.value.actionLogId,
      reason: 'User declined',
    }
    const res = await proxy?.$cf?.chat?.declineTodoTool?.(body)
    if (!res?.success) {
      throw new Error(res?.message || 'Failed to notify assistant')
    }
    const data = res.data || {}
    if (data.assistantMessage) {
      appendAssistantMessage(data.assistantMessage)
    }
    closeToolDialog()
  } catch (error) {
    toolError.value = error?.message || 'Failed to notify assistant'
  } finally {
    toolSubmitting.value = false
  }
}


voiceBus.on('voice:error', (_id, _code, message) => {
  proxy?.$cf?.toast?.({ message, level: 'error' })
  awaitingResponse = false
  if (voiceMode.value && recognition) {
    recognition.start()
    isRecording.value = true
  }
})

if (typeof window !== 'undefined') {
  const SR = window.SpeechRecognition || window.webkitSpeechRecognition
  if (SR) {
    recognition = new SR()
    recognition.lang = 'zh-CN'
    recognition.interimResults = false
    recognition.continuous = false
    recognition.onresult = (e) => {
      const transcript = e.results[0][0].transcript
      input.value = transcript
      if (awaitingResponse) {
        finalizeStream({ cancelled: true })
        stopStream()
        voiceBus.emit('voice:stop', currentUtteranceId)
        awaitingResponse = false
      }
      send()
    }
    recognition.onerror = () => {
      isRecording.value = false
    }
    recognition.onend = () => {
      //isRecording.value = false
      if (voiceMode.value) {
        recognition.start()
        isRecording.value = true
      } else {
        isRecording.value = false
      }
    }
  }
}

const toggleRecording = () => {
  if (!recognition) {
    proxy?.$cf?.toast?.({ message: '当前浏览器不支持语音识别', level: 'error' })
    return
  }
  voiceBus.emit('voice:stop', currentUtteranceId)
  if (voiceMode.value) {
    recognition.stop()
    voiceMode.value = false
    isRecording.value = false
    awaitingResponse = false
    // if (typeof window !== 'undefined' && window.speechSynthesis) {
    //   window.speechSynthesis.cancel()
    // }
  } else {
    recognition.start()
    voiceMode.value = true
    isRecording.value = true
    awaitingResponse = false
  }
}

// const speak = (text) => {
//   if (!voiceMode.value || typeof window === 'undefined' || !window.speechSynthesis) return
//   const utter = new SpeechSynthesisUtterance(text)
//   utter.lang = 'zh-CN'
//   utter.onend = () => {
//     if (voiceMode.value && recognition) {
//       recognition.start()
//       isRecording.value = true
//     }
//   }
//   window.speechSynthesis.cancel()
//   window.speechSynthesis.speak(utter)
// }


watch(conversationId, (id) => {
  if (typeof window === 'undefined') {
    return
  }
  if (!id) {
    localStorage.removeItem(CONVERSATION_CACHE_KEY)
    conversationCache.value = null
    return
  }
  const existing = conversationCache.value
  const createdAt = existing?.id === id && existing?.createdAt
      ? existing.createdAt
      : new Date().toISOString()
  const payload = { id, createdAt }
  conversationCache.value = payload
  try {
    localStorage.setItem(CONVERSATION_CACHE_KEY, JSON.stringify(payload))
  } catch (error) {
    console.warn('Failed to persist conversation cache', error)
  }
})
onLoad(async (options) => {
  if (typeof window !== 'undefined') {
    localStorage.removeItem('chatMessages')
    localStorage.removeItem('conversationId')
  }
  if (options?.conversationId) {
    conversationId.value = options.conversationId
  } else if (!conversationId.value && typeof window !== 'undefined') {
    const cached = readConversationCache()
    if (cached) {
      conversationCache.value = cached
      conversationId.value = cached.id
    }
  }
  try {
    await ensureUser()
  } catch (error) {
    if (error?.message) {
      proxy?.$cf?.toast?.({ message: error.message, level: 'warning' })
    }
  }
  if (conversationId.value && userId.value) {
    await loadHistory({ reset: true, scroll: true })
  }
})

//onBeforeUnmount(() => stopStream())
onBeforeUnmount(() => {
  finalizeStream({ cancelled: true })
  stopStream()
  voiceBus.emit('voice:stop', currentUtteranceId)
})

voiceBus.on('voice:stopped', (id) => {
  if (voiceMode.value && id === currentUtteranceId) {
    awaitingResponse = false
  }
})

voiceBus.on('voice:ended', (id) => {
  if (voiceMode.value && id === currentUtteranceId) {
    awaitingResponse = false
  }
})
</script>

<style scoped>
:root {
  color-scheme: light;
}

.chat-wrap {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #F8F7F2;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
}

/* 顶部栏 */
.topbar {
  position: sticky;
  top: 0;
  z-index: 5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 18px 20px 12px;
  background: #F8F7F2;
}

.back-button {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: none;
  background: rgba(122, 143, 119, 0.16);
  color: #7A8F77;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  cursor: pointer;
  transition: background 0.2s ease;
}

.back-button:focus-visible,
.topbar-action:focus-visible,
.icon-button:focus-visible {
  outline: 2px solid rgba(122, 143, 119, 0.45);
  outline-offset: 2px;
}

.back-button:active {
  background: rgba(122, 143, 119, 0.28);
}

.topbar-title {
  flex: 1;
  text-align: center;
  font-size: 20px;
  font-weight: 600;
  letter-spacing: 0.4px;
  color: #2F3D2F;
}

.topbar-action {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: none;
  background: rgba(122, 143, 119, 0.16);
  color: #7A8F77;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s ease;
}

.topbar-action:active {
  background: rgba(122, 143, 119, 0.28);
}


/* 消息列表 */
.messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px 20px 160px;
  background: transparent;
}
.history-loading {
  text-align: center;
  color: rgba(49, 65, 49, 0.6);
  font-size: 14px;
  margin: 12px 0;
}


.message-row {
  display: flex;
  gap: 12px;
  margin: 12px 0;
  align-items: flex-start;
}

.message-row.user {
  flex-direction: row-reverse;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #fff;
  border: 2px solid #fff;
  box-shadow: 0 10px 24px rgba(122, 143, 119, 0.15);
  object-fit: cover;
}

.assistant-avatar {
  align-self: flex-start;
}

.user-avatar {
  align-self: flex-start;
}

.bubble {
  max-width: 78%;
  padding: 16px 18px;
  border-radius: 26px;
  word-break: break-word;
  line-height: 1.6;
  font-size: 16px;
  font-weight: 500;
  box-shadow: 0 18px 32px rgba(122, 143, 119, 0.16);
  position: relative;
}

.bubble-text {
  white-space: pre-wrap;
  color: inherit;
  letter-spacing: 0.2px;
}

.assistant-bubble {
  background: #E6EFE1;
  color: #314131;
  border-top-left-radius: 14px;
}

.assistant-bubble.is-streaming {
  box-shadow: 0 20px 40px rgba(122, 143, 119, 0.22);
}

.assistant-bubble.is-error {
  background: #F6E5E3;
  color: #7F3C36;
}

.assistant-bubble .cursor {
  display: inline-block;
  width: 3px;
  height: 1.2em;
  background: #7A8F77;
  margin-left: 6px;
  animation: blink 1s steps(2, start) infinite;
  vertical-align: bottom;
}

.assistant-bubble .error-text {
  margin-top: 10px;
  font-size: 13px;
  color: rgba(127, 60, 54, 0.85);
}

.user-bubble {
  background: #DDE4D9;
  color: #2F3D2F;
  border-top-right-radius: 14px;
}

@keyframes blink {
  0%, 49% {
    opacity: 1;
  }
  50%, 100% {
    opacity: 0;
  }
}



.composer {
  position: sticky;
  bottom: 0;
  padding: 0 20px 28px;
  background: linear-gradient(180deg, rgba(248, 247, 242, 0), #F8F7F2 45%, #F8F7F2 100%);
  backdrop-filter: blur(8px);
}

.composer-surface {
  display: flex;
  align-items: center;
  gap: 14px;
  background: #ffffff;
  border-radius: 32px;
  padding: 14px 18px;
  box-shadow: 0 22px 46px rgba(122, 143, 119, 0.22);
}

.composer-input {
  flex: 1;
  border: none;
  background: transparent;
  font-size: 16px;
  color: #314131;
  line-height: 1.6;
  min-height: 24px;
  outline: none;
}

.composer-input:disabled {
  color: rgba(49, 65, 49, 0.4);
}

.composer-input::placeholder {
  color: rgba(49, 65, 49, 0.45);
}

.icon-button {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  background: rgba(122, 143, 119, 0.12);
  color: #7A8F77;
  transition: background 0.2s ease, box-shadow 0.2s ease;
}

.icon-button svg {
  pointer-events: none;
}

.icon-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.icon-button:active {
  background: rgba(122, 143, 119, 0.24);
}

.icon-button.active {
  background: rgba(122, 143, 119, 0.28);
}

.mic-button {
  background: rgba(122, 143, 119, 0.12);
}

.send-action {
  background: #7A8F77;
  color: #fff;
  box-shadow: 0 12px 24px rgba(122, 143, 119, 0.4);
}

.send-action:disabled {
  background: rgba(122, 143, 119, 0.5);
  box-shadow: none;
}
.tool-modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(47, 61, 47, 0.32);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx;
  z-index: 40;
}

.tool-modal {
  position: relative;
  width: 100%;
  max-width: 660rpx;
  background: #F8F7F2;
  border-radius: 32rpx;
  padding: 48rpx 36rpx;
  box-shadow: 0 26rpx 56rpx rgba(122, 143, 119, 0.28);
  border: 1px solid rgba(122, 143, 119, 0.18);
  display: flex;
  flex-direction: column;
  gap: 28rpx;
  color: #2F3D2F;
}

.modal-close {
  position: absolute;
  top: 24rpx;
  right: 24rpx;
  border: none;
  background: rgba(122, 143, 119, 0.1);
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  font-size: 34rpx;
  color: #7A8F77;
  cursor: pointer;
  line-height: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s ease, color 0.2s ease;
}

.modal-close:active,
.modal-close:focus-visible {
  background: rgba(122, 143, 119, 0.24);
  color: #2F3D2F;
}

.modal-title {
  font-size: 40rpx;
  font-weight: 700;
  color: #2F3D2F;
  text-align: center;
}

.modal-summary {
  background: rgba(230, 239, 225, 0.65);
  border-radius: 24rpx;
  padding: 24rpx;
  border: 1px solid rgba(122, 143, 119, 0.2);
}

.modal-summary-text {
  font-size: 30rpx;
  color: #314131;
  line-height: 1.6;
}

.modal-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 24rpx;
}

.modal-label {
  font-size: 28rpx;
  color: rgba(49, 65, 49, 0.7);
  font-weight: 600;
  min-width: 120rpx;
}

.modal-value {
  font-size: 30rpx;
  color: #2F3D2F;
  text-align: right;
  flex: 1;
  line-height: 1.5;
}

.modal-error {
  color: #B03A3A;
  font-size: 28rpx;
  text-align: center;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 24rpx;
}

.modal-btn {
  border: none;
  border-radius: 999rpx;
  padding: 22rpx 40rpx;
  font-size: 30rpx;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease, background 0.2s ease, color 0.2s ease;
}

.modal-btn.primary {
  background: #7A8F77;
  color: #fff;
  box-shadow: 0 16rpx 28rpx rgba(122, 143, 119, 0.35);
}

.modal-btn.primary:active {
  transform: translateY(2rpx);
  box-shadow: 0 10rpx 20rpx rgba(122, 143, 119, 0.3);
}

.modal-btn.secondary {
  background: rgba(122, 143, 119, 0.14);
  color: #2F3D2F;
}

.modal-btn.secondary:active {
  background: rgba(122, 143, 119, 0.24);
}

.modal-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  box-shadow: none;
}
</style>
<script setup>
</script>