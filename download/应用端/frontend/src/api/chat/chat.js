import service from '@/utils/request'

export default {
    createConversation(data) {
        return service({
            url: '/conversations',
            method: 'post',
            data,
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
}