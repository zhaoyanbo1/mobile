import { voiceBus } from './bus';

export class VoicePlayer {
    constructor(options = {}) {
        this.ctx = null;
        this.queue = [];
        this.current = null;
        this.played = 0;
        this.options = Object.assign({ prebufferMs: 400, onProgress: () => {}, onEnd: () => {} }, options);
        this.started = false;
    }

    _ensureContext() {
        if (!this.ctx) {
            const Ctx = window.AudioContext || window.webkitAudioContext;
            this.ctx = new Ctx();
        }
    }

    async addSegment(arrayBuffer) {
        this._ensureContext();
        const buffer = await this.ctx.decodeAudioData(arrayBuffer.slice(0));
        this.queue.push(buffer);
        if (!this.started) {
            this.started = true;
            setTimeout(() => this._playNext(), this.options.prebufferMs);
        }
    }

    _playNext() {
        if (!this.queue.length) {
            this.started = false;
            this.options.onEnd();
            return;
        }
        const buffer = this.queue.shift();
        const source = this.ctx.createBufferSource();
        source.buffer = buffer;
        source.connect(this.ctx.destination);
        const index = this.played;
        this.options.onProgress(index, 0);
        source.onended = () => {
            this.options.onProgress(index, buffer.duration * 1000);
            this._playNext();
        };
        source.start();
        this.current = source;
        this.played++;
    }

    stop() {
        if (this.current) {
            try {
                this.current.stop();
            } catch (e) {}
            this.current = null;
        }
        this.queue = [];
        this.started = false;
        this.played = 0;
    }
}