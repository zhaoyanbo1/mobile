<template>
  <view class="chat-page">
    <!-- 顶部栏 -->
    <view class="topbar">
      <view class="back-wrap" @click="goBack">
        <text class="back-icon"><</text>
      </view>
      <text class="title">LiveWell Coach</text>
      <view class="right-spacer"></view>
    </view>


    <!-- 聊天主体 -->
    <view class="chat-body">
      <main ref="scrollRef" class="messages" @scroll="handleScroll">
        <view v-if="historyLoading" class="history-loading">Loading history...</view>

        <view
            v-for="m in messages"
            :key="m.id"
            class="message-row"
            :class="m.role"
        >
          <!-- 助手消息 -->
          <template v-if="m.role === 'assistant'">
            <image class="avatar assistant-avatar" :src="avatarUrl" />
            <view
                class="bubble assistant-bubble"
                :class="{ 'is-error': m.status === 'ERROR', 'is-streaming': m.streaming }"
            >
              <view class="bubble-text">
                <template v-if="m.text">
                  {{ formatAssistantText(m.text) }}
                </template>
                <template v-else>&nbsp;</template>
                <text v-if="m.streaming" class="cursor"></text>
              </view>
              <view v-if="m.status === 'ERROR' && m.errorMessage" class="error-text">
                {{ m.errorMessage }}
              </view>
            </view>
          </template>

          <!-- 用户消息（不显示头像） -->
          <template v-else>
            <view class="bubble user-bubble">
              <view class="bubble-text">{{ m.text }}</view>
            </view>
          </template>
        </view>
      </main>

      <!-- 回到底部按钮 -->
      <button
          v-if="showScrollToBottom"
          class="scroll-to-bottom"
          type="button"
          @click="jumpToLatest"
          aria-label="Back to new"
      >
        ↓
      </button>
    </view>

    <!-- 底部输入条 -->
    <view class="composer">
      <view class="composer-surface">
        <button
            class="icon-button mic-button"
            @click="toggleRecording"
            :class="{ active: isRecording }"
            aria-label="Voice input"
        >
          🎤
        </button>
        <input
            class="composer-input"
            v-model="input"
            :disabled="loading"
            placeholder="What would you like to talk about?"
            @keydown.enter.exact.prevent="send"
        />
        <button
            class="icon-button send-button"
            :disabled="loading || !input.trim()"
            @click="send"
            aria-label="Send"
        >
          ➤
        </button>
      </view>
    </view>

    <!-- tool 弹窗 -->
    <view
        v-if="pendingToolCall"
        class="tool-modal-backdrop"
        role="dialog"
        aria-modal="true"
    >
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
          <button class="modal-btn secondary" :disabled="toolSubmitting" @click="declineToolCall">
            No thanks
          </button>
          <button class="modal-btn primary" :disabled="toolSubmitting" @click="confirmToolCall">
            {{ toolSubmitting ? 'Saving…' : 'Add to list' }}
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, nextTick, onBeforeUnmount, getCurrentInstance, onMounted, watch } from 'vue'
import { fetchEventSource } from '@microsoft/fetch-event-source'
import { marked } from 'marked'
import { voiceBus } from '@/voice/bus'

const avatarUrl =
    'https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/svg/1f9d1-1f3fb-200d-1f9af.svg'

marked.setOptions({
  mangle: false,
  headerIds: false,
  breaks: true,
})

let domParser = null
const ensureDomParser = () => {
  if (typeof window === 'undefined') return null
  if (!domParser) domParser = new DOMParser()
  return domParser
}

const formatAssistantText = (text) => {
  const source = typeof text === 'string' ? text : ''
  if (!source) return ''
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
const messages = ref([])
const loading = ref(false)
const scrollRef = ref(null)
const showScrollToBottom = ref(false)

const conversationId = ref('')
const conversationCache = ref(null)
const CONVERSATION_CACHE_KEY = 'ai_chat_conversation'
const CONVERSATION_MAX_AGE_MS = 12 * 60 * 60 * 1000

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

const createLocalId = (prefix) =>
    `${prefix}-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`

let windowScrollHandler = null

const updateScrollAffordance = () => {
  let distance = 0
  const el = scrollRef.value
  if (el) {
    const maxScrollTop = Math.max(0, el.scrollHeight - el.clientHeight)
    distance = Math.max(distance, maxScrollTop - el.scrollTop)
  }
  if (typeof window !== 'undefined') {
    const doc = document.scrollingElement || document.documentElement || document.body
    if (doc) {
      const winScrollTop = window.scrollY ?? doc.scrollTop ?? 0
      const winDistance = Math.max(0, doc.scrollHeight - window.innerHeight - winScrollTop)
      distance = Math.max(distance, winDistance)
    }
  }
  showScrollToBottom.value = distance > 32
}

const scrollToBottom = async () => {
  await nextTick()
  const el = scrollRef.value
  if (el) {
    el.scrollTop = el.scrollHeight
    showScrollToBottom.value = false
  }
}
const jumpToLatest = async () => {
  await scrollToBottom()
  showScrollToBottom.value = false
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
    if (token) headers.Authorization = `Bearer ${token}`
  } catch (e) {}
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
  if (userId.value) return userId.value
  const res = await proxy?.$cf?.login?.getLoginUser?.()
  if (res?.success && res.data?.user_info_id) {
    userId.value = res.data.user_info_id
    return userId.value
  }
  throw new Error(res?.message || '未能获取用户信息')
}

const restoreLatestConversationId = async () => {
  if (!proxy?.$cf?.chat?.listConversations || !userId.value) return ''
  try {
    const res = await proxy.$cf.chat.listConversations({
      userId: userId.value,
      page: 1,
      size: 1,
      status: 'ACTIVE',
    })
    if (!res?.success) return ''
    const records = Array.isArray(res.data?.records) ? res.data.records : []
    const latest = records.find((item) => item?.conversationId)
    if (!latest) return ''
    if (conversationId.value === latest.conversationId) return conversationId.value
    conversationCache.value = {
      id: latest.conversationId,
      createdAt: new Date().toISOString(),
    }
    conversationId.value = latest.conversationId
    return conversationId.value
  } catch (error) {
    console.warn('Failed to restore latest conversation', error)
    return ''
  }
}

const loadHistory = async ({ reset = false, prepend = false, scroll = false } = {}) => {
  if (!conversationId.value || !userId.value || historyLoading.value) return 0
  if (prepend && !hasMoreHistory.value) return 0
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
    } else {
      await nextTick()
      updateScrollAffordance()
    }
    return records.length
  } catch (error) {
    proxy?.$cf?.toast?.({ message: '加载消息失败', level: 'error' })
    return 0
  } finally {
    historyLoading.value = false
  }
}

const loadCompleteHistory = async () => {
  if (!conversationId.value || !userId.value) {
    messages.value = []
    historyCursor.value = null
    hasMoreHistory.value = false
    return 0
  }
  if (historyLoading.value) return 0
  messages.value = []
  historyCursor.value = null
  hasMoreHistory.value = true
  let total = 0
  let firstBatch = true
  while (hasMoreHistory.value) {
    const loaded = await loadHistory({ reset: firstBatch, prepend: !firstBatch })
    if (loaded <= 0) break
    total += loaded
    firstBatch = false
  }
  if (total > 0) await scrollToBottom()
  return total
}

const handleScroll = async (event) => {
  const el = event?.target
  if (el && hasMoreHistory.value && !historyLoading.value && el.scrollTop <= 24) {
    const previousHeight = el.scrollHeight
    const previousTop = el.scrollTop
    const loaded = await loadHistory({ prepend: true })
    if (loaded > 0) {
      await nextTick()
      const newHeight = el.scrollHeight
      el.scrollTop = newHeight - previousHeight + previousTop
    }
  }
  updateScrollAffordance()
}

const finalizeStream = ({ errorMessage, cancelled } = {}) => {
  const current = streamingMessage.value
  if (!current) return
  current.streaming = false
  if (errorMessage) {
    current.status = 'ERROR'
    if (!current.text) current.text = ''
    current.errorMessage = errorMessage
    currentUtteranceId = null
  } else if (cancelled) {
    current.status = 'FINAL'
    if (!current.text) {
      const idx = messages.value.indexOf(current)
      if (idx !== -1) messages.value.splice(idx, 1)
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
  if (typeof window === 'undefined') return null
  try {
    const raw = localStorage.getItem(CONVERSATION_CACHE_KEY)
    if (!raw) return null
    const parsed = JSON.parse(raw)
    if (!parsed?.id || !parsed?.createdAt) return null
    const createdAt = new Date(parsed.createdAt)
    if (Number.isNaN(createdAt.getTime())) return null
    if (Date.now() - createdAt.getTime() > CONVERSATION_MAX_AGE_MS) return null
    return { id: parsed.id, createdAt: parsed.createdAt }
  } catch (error) {
    console.warn('Failed to read cached conversation', error)
    return null
  }
}

const isConversationFresh = () => {
  if (!conversationCache.value?.createdAt) return false
  const createdAt = new Date(conversationCache.value.createdAt)
  if (Number.isNaN(createdAt.getTime())) return false
  return Date.now() - createdAt.getTime() <= CONVERSATION_MAX_AGE_MS
}

const ensureConversation = async (titleHint) => {
  if (conversationId.value && isConversationFresh()) return conversationId.value
  if (!conversationId.value) {
    const restored = await restoreLatestConversationId()
    if (restored) return restored
  } else if (!isConversationFresh()) {
    const previousId = conversationId.value
    conversationId.value = ''
    const restored = await restoreLatestConversationId()
    if (restored) return restored
    conversationId.value = previousId
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
  if (!q || loading.value) return

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
    if (tzOptions?.timeZone) payload.timezone = tzOptions.timeZone
    const offsetMinutes = new Date().getTimezoneOffset()
    if (!Number.isNaN(offsetMinutes)) payload.utcOffsetMinutes = offsetMinutes
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
      if (response.ok) return
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
  }).catch(() => {})
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
  if (!text) return
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
  if (!raw) return
  let payload = null
  try {
    payload = typeof raw === 'string' ? JSON.parse(raw) : raw
  } catch (error) {
    console.warn('Failed to parse tool call payload', error)
    return
  }
  if (!payload || !payload.actionLogId) return
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
  if (!value) return 'Any time'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value
  return date.toLocaleString()
}

const describeToolType = (type) => {
  if (type === 'medication') return 'Medication'
  if (type === 'activity') return 'Activity'
  return type || 'Task'
}

const confirmToolCall = async () => {
  if (!pendingToolCall.value || toolSubmitting.value) return
  toolSubmitting.value = true
  toolError.value = ''
  try {
    const body = {
      conversationId: pendingToolCall.value.conversationId || conversationId.value,
      actionLogId: pendingToolCall.value.actionLogId,
    }
    const res = await proxy?.$cf?.chat?.executeTodoTool?.(body)
    if (!res?.success) throw new Error(res?.message || 'Failed to add reminder')
    const data = res.data || {}
    if (data.assistantMessage) appendAssistantMessage(data.assistantMessage)
    if (data.todo) uni.$emit?.('todo:created', data.todo)
    proxy?.$cf?.toast?.({ message: 'Added to your todo list', level: 'success' })
    closeToolDialog()
  } catch (error) {
    toolError.value = error?.message || 'Failed to add reminder'
  } finally {
    toolSubmitting.value = false
  }
}

const declineToolCall = async () => {
  if (!pendingToolCall.value || toolSubmitting.value) return
  toolSubmitting.value = true
  toolError.value = ''
  try {
    const body = {
      conversationId: pendingToolCall.value.conversationId || conversationId.value,
      actionLogId: pendingToolCall.value.actionLogId,
      reason: 'User declined',
    }
    const res = await proxy?.$cf?.chat?.declineTodoTool?.(body)
    if (!res?.success) throw new Error(res?.message || 'Failed to notify assistant')
    const data = res.data || {}
    if (data.assistantMessage) appendAssistantMessage(data.assistantMessage)
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
  } else {
    recognition.start()
    voiceMode.value = true
    isRecording.value = true
    awaitingResponse = false
  }
}

watch(conversationId, (id) => {
  if (typeof window === 'undefined') return
  if (!id) {
    localStorage.removeItem(CONVERSATION_CACHE_KEY)
    conversationCache.value = null
    return
  }
  const existing = conversationCache.value
  const createdAt =
      existing?.id === id && existing?.createdAt ? existing.createdAt : new Date().toISOString()
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
  let hasUser = false
  try {
    await ensureUser()
    hasUser = true
  } catch (error) {
    if (error?.message) {
      proxy?.$cf?.toast?.({ message: error.message, level: 'warning' })
    }
  }
  if (hasUser && !conversationId.value) {
    await restoreLatestConversationId()
  }
  if (conversationId.value && userId.value) {
    await loadCompleteHistory()
  } else {
    messages.value = []
    historyCursor.value = null
    hasMoreHistory.value = false
    showScrollToBottom.value = false
  }
})

onMounted(() => {
  nextTick(() => updateScrollAffordance())
  if (typeof window !== 'undefined') {
    windowScrollHandler = () => updateScrollAffordance()
    window.addEventListener('scroll', windowScrollHandler, { passive: true })
  }
})

onBeforeUnmount(() => {
  finalizeStream({ cancelled: true })
  stopStream()
  voiceBus.emit('voice:stop', currentUtteranceId)
  if (typeof window !== 'undefined' && windowScrollHandler) {
    window.removeEventListener('scroll', windowScrollHandler)
    windowScrollHandler = null
  }
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
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f8f9f8;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial,
  "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
}

.topbar {
  display: grid;
  grid-template-columns: 80rpx 1fr 80rpx;
  align-items: center;          /* 让三列都居中 */
  height: 112rpx;
  background: #fff;
  padding: 0 24rpx;
}

/* 包一层，用 flex 真正居中 */
.back-wrap {
  display: flex;
  align-items: center;          /* 垂直居中箭头 */
  height: 100%;
}

/* 箭头本身 */
.back-icon {
  font-size: 80rpx;             /* 不要 110rpx 太大就显得掉得更厉害 */
  color: #2f3d2f;
  line-height: 1;
  transform: scaleX(0.75);      /* 瘦一点 */
  transform-origin: left center;
  padding: 0 8rpx;
  /* 微调往上提一点点，让视觉更平 */
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


/* 主体 */
.chat-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.messages {
  flex: 1;
  overflow-y: auto;
  padding: 30rpx 28rpx 180rpx;
}

/* 消息 */
.message-row {
  display: flex;
  gap: 20rpx;
  margin-bottom: 24rpx;
  align-items: flex-start;
}
.message-row.user {
  flex-direction: row-reverse;
}
.avatar {
  width: 74rpx;
  height: 74rpx;
  border-radius: 999rpx;
  background: #fff;
  object-fit: cover;
  box-shadow: 0 10rpx 26rpx rgba(0, 0, 0, 0.05);
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
.assistant-bubble {
  background: #e6efe1;
  color: #314131;
  border-top-left-radius: 16rpx;
}
.user-bubble {
  background: #dde4d9;
  color: #2f3d2f;
  border-top-right-radius: 16rpx;
}
.bubble-text {
  white-space: pre-wrap;
}
.cursor {
  display: inline-block;
  width: 4rpx;
  height: 1.2em;
  background: #7a8f77;
  margin-left: 6rpx;
  animation: blink 1s steps(2, start) infinite;
}

/* 回到底部：圆角矩形 */
.scroll-to-bottom {
  position: fixed;
  right: 28rpx;
  bottom: 140rpx;
  width: 120rpx;
  height: 72rpx;
  border-radius: 20rpx;
  border: none;
  background: #7a8f77;
  color: #fff;
  font-size: 40rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 14rpx 28rpx rgba(122, 143, 119, 0.28);
  z-index: 20;
}

/* 底部输入 */
.composer {
  position: sticky;
  bottom: 0;
  padding: 0 22rpx 32rpx;
  background: linear-gradient(180deg, rgba(248, 249, 248, 0), #f8f9f8 55%, #f8f9f8 100%);
  z-index: 20;
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
.icon-button {
  min-width: 120rpx;
  height: 72rpx;
  border-radius: 20rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  transition: background 0.15s ease;
}
.mic-button {
  background: rgba(122, 143, 119, 0.12);
  color: #7a8f77;
}
.mic-button.active {
  background: rgba(122, 143, 119, 0.3);
}
.send-button {
  background: #7a8f77;
  color: #fff;
  box-shadow: 0 14rpx 28rpx rgba(122, 143, 119, 0.35);
}
.send-button:disabled {
  background: rgba(122, 143, 119, 0.5);
  box-shadow: none;
}

/* tool 弹窗保持你原本的风格 */
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
  background: #f8f7f2;
  border-radius: 32rpx;
  padding: 48rpx 36rpx;
  box-shadow: 0 26rpx 56rpx rgba(122, 143, 119, 0.28);
  border: 1px solid rgba(122, 143, 119, 0.18);
  display: flex;
  flex-direction: column;
  gap: 28rpx;
  color: #2f3d2f;
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
  color: #7a8f77;
  display: flex;
  align-items: center;
  justify-content: center;
}
.modal-title {
  font-size: 40rpx;
  font-weight: 700;
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
  line-height: 1.6;
}
.modal-row {
  display: flex;
  justify-content: space-between;
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
  color: #2f3d2f;
  text-align: right;
  flex: 1;
  line-height: 1.5;
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
}
.modal-btn.primary {
  background: #7a8f77;
  color: #fff;
}
.modal-btn.secondary {
  background: rgba(122, 143, 119, 0.14);
  color: #2f3d2f;
}
.modal-error {
  color: #b03a3a;
  text-align: center;
}

@keyframes blink {
  0%, 49% { opacity: 1; }
  50%, 100% { opacity: 0; }
}
</style>
