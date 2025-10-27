import { computed, reactive } from 'vue'

type FriendKey = string

type ConversationKey = string

interface FriendEntry {
    unread: number
    conversationId?: ConversationKey
    nickname?: string
    avatarUrl?: string
}

const state = reactive({
    friends: {} as Record<FriendKey, FriendEntry>,
    conversationToFriend: {} as Record<ConversationKey, FriendKey>,
    activityPending: 0
})

const friendUnreadTotal = computed(() => {
    return Object.values(state.friends).reduce((sum, entry) => sum + (entry.unread || 0), 0)
})

const activityPendingCount = computed(() => (state.activityPending > 0 ? state.activityPending : 0))

function ensureFriendEntry(friendId: FriendKey): FriendEntry {
    if (!state.friends[friendId]) {
        state.friends[friendId] = { unread: 0 }
    }
    return state.friends[friendId]
}

function normalizeKey(value: string | number | null | undefined): string | undefined {
    if (value === null || value === undefined) return undefined
    return String(value)
}

function applyFriendList(list: Array<Record<string, any>> = []) {
    const seen = new Set<FriendKey>()

    list.forEach((raw) => {
        if (!raw) return
        const friendId = normalizeKey(raw.userId ?? raw.friendId)
        if (!friendId) return
        seen.add(friendId)

        const entry = ensureFriendEntry(friendId)
        const conversationKey = normalizeKey(raw.conversationId)

        entry.unread = Math.max(0, Number(raw.unreadCount ?? entry.unread ?? 0))
        if (conversationKey) {
            entry.conversationId = conversationKey
            state.conversationToFriend[conversationKey] = friendId
        }
        if (raw.nickname != null) entry.nickname = raw.nickname
        if (raw.avatarUrl != null) entry.avatarUrl = raw.avatarUrl
    })

    Object.keys(state.friends).forEach((friendId) => {
        if (!seen.has(friendId)) {
            const conversationKey = state.friends[friendId].conversationId
            if (conversationKey) {
                delete state.conversationToFriend[conversationKey]
            }
            delete state.friends[friendId]
        }
    })
}

function setFriendUnread(friendId: string | number, count: number) {
    const key = normalizeKey(friendId)
    if (!key) return
    const entry = ensureFriendEntry(key)
    entry.unread = count > 0 ? count : 0
}

function markFriendRead(friendId: string | number | null | undefined) {
    const key = normalizeKey(friendId)
    if (!key) return
    const entry = ensureFriendEntry(key)
    entry.unread = 0
}

function markConversationRead(conversationId: string | number | null | undefined) {
    const key = normalizeKey(conversationId)
    if (!key) return
    const friendId = state.conversationToFriend[key]
    if (friendId) {
        markFriendRead(friendId)
    }
}

function setActivityPendingCount(count: number) {
    state.activityPending = count > 0 ? count : 0
}

function setConversationForFriend(friendId: string | number | null | undefined, conversationId: string | number | null | undefined) {
    const friendKey = normalizeKey(friendId)
    const conversationKey = normalizeKey(conversationId)
    if (!friendKey) return
    const entry = ensureFriendEntry(friendKey)
    if (conversationKey) {
        entry.conversationId = conversationKey
        state.conversationToFriend[conversationKey] = friendKey
    }
}

function getFriendUnread(friendId: string | number | null | undefined): number {
    const key = normalizeKey(friendId)
    if (!key) return 0
    return state.friends[key]?.unread ?? 0
}

export default function useNotificationStore() {
    return {
        state,
        friendUnreadTotal,
        activityPendingCount,
        applyFriendList,
        setFriendUnread,
        markFriendRead,
        markConversationRead,
        setActivityPendingCount,
        setConversationForFriend,
        getFriendUnread
    }
}