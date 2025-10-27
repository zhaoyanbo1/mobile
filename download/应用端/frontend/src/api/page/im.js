// // src/api/page/im.js
// import service from '@/utils/request'
//
// export default {
//     /** 确保/创建 1v1 会话 */
//     ensureConversation(peerId) {
//         return service({
//             url: `/im/conversations/dm?peerId=${peerId}`,
//             method: 'post'
//         })
//     },
//
//     /** 拉取消息 */
//     getMessages(conversationId, beforeId = null, limit = 20) {
//         const params = new URLSearchParams({ conversationId, limit })
//         if (beforeId) params.append('beforeId', beforeId)
//         return service({
//             url: `/im/messages?${params.toString()}`,
//             method: 'get'
//         })
//     },
//
//     /** 发送消息 */
//     sendMessage({ conversationId, content }) {
//         return service({
//             url: '/im/messages',
//             method: 'post',
//             data: {
//                 conversationId,
//                 contentType: 'TEXT',
//                 content
//             }
//         })
//     },
//
//     /** 标记已读 */
//     markRead(conversationId, lastReadMsgId) {
//         return service({
//             url: `/im/read?conversationId=${conversationId}&lastReadMsgId=${lastReadMsgId}`,
//             method: 'post'
//         })
//     }
// }
// src/api/page/im.js
import service from '@/utils/request'   // 你的统一请求封装（前面我们已确认）

// 适配：既兼容 axios 实例，也兼容 uni.request 封装函数
function GET(url, params = {}) {
    if (typeof service.get === 'function') {
        // axios 分支
        return service.get(url, { params })
    }
    // uni.request 分支
    return service({ url, method: 'get', data: params })
}

function POST(url, data = {}) {
    if (typeof service.post === 'function') {
        // axios 分支
        return service.post(url, data)
    }
    // uni.request 分支
    return service({ url, method: 'post', data })
}

// 按你后端路由来：之前网络面板显示是  /api/im/ 下的 dm、messages
// baseURL 已在 request.js 里配置过，所以这里用相对路径即可
const prefix = '/im'

// 1) 确保/获取 1v1 会话：GET /im/conversations/dm?peerId=xxx
//    服务器返回会话ID（Long）
function ensureDm(peerId) {
    // return GET(`${prefix}/conversations/dm`, { peerId })
    return POST(`${prefix}/conversations/dm?peerId=${peerId}`)
}

// 2) 拉历史：GET /im/messages?conversationId&beforeId&limit
//    服务器返回 { list: [...], nextBeforeId: xxx } 或 {data:{list:[]}}
function getMessages(conversationId, beforeId = null, limit = 20) {
    const params = { conversationId, limit }
    if (beforeId) params.beforeId = beforeId
    return GET(`${prefix}/messages`, params)
}

// 3) 发送消息：POST /im/messages
//    payload: { conversationId?, peerId?, contentType, content }
function sendMessage(payload) {
    return POST(`${prefix}/messages`, payload)
}

// 也可以按需导出单个函数
export default {
    ensureDm,
    getMessages,
    sendMessage,
}
