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
        <view class="bubble">
          {{ m.content }}
        </view>
      </view>
      <!-- 占位，保证滚动到底 -->
      <view style="height: 10px;"></view>
    </scroll-view>

    <!-- 底部输入栏 -->
    <view class="input-bar">
      <input
          class="ipt"
          v-model="inputText"
          confirm-type="send"
          @confirm="sendMessage"
          placeholder="输入消息…"
      />
      <button class="btn-send" @click="sendMessage">发送</button>
    </view>
  </view>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import imApi from '@/api/page/im.js' // ← 你的 IM 接口文件（默认导出）

/** 当前登录用户 ID，用于左右判断 */
const myId = ref(Number(uni.getStorageSync('uid')) || 0)

/** 页面状态 */
const peerId = ref(null)             // 对方用户ID
const peerName = ref('')             // 对方昵称（标题展示）
const conversationId = ref(null)     // 当前会话ID
const messages = ref([])             // 消息列表
const inputText = ref('')            // 输入框内容
const scrollTop = ref(0)             // 控制滚动

/** 进入页面：拿到 query 参数并加载历史 */
onLoad(async (q) => {
  // 兼容不同命名
  if (q?.peerId)   peerId.value = Number(q.peerId)
  if (q?.nickname) peerName.value = decodeURIComponent(q.nickname)

  // 如果通过聊天列表跳转时已经带了会话ID，直接用；否则确保会话存在
  if (q?.conversationId) {
    conversationId.value = Number(q.conversationId)
  } else if (peerId.value) {
    // 确保/创建 1v1 会话（若你的 im.js 方法名不同，请改成你的）
    const r = await imApi.ensureDm(peerId.value)
    conversationId.value = r?.data ?? r ?? null
  }

  await loadMessages()
  scrollToBottom(true)
})

/** 统一消息结构 */
function normalizeMsg(m) {
  return {
    messageId: m.messageId ?? Date.now(),
    senderId: m.senderId,
    contentType: m.contentType || 'TEXT',
    content: m.content ?? '',
    createdAtEpochMs: m.createdAtEpochMs ?? Date.now()
  }
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
  scrollToBottom(true)
}



/** 发送文本消息：先本地追加“临时消息”，成功后用服务端返回覆盖 */
async function sendMessage() {
  const text = inputText.value.trim()
  if (!text) return
  if (!conversationId.value && !peerId.value) {
    uni.showToast({ title: '缺少会话信息', icon: 'none' })
    return
  }

  inputText.value = ''

  // 1) 本地先追加一条临时消息（右侧）
  const tempId = Date.now()
  const tempMsg = {
    messageId: tempId,
    senderId: myId.value,
    contentType: 'TEXT',
    content: text,
    createdAtEpochMs: Date.now(),
    _pending: true
  }
  messages.value.push(tempMsg)
  sortMessages()
  scrollToBottom()

  // 2) 调用后端发送
  try {
    const resp = await imApi.sendMessage({
      conversationId: conversationId.value, // 若为空，服务端会使用 peerId 创建会话（取决于你的实现）
      peerId: peerId.value,
      contentType: 'TEXT',
      content: text
    })

    // 3) 用服务端返回的值覆盖临时消息（避免重复）
    const saved = normalizeMsg(resp?.data ?? resp)
    // 如果后端可能返回新的 conversationId，这里接收并更新
    if (!conversationId.value && saved.conversationId) {
      conversationId.value = saved.conversationId
    }

    const idx = messages.value.findIndex(m => m.messageId === tempId)
    if (idx > -1) messages.value[idx] = saved
    else messages.value.push(saved)

    sortMessages()
    scrollToBottom()
  } catch (e) {
    // 失败：移除临时消息并提示
    messages.value = messages.value.filter(m => m.messageId !== tempId)
    uni.showToast({ title: '发送失败', icon: 'none' })
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
</style>
