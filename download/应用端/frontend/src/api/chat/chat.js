import service from '@/utils/request'

export default {
    createConversation(data) {
        return service({
            url: '/conversations',
            method: 'post',
            data,
        })
    },
    listConversations({ userId, page = 1, size = 20, status } = {}) {
        const params = { userId, page, size }
        if (status) {
            params.status = status
        }
        return service({
            url: '/conversations',
            method: 'get',
            params,
        })
    },
    fetchMessages({ conversationId, userId, cursor, limit }) {
        const params = { userId }
        if (cursor !== undefined && cursor !== null) {
            params.cursor = cursor
        }
        if (limit !== undefined && limit !== null) {
            params.limit = limit
        }
        return service({
            url: `/conversations/${conversationId}/messages`,
            method: 'get',
            params,
        })
    },
    executeTodoTool(data) {
        return service({
            url: '/chat/tools/todos/execute',
            method: 'post',
            data,
        })
    },
    declineTodoTool(data) {
        return service({
            url: '/chat/tools/todos/decline',
            method: 'post',
            data,
        })
    },
}