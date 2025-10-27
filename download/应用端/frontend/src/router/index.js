import { createRouter, createWebHistory } from 'vue-router';

// 定义路径与组件的对应关系
const routes = [
    { path: '/', redirect: '/friends' }, // 访问根路径时自动跳转到好友列表
    { path: '/friends', name: 'friends', component: () => import('@/pages/chat/FriendList.vue') },
    { path: '/chat', name: 'chat', component: () => import('@/pages/chat/ChatRoom.vue') },
];

// 创建路由对象
export const router = createRouter({
    history: createWebHistory(), // 使用浏览器的历史模式（URL 没有 #）
    routes,
});
