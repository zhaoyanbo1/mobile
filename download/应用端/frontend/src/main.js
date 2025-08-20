import {createSSRApp} from "vue";
import api from "@/api"
import App from "./App.vue";
import '@/styles/index.css';
import {createI18n} from 'vue-i18n';
import en from '@/i18n/en.js';
import zh from '@/i18n/zh.js';
import get_resource_url from "@/api/config/static_config";
import wxShare from "./api/utils/wxShare";
import AudioPlayer from '@/api/utils/audioPlayer';

export function createApp() {
    const lange = import.meta.env.VITE_APP_LANGE;
    const i18n = createI18n({
        locale: lange,         // 默认语言
        fallbackLocale: 'en', // 备用语言
        messages: {
            en,
            zh
        }
    });

    const app = createSSRApp(App);
    app.config.globalProperties.$cf = api;
    app.config.globalProperties.get_resource_url= get_resource_url;
    app.config.globalProperties.$audioPlayer = AudioPlayer;

    app.use(i18n);
    app.mixin(wxShare)
    app.config.globalProperties.$tt = i18n.global.t;
    return {
        app,
    };
}