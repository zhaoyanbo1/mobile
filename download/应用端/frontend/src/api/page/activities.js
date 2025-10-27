import service from '@/utils/request'

function buildQuery(params = {}) {
    const parts = Object.entries(params)
        .filter(([, value]) => value !== undefined && value !== null && value !== '')
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
    return parts.length ? `?${parts.join('&')}` : ''
}

export default {
    /** 获取活动总览（包含自己管理的活动及待处理的申请） */
    overview(params = {}) {
        const query = buildQuery(params)
        return service({
            url: `/team-activities${query}`,
            method: 'get'
        })
    },

    /** 获取我创建的活动列表 */
    manage(params = {}) {
        const query = buildQuery(params)
        return service({
            url: `/team-activities/manage${query}`,
            method: 'get'
        })
    }
}