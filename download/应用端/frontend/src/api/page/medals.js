// /**
//  * @typedef {Object} LeaderboardRow
//  * @property {number} userId
//  * @property {string} username
//  * @property {string=} avatarUrl
//  * @property {number} medalCount
//  * @property {string=} lastMedalAt
//  */
//
// /**
//  * @typedef {Object} MyMedalStat
//  * @property {number} userId
//  * @property {number} medalCount
//  * @property {string=} lastMedalAt
//  * @property {number} rank
//  */
//
// /**
//  * @typedef {Object} LeaderboardResp
//  * @property {number} page
//  * @property {number} size
//  * @property {number} total
//  * @property {LeaderboardRow[]} list
//  * @property {MyMedalStat=} me
//  */
// export async function getLeaderboard(page = 1, size = 20) {
//     const uid = uni.getStorageSync('userId') || ''
//     const qs = `page=${page}&size=${size}${uid ? `&userId=${uid}` : ''}`
//     const res = await fetch(`/api/medals/leaderboard?${qs}`, { credentials: 'include' })
//     if (!res.ok) throw new Error(`HTTP ${res.status}`)
//     return res.json()
// }
// export default { getLeaderboard };

const BASE = process.env.VUE_APP_BASE_API || '/api';

// function authHeader() {
//     const token =
//         uni.getStorageSync('token') ||
//         uni.getStorageSync('h5_token') ||
//         localStorage.getItem('token') ||
//         localStorage.getItem('h5_token');
//     const headers = {};
//     if (token) headers.Authorization = 'Bearer ' + token;
//     return headers;
// }
function authHeader() {
    const token = uni.getStorageSync('token') || uni.getStorageSync('h5_token');
    const me = uni.getStorageSync('me') || null;

    const headers = { 'Content-Type': 'application/json' };

    if (token) headers.Authorization = 'Bearer ' + token;
    if (me?.userId || me?.user_info_id)
        headers['X-User-Id'] = me.userId || me.user_info_id;   // ✅ 这一行很重要！

    return headers;
}



function request(method, path, data) {
    return new Promise((resolve, reject) => {

        const header = { 'Content-Type': 'application/json', ...authHeader() }
        const url = BASE + path
        console.log('[API]', method, url, header, data ?? null)   // ← 新增日志

        uni.request({
            // url: BASE + path,
            // method,
            // header: { 'Content-Type': 'application/json', ...authHeader() },
            // data,
            url,
            method,
            header,
            data,
            success: (res) => (res.statusCode < 400 ? resolve(res.data) : reject(res)),
            fail: reject
        });
    });
}

// function request(method, path, data) {
//     const token = uni.getStorageSync('token');
//     const me = uni.getStorageSync('me');  // 这里存有 userId 或 user_info_id
//     const headers = {
//         'Content-Type': 'application/json',
//     };
//
//     if (token) headers.Authorization = 'Bearer ' + token;
//     if (me?.userId || me?.user_info_id)
//         headers['X-User-Id'] = me.userId || me.user_info_id;  // ✅ 新增这一行
//
//     console.log('[API]', method, path, headers); // 用于调试
//     return new Promise((resolve, reject) => {
//         uni.request({
//             url: BASE + path,
//             method,
//             header: headers,
//             data,
//             success: (res) => (res.statusCode < 400 ? resolve(res.data) : reject(res)),
//             fail: reject
//         });
//     });
// }

// 排行榜（保留你原来的 userId 传参）
export function getLeaderboard(userId, pageNo = 1, pageSize = 20) {
    // return request('GET', `/leaderboard/page?userId=${userId ?? ''}&pageNo=${pageNo}&pageSize=${pageSize}`);
    return request('GET', `/leaderboard/page?userId=${userId ?? ''}&pageNo=${pageNo}&pageSize=${pageSize}`)
}

// 好友接口（同样自动带 token）
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


export default { getLeaderboard,createFriendRequest, acceptFriendRequest, declineFriendRequest, cancelFriendRequest };
