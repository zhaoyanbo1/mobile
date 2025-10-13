<template>
  <div class="chat-wrap">
    <!-- 顶部栏 -->
    <view class="topbar">
      <button class="back" @click="goBack" aria-label="Back">‹</button>
      <text class="title">AI Chat</text>
      <view class="spacer"></view>
      <button class="new" @click="newConversation">New</button>
    </view>

    <!-- 消息区 -->
    <main ref="scrollRef" class="messages" @scroll.passive="handleScroll">
      <div v-if="historyLoading" class="history-loading">正在加载历史...</div>
      <div v-for="m in messages" :key="m.id" class="row" :class="m.role">
        <template v-if="m.role === 'assistant'">
          <img class="avatar" :src="avatarUrl" alt="assistant" />
          <div class="bubble assistant-bubble" :class="{ 'is-error': m.status === 'ERROR', 'is-streaming': m.streaming }">
            <template v-if="m.text">
              {{ m.text }}<span v-if="m.streaming" class="cursor"></span>
            </template>
            <template v-else>
              <span v-if="m.streaming" class="cursor"></span>
            </template>
            <div v-if="m.status === 'ERROR' && m.errorMessage" class="error-text">{{ m.errorMessage }}</div>
          </div>
        </template>
        <template v-else>
          <div class="bubble user-bubble">{{ m.text }}</div>
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
      <button class="tool-btn" title="Camera" aria-label="Camera">
        <svg viewBox="0 0 24 24" width="22" height="22"><path d="M4 7h3l2-2h6l2 2h3v12H4z" fill="none" stroke="currentColor" stroke-width="2"/><circle cx="12" cy="13" r="4" fill="none" stroke="currentColor" stroke-width="2"/></svg>
      </button>
      <input
          class="input"
          v-model="input"
          :disabled="loading"
          placeholder="Type a message..."
          @keydown.enter.exact.prevent="send"
      />
      <button class="tool-btn" title="Voice" aria-label="Voice" @click="toggleRecording" :class="{active: isRecording}">
        <svg viewBox="0 0 24 24" width="22" height="22"><path d="M12 3a3 3 0 0 0-3 3v6a3 3 0 0 0 6 0V6a3 3 0 0 0-3-3z" fill="none" stroke="currentColor" stroke-width="2"/><path d="M19 11a7 7 0 0 1-14 0M12 19v2" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/></svg>
      </button>
      <button class="send-btn" :disabled="loading || !input.trim()" @click="send">Send</button>
    </footer>
  </div>
</template>

<script setup>
  // import {ref, nextTick, onBeforeUnmount, getCurrentInstance} from 'vue'
  import {ref, nextTick, onBeforeUnmount, getCurrentInstance, onMounted, watch} from 'vue'
  import { fetchEventSource } from '@microsoft/fetch-event-source'
  import { voiceBus } from '@/voice/bus'

  // 头像：随便用一张本地图、CDN、或 emoji 占位
  const avatarUrl = 'https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/svg/1f9d1-1f3fb-200d-1f9af.svg'

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

  const ensureConversation = async (titleHint) => {
    if (conversationId.value) {
      return conversationId.value
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
    messages.value = []
    historyCursor.value = null
    hasMoreHistory.value = true
    input.value = ''
    voiceBus.emit('voice:stop', currentUtteranceId)
    currentUtteranceId = null
  }

  const goBack = () => {
    history.length > 1 ? history.back() : null
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
    if (id) {
      localStorage.setItem('conversationId', id)
    } else {
      localStorage.removeItem('conversationId')
    }
  })
  onLoad(async (options) => {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('chatMessages')
    }
    if (options?.conversationId) {
      conversationId.value = options.conversationId
    } else if (!conversationId.value && typeof window !== 'undefined') {
      const savedId = localStorage.getItem('conversationId')
      if (savedId) {
        conversationId.value = savedId
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
    height: 100vh;
    display: grid;
    grid-template-rows: 56px 1fr auto;
    background: #fff;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
  }

  /* 顶部栏 */
  .topbar {
    display: flex;
    align-items: center;
    padding: 0 12px;
    border-bottom: 1px solid #f1f1f1;
    background: #fff;
  }

  .topbar .back {
    border: none;
    background: transparent;
    padding: 8px;
    cursor: pointer;
  }

  .topbar .title {
    flex: 1;
    text-align: center;
    font-weight: 600;
  }

  .topbar .spacer {
    width: 32px;
  }
  .topbar .new {
    margin-left: 8px;
    padding: 8px;
  }


  /* 消息列表 */
  .messages {
    overflow-y: auto;
    padding: 12px 14px 90px;
    background: #fafafa;
  }
  .history-loading {
    text-align: center;
    color: #9ca3af;
    font-size: 12px;
    margin: 4px 0 12px;
  }


  .row {
    display: flex;
    margin: 8px 0;
  }

  .row.assistant {
    align-items: flex-start;
  }

  .row.user {
    justify-content: flex-end;
  }

  .avatar {
    width: 28px;
    height: 28px;
    margin-right: 8px;
    border-radius: 50%;
    flex: 0 0 auto;
    background: #fff;
    box-shadow: 0 1px 0 rgba(0, 0, 0, .05);
  }

  .bubble {
    max-width: 78%;
    padding: 12px 14px;
    border-radius: 16px;
    color: #fff;
    font-weight: 600;
    word-break: break-word;
    white-space: pre-wrap;
    line-height: 1.35;
    box-shadow: 0 1px 0 rgba(0, 0, 0, .06);
  }

  .assistant-bubble {
    background: #3b4452;
    border-top-left-radius: 8px;
  }

  .assistant-bubble.is-error {
    background: #dc2626;
  }

  .assistant-bubble .cursor {
    display: inline-block;
    width: 2px;
    height: 1.2em;
    background: currentColor;
    margin-left: 4px;
    animation: blink 1s steps(2, start) infinite;
    vertical-align: bottom;
  }

  .assistant-bubble .error-text {
    margin-top: 6px;
    font-size: 12px;
    color: rgba(255, 255, 255, 0.75);
  }

  @keyframes blink {
    0%, 49% {
      opacity: 1;
    }
    50%, 100% {
      opacity: 0;
    }
  }


  .user-bubble {
    background: #f0ab45;
    border-top-right-radius: 8px;
  }

  /* 底部输入栏 */
  .composer {
    position: sticky;
    bottom: 0;
    display: grid;
    grid-template-columns: 40px 1fr 40px 72px;
    gap: 10px;
    align-items: center;
    padding: 10px 12px 16px;
    background: rgba(245, 245, 245, .95);
    backdrop-filter: saturate(150%) blur(6px);
    border-top-left-radius: 18px;
    border-top-right-radius: 18px;
  }

  .input {
    height: 40px;
    border: none;
    border-radius: 10px;
    padding: 0 12px;
    outline: none;
    background: #fff;
    box-shadow: inset 0 0 0 1px #eee;
  }

  .tool-btn, .send-btn {
    height: 40px;
    border: none;
    border-radius: 12px;
    background: #fff;
    cursor: pointer;
    box-shadow: 0 1px 0 rgba(0, 0, 0, .06);
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }

  .tool-btn.active {
    background: #fca5a5;
  }

  .tool-btn:disabled, .send-btn:disabled {
    opacity: .5;
    cursor: not-allowed;
  }

  .send-btn {
    background: #111827;
    color: #fff;
    font-weight: 600;
  }
</style>
<script setup>
</script>