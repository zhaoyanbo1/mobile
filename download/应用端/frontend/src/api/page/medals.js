//自动选择正确 BASE 地址
const BASE = import.meta.env.VITE_APP_BASE_API
    ? import.meta.env.VITE_APP_BASE_API.replace(/\/+$/, '')
    : ((import.meta.env.VITE_APP_SERVICE_API || 'http://40.82.192.142').replace(/\/+$/, '') + '/api');

function authHeader() {
    const token = uni.getStorageSync('token') || uni.getStorageSync('h5_token');
    const me = uni.getStorageSync('me') || null;
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers.Authorization = 'Bearer ' + token;
    if (me?.userId || me?.user_info_id)
        headers['X-User-Id'] = me.userId || me.user_info_id;
    return headers;
}

function joinUrl(base, path) {
    if (/^https?:\/\//i.test(path)) return path; // 已是完整URL
    return base.replace(/\/+$/, '') + '/' + path.replace(/^\/+/, '');
}

function request(method, path, data) {
    return new Promise((resolve, reject) => {
        const header = { ...authHeader() };
        const url = joinUrl(BASE, path);
        console.log('[API]', method, url, header, data ?? null);

        uni.request({
            url,
            method,
            header,
            data,
            success: (res) => {
                if (res.statusCode >= 400) return reject(res);
                resolve(res.data);
            },
            fail: reject,
        });
    });
}

// ===== 排行榜 =====
export function getLeaderboard(userId, pageNo = 1, pageSize = 20) {
    const uid = userId ?? '';
    return request(
        'GET',
        `/leaderboard/page?userId=${encodeURIComponent(uid)}&pageNo=${pageNo}&pageSize=${pageSize}`
    );
}

// ===== 好友接口 =====
export function createFriendRequest(receiverId) {
    return request('POST', `/friends/requests`, { receiverId });
}
export function acceptFriendRequest(requestId) {
    return request('POST', `/friends/requests/${requestId}/accept`, {});
}
export function declineFriendRequest(requestId) {
    return request('POST', `/friends/requests/${requestId}/decline`, {});
}
export function cancelFriendRequest(requestId) {
    return request('POST', `/friends/requests/${requestId}/cancel`, {});
}

export default {
    getLeaderboard,
    createFriendRequest,
    acceptFriendRequest,
    declineFriendRequest,
    cancelFriendRequest,
};
