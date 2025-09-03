<template>
  <div class="chat-wrap">
    <!-- 顶部栏 -->
    <header class="topbar">
      <button class="back" @click="goBack" aria-label="Back">
        <svg viewBox="0 0 24 24" width="24" height="24"><path d="M15 18l-6-6 6-6" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
      </button>
      <div class="title">Chat</div>
      <div class="spacer"></div>
    </header>

    <!-- 消息区 -->
    <main ref="scrollRef" class="messages">
      <div v-for="(m,i) in messages" :key="i" class="row" :class="m.role">
        <template v-if="m.role === 'assistant'">
          <img class="avatar" :src="avatarUrl" alt="assistant" />
          <div class="bubble assistant-bubble">{{ m.text }}</div>
        </template>
        <template v-else>
          <div class="bubble user-bubble">{{ m.text }}</div>
        </template>
      </div>

      <!-- 正在流式生成时的临时气泡 -->
      <div v-if="streamingChunk" class="row assistant">
        <img class="avatar" :src="avatarUrl" alt="assistant" />
        <div class="bubble assistant-bubble">{{ streamingChunk }}</div>
      </div>
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
import {ref, nextTick, onBeforeUnmount, getCurrentInstance} from 'vue'

// 头像：随便用一张本地图、CDN、或 emoji 占位
const avatarUrl = 'https://cdn.jsdelivr.net/gh/twitter/twemoji@14.0.2/assets/svg/1f9d1-1f3fb-200d-1f9af.svg'

const { proxy } = getCurrentInstance()

const input = ref('')
const messages = ref([
  // 可选：默认欢迎语
  // { role: 'assistant', text: 'Hi! Ask me anything.' }
])
const streamingChunk = ref('')
const loading = ref(false)
const scrollRef = ref(null)
let es = null

const isRecording = ref(false)
const voiceMode = ref(false)
let recognition = null

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
      send()
    }
    recognition.onerror = () => {
      isRecording.value = false
    }
    recognition.onend = () => {
      isRecording.value = false
    }
  }
}

const toggleRecording = () => {
  if (!recognition) {
    proxy?.$cf?.toast?.({ message: '当前浏览器不支持语音识别', level: 'error' })
    return
  }
  if (voiceMode.value) {
    recognition.stop()
    voiceMode.value = false
    isRecording.value = false
    if (typeof window !== 'undefined' && window.speechSynthesis) {
      window.speechSynthesis.cancel()
    }
  } else {
    recognition.start()
    voiceMode.value = true
    isRecording.value = true
  }
}

const speak = (text) => {
  if (!voiceMode.value || typeof window === 'undefined' || !window.speechSynthesis) return
  const utter = new SpeechSynthesisUtterance(text)
  utter.lang = 'zh-CN'
  utter.onend = () => {
    if (voiceMode.value && recognition) {
      recognition.start()
      isRecording.value = true
    }
  }
  window.speechSynthesis.cancel()
  window.speechSynthesis.speak(utter)
}


const autoScroll = async () => {
  await nextTick()
  const el = scrollRef.value
  if (el) el.scrollTop = el.scrollHeight
}
const stopStream = () => {
  if (es) {
    es.close()
    es = null
  }
  loading.value = false
}

const send = () => {
  const q = input.value.trim()
  if (!q || loading.value) return

  // 推入用户消息
  messages.value.push({role: 'user', text: q})
  input.value = ''
  streamingChunk.value = ''
  loading.value = true
  autoScroll()

  // 连接你的后端 SSE：/api/llm/stream?q=...
  es = new EventSource(`/api/llm/chat/stream?q=${encodeURIComponent(q)}`)
  es.onmessage = (e) => {
    if (e.data === '[DONE]') {
      // 把临时流式片段落入最终消息
      if (streamingChunk.value) {
        messages.value.push({role: 'assistant', text: streamingChunk.value})
        speak(streamingChunk.value)
        streamingChunk.value = ''
      }
      stopStream()
      autoScroll()
      return
    }
    streamingChunk.value += e.data
    autoScroll()
  }
  es.onerror = () => {
    stopStream()
  }
}

const goBack = () => {
  // 如果你在路由里，用下面这句；否则留空或自定义
  history.length > 1 ? history.back() : null
}

onBeforeUnmount(() => stopStream())
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

/* 消息列表 */
.messages {
  overflow-y: auto;
  padding: 12px 14px 90px;
  background: #fafafa;
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
