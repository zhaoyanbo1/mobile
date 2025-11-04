// src/utils/request.js
import axios from 'axios';

/** 读取环境变量（兼容多端 & 运行时覆盖） */
function readEnv() {
    const env = (typeof import.meta !== 'undefined' && import.meta.env) ? import.meta.env : {};
    // 运行时覆盖（可在控制台或入口处注入）
    const runtimeWin = (typeof window !== 'undefined' ? window.__BASE_API__ : '');
    const runtimeLS  = (typeof localStorage !== 'undefined' ? localStorage.getItem('BASE_API') : '');

    return {
        base: env.VITE_APP_BASE_API || env.VITE_BASE_API || env.VITE_API_BASE || '',
        svc:  env.VITE_APP_SERVICE_API || env.VITE_SERVICE_API || '',
        rt:   runtimeWin || runtimeLS || '',
    };
}

/** 规范化：去掉末尾多余斜杠 */
function stripEndSlash(s) {
    return s.replace(/\/+$/, '');
}

/** 解析后端基地址：
 *  优先顺序：运行时覆盖(__BASE_API__/localStorage) > VITE_APP_BASE_API(全量) > VITE_APP_SERVICE_API + '/api' > location.origin + '/api' > 固定回退
 */
function resolveBase() {
    const { base, svc, rt } = readEnv();

    // 1) 运行时覆盖：window.__BASE_API__ 或 localStorage.BASE_API
    if (rt && /^https?:\/\//i.test(rt)) return stripEndSlash(rt);

    // 2) .env 的全量 BASE（推荐直接写成 http://host:port/api）
    if (base && /^https?:\/\//i.test(base)) return stripEndSlash(base);

    // 3) .env 的 SERVICE（裸 host），自动拼 '/api'
    if (svc && /^https?:\/\//i.test(svc)) return stripEndSlash(svc) + '/api';

    // 4) H5 回落到本域 /api
    if (typeof location !== 'undefined' && location.origin) {
        return stripEndSlash(location.origin) + '/api';
    }

    // 5) 最终兜底
    return 'http://40.82.192.142/api';
}

const BASE_URL = resolveBase();
console.log('[REQ] baseURL =', BASE_URL);

/** 创建统一 axios 实例 */
const service = axios.create({
    baseURL: BASE_URL,
    timeout: 15000,
    headers: { 'Content-Type': 'application/json;charset=utf-8' }
});

/** 请求拦截：统一 Token / X-User-Id；并把以 “/” 开头的 url 规范化成相对路径，避免覆盖 baseURL 的路径段 */
service.interceptors.request.use(cfg => {
    const token = (typeof uni !== 'undefined' && (uni.getStorageSync('token') || uni.getStorageSync('h5_token'))) || '';
    const me    = (typeof uni !== 'undefined' && uni.getStorageSync('me')) || null;

    cfg.headers = cfg.headers || {};
    if (token) cfg.headers.Authorization = `Bearer ${token}`;
    const uid = me?.userId || me?.user_info_id || (typeof uni !== 'undefined' && uni.getStorageSync('uid'));
    if (uid) cfg.headers['X-User-Id'] = String(uid);

    // ⚠️关键：把 "/xxx" 改为 "xxx"，避免某些环境把 "/xxx" 当成“覆盖路径”处理
    if (typeof cfg.url === 'string' && cfg.url.startsWith('/')) {
        cfg.url = cfg.url.replace(/^\/+/, '');
    }

    return cfg;
});

/** 响应拦截：直接返回 data */
service.interceptors.response.use(
    res => res.data,
    err => Promise.reject(err)
);

export default service;