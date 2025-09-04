import { voiceBus } from './bus';
import { segmentText } from './TextSegmenter';
import { VoicePlayer } from './VoicePlayer';

export class VoiceController {
  constructor(config = {}) {
    this.config = Object.assign({
      voiceId: 'alloy',
      speed: 0.95,
      prebufferMs: 400
    }, config);
    this.player = new VoicePlayer({
      prebufferMs: this.config.prebufferMs,
      onProgress: (idx, time) => {
        if (this.currentUtteranceId != null) {
          voiceBus.emit('voice:progress', this.currentUtteranceId, idx, time);
        }
      }
    });
    this.abortController = null;
    voiceBus.on('voice:play', (utteranceId, textOrSegments, options = {}) => {
      const segments = Array.isArray(textOrSegments)
          ? textOrSegments
          : segmentText(textOrSegments);
      this.play(utteranceId, segments, options);
    });
    voiceBus.on('voice:stop', (utteranceId) => {
      if (!utteranceId || utteranceId === this.currentUtteranceId) {
        this.stop();
      }
    });
  }

  async play(utteranceId, segments, options = {}) {
    this.stop();
    this.currentUtteranceId = utteranceId;
    const payload = {
      segments: segments.map(s => s.text),
      voice: options.voiceId || this.config.voiceId,
      speed: options.speed || this.config.speed,
      emotion: options.emotion || 'neutral'
    };
    try {
      this.abortController = new AbortController();
      const res = await fetch('/api/tts/stream', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
        signal: this.abortController.signal
      });
      if (!res.body) throw new Error('no stream');
      const reader = res.body.getReader();
      while (true) {
        const { done, value } = await reader.read();
        if (done) break;
        const chunk = value.buffer.slice(value.byteOffset, value.byteOffset + value.byteLength);
        await this.player.addSegment(chunk);
      }
    } catch (e) {
      const code = e.name === 'AbortError' ? 'aborted' : 'network';
      voiceBus.emit('voice:error', utteranceId, code, e.message);
    }
  }

  stop() {
    if (this.abortController) {
      this.abortController.abort();
      this.abortController = null;
    }
    this.player.stop();
    const id = this.currentUtteranceId;
    this.currentUtteranceId = null;
    if (id != null) {
      voiceBus.emit('voice:stopped', id);
    }
  }
}