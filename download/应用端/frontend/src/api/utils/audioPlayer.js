// src/utils/audioPlayer.js
export default class AudioPlayer {
    constructor({ src, autoplay = false, loop = false }) {
        console.log(src, autoplay, loop);

        this.audio = new Audio(src);
        this.audio.loop = loop;
        this.audio.autoplay = autoplay;
        this.events = {};

        // === 核心新增：监听音频元数据加载 ===
        this.audio.addEventListener('loadedmetadata', () => {
            this.emit('durationchange', this.getDuration());
        });

        // 播放进度/控制相关事件
        this.audio.addEventListener('play', () => this.emit('play'));
        this.audio.addEventListener('pause', () => this.emit('pause'));
        this.audio.addEventListener('ended', () => this.emit('ended'));
        this.audio.addEventListener('timeupdate', () =>
            this.emit('timeupdate', this.getCurrentTime())
        );
    }

    // 播放：返回 Promise，供外部捕获异常（浏览器策略）
    play() {
        return this.audio.play().catch((err) => {
            console.warn('播放失败：', err);
            this.emit('error', err);
        });
    }

    pause() {
        this.audio.pause();
    }

    seek(seconds) {
        if (!isNaN(this.audio.duration)) {
            this.audio.currentTime = Math.min(this.audio.duration, Math.max(0, seconds));
        }
    }

    getCurrentTime() {
        return this.audio.currentTime || 0;
    }

    getDuration() {
        return this.audio.duration || 0;
    }

    on(event, callback) {
        if (!this.events[event]) this.events[event] = [];
        this.events[event].push(callback);
    }

    emit(event, ...args) {
        if (this.events[event]) {
            for (const cb of this.events[event]) {
                cb(...args);
            }
        }
    }

    destroy() {
        this.pause();
        this.audio.src = '';
        this.audio = null;
        this.events = {};
    }
}