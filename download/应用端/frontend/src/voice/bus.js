class VoiceBus {
    constructor() {
        this.listeners = {};
    }
    on(event, handler) {
        (this.listeners[event] || (this.listeners[event] = [])).push(handler);
    }
    off(event, handler) {
        if (!this.listeners[event]) return;
        this.listeners[event] = this.listeners[event].filter(h => h !== handler);
    }
    emit(event, ...args) {
        (this.listeners[event] || []).forEach(h => {
            try {
                h(...args);
            } catch (e) {
                console.error('voice bus handler error', e);
            }
        });
    }
}

export const voiceBus = new VoiceBus();