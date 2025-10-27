// src/api/page/friends.js
import service from '@/utils/request'   // ✅ 复用你全局封装的 axios/uni.request

export default {
    /** 获取好友列表 */
    getMyFriendList() {
        return service({
            url: '/friends/my',
            method: 'get'
        })
    }
}
