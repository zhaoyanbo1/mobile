export const dynamicRoutes = [{
        path: '/',
        meta: {
            title: '首页',
            isParentView: false
        },
        component: ()=> import('@/views/dashboard_statistic/index.vue'),
    }, {
        path: '/system_config',
        meta: {
            title: '系统配置',
            shownot: false
        },
        component: ()=> import('@/views/system_config/index.vue'),
    }, {
        path: '/user_info',
        meta: {
            title: '用户信息',
            shownot: false
        },
        component: ()=> import('@/views/user_info/index.vue'),
    }, {
        path: '/health_questionnaire',
        meta: {
            title: '健康问卷',
            shownot: false
        },
        component: ()=> import('@/views/health_questionnaire/index.vue'),
    }, {
        path: '/reminder_item',
        meta: {
            title: '提醒事项',
            shownot: false
        },
        component: ()=> import('@/views/reminder_item/index.vue'),
    }, {
        path: '/reminder_type_enum',
        meta: {
            title: '提醒类型枚举',
            shownot: false
        },
        component: ()=> import('@/views/reminder_type_enum/index.vue'),
    }, {
        path: '/activity_recommendation',
        meta: {
            title: '活动推荐',
            shownot: false
        },
        component: ()=> import('@/views/activity_recommendation/index.vue'),
    }, {
        path: '/diet_recommendation',
        meta: {
            title: '饮食推荐',
            shownot: false
        },
        component: ()=> import('@/views/diet_recommendation/index.vue'),
    }, {
        path: '/medicine_recommendation',
        meta: {
            title: '药品推荐',
            shownot: false
        },
        component: ()=> import('@/views/medicine_recommendation/index.vue'),
    }, {
        path: '/emergency_contact',
        meta: {
            title: '紧急联系人',
            shownot: false
        },
        component: ()=> import('@/views/emergency_contact/index.vue'),
    }, {
        path: '/system_settings',
        meta: {
            title: '系统设置',
            shownot: false
        },
        component: ()=> import('@/views/system_settings/index.vue'),
    }, {
        path: '/dynamic_api_setting',
        meta: {
            title: 'API配置',
            shownot: false
        },
        component: ()=> import('@/views/dynamic_api_setting/index.vue'),
    }, {
        path: '/system_config/detail',
        meta: {
            title: '系统配置详情',
            shownot: true
        },
        component: ()=> import('@/views/system_config/detail/index.vue'),
    }, {
        path: '/user_info/detail',
        meta: {
            title: '用户信息详情',
            shownot: true
        },
        component: ()=> import('@/views/user_info/detail/index.vue'),
    }, {
        path: '/health_questionnaire/detail',
        meta: {
            title: '健康问卷详情',
            shownot: true
        },
        component: ()=> import('@/views/health_questionnaire/detail/index.vue'),
    }, {
        path: '/reminder_item/detail',
        meta: {
            title: '提醒事项详情',
            shownot: true
        },
        component: ()=> import('@/views/reminder_item/detail/index.vue'),
    }, {
        path: '/reminder_type_enum/detail',
        meta: {
            title: '提醒类型枚举详情',
            shownot: true
        },
        component: ()=> import('@/views/reminder_type_enum/detail/index.vue'),
    }, {
        path: '/activity_recommendation/detail',
        meta: {
            title: '活动推荐详情',
            shownot: true
        },
        component: ()=> import('@/views/activity_recommendation/detail/index.vue'),
    }, {
        path: '/diet_recommendation/detail',
        meta: {
            title: '饮食推荐详情',
            shownot: true
        },
        component: ()=> import('@/views/diet_recommendation/detail/index.vue'),
    }, {
        path: '/medicine_recommendation/detail',
        meta: {
            title: '药品推荐详情',
            shownot: true
        },
        component: ()=> import('@/views/medicine_recommendation/detail/index.vue'),
    }, {
        path: '/emergency_contact/detail',
        meta: {
            title: '紧急联系人详情',
            shownot: true
        },
        component: ()=> import('@/views/emergency_contact/detail/index.vue'),
    }, {
        path: '/system_settings/detail',
        meta: {
            title: '系统设置详情',
            shownot: true
        },
        component: ()=> import('@/views/system_settings/detail/index.vue'),
    }, {
        path: '/dynamic_api_setting/detail',
        meta: {
            title: 'API配置详情',
            shownot: true
        },
        component: ()=> import('@/views/dynamic_api_setting/detail/index.vue'),
    }]