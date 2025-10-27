// #ifdef MP-WEIXIN || APP-PLUS || APP-HARMONY
import home_page_name from "../api/config/home_condfig";
let baseurl = import.meta.env.VITE_APP_SERVICE_API;
// #endif

// #ifdef H5
let baseurl = import.meta.env.VITE_APP_BASE_API;
// #endif

// #ifdef H5
import axios from 'axios';

/** 🔒 安全提示函数：跨端兼容 toast / 控制台 / ElMessage */
function safeMsg(type, msg) {
    if (typeof uni !== 'undefined' && uni.showToast) {
        uni.showToast({
            title: String(msg || ''),
            icon: type === 'error' ? 'error' : 'none',
            duration: 2000
        });
        return;
    }
    if (typeof window !== 'undefined' && window.ElMessage) {
        window.ElMessage[type] ? window.ElMessage[type](msg) : window.ElMessage(msg);
        return;
    }
    console[type === 'error' ? 'error' : 'log'](msg);
}

// 创建 axios 实例
const service = axios.create({
    baseURL: baseurl,
    timeout: 50000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8',
        APP_ID: import.meta.env.VITE_APP_ID,
        APP_TYPE: import.meta.env.VITE_APP_TYPE
    }
});

const useLogin = import.meta.env.VITE_USE_LOGIN === 'true';

// 请求拦截器
service.interceptors.request.use(
    (config) => {
        if (!config.headers) throw new Error(`Expected 'config' and 'config.headers' not to be undefined`);
        try {
            if (uni.getStorageSync("h5_token")) {
                config.headers.Authorization = 'Bearer ' + uni.getStorageSync("h5_token");
            }
        } catch (e) {
            console.log(e);
        }
        config.headers.APP_ID = import.meta.env.VITE_APP_ID;
        config.headers.APP_TYPE = import.meta.env.VITE_APP_TYPE;
        config.headers.X_Env = import.meta.env.VITE_APP_ENV;

        const uid = uni.getStorageSync('uid');
        if (uid) config.headers['X-User-Id'] = String(uid);


        return config;
    },
    (error) => Promise.reject(error)
);

// 响应拦截器
service.interceptors.response.use(
    (response) => {
        const res = response.data;
        const {code, message} = res;
        if (code == null || code == undefined) {
            return res;
        }
        if (code === 0) {
            return res;
        } else {
            if (code === 401 || code === 403) {
                uni.removeStorageSync("h5_token");
                location.href =
                    import.meta.env.VITE_BASE;
            } else {
                uni.showToast({
                    title: message || '系统出错',
                    icon: "none"
                })
                // ElMessage({
                //     message: message || '系统出错',
                //     type: 'error',
                //     duration: 5 * 1000,
                // });
                uni.showToast({
                    title: message || '系统出错',
                    icon: "none"
                })

            }
            return  Promise.reject(new Error(message || 'Error'));
        }
    },
    (error) => {
        console.log('请求异常：', error);
        if (error?.response?.status === 401) {
            uni.removeStorageSync("h5_token");
            location.href = import.meta.env.VITE_BASE;
        } else {
            safeMsg('error', '网络异常，请稍后再试!');
            return Promise.reject(new Error('Error'));
        }
    }
);

export default service;
// #endif


// #ifdef MP-WEIXIN || APP-PLUS || APP-HARMONY
let service = (res) => {
    console.log("加载中", res);
    uni.showLoading({ title: '加载中' });

    let { url, data, method, token } = res;
    token = uni.getStorageSync("h5_token");

    return new Promise((resolve, reject) => {
        uni.request({
            url: baseurl + url,
            data,
            method,
            timeout: 18000,
            header: {
                'content-type': 'application/json',
                Authorization: 'Bearer ' + token,
                APP_ID: import.meta.env.VITE_APP_ID,
                APP_TYPE: import.meta.env.VITE_APP_TYPE,
                X_Env: import.meta.env.VITE_APP_ENV
            },
            success(res) {
                if (res.data.code === 401) {
                    setTimeout(() => {
                        uni.navigateTo({ url: `/pages/${home_page_name}/index` });
                        uni.removeStorage({ key: 'h5_token' });
                    }, 800);
                } else if (res.data.code !== 0 && res.data.code !== undefined) {
                    uni.showToast({ title: res.data.message || '系统出错', icon: "none" });
                } else {
                    resolve(res.data);
                }
            },
            fail(err) {
                reject(err);
                console.log(err);
                if (err.errMsg === "request:fail timeout") {
                    uni.showToast({ title: "当前网络状态不佳请刷新重试", icon: "none" });
                }
            },
            complete() {
                uni.hideLoading();
            }
        });
    });
};

export default service;
// #endif
