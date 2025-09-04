export function segmentText(aiText) {
    if (!aiText) return [];
    const regex = /[^，,。！？；;…—]+[，,。！？；;…—]?/g;
    const result = [];
    const matches = aiText.matchAll(regex);
    for (const m of matches) {
        const text = m[0].trim();
        if (!text) continue;
        const last = text.slice(-1);
        let pause = 0;
        if ('。！？?'.includes(last)) {
            pause = 400;
        } else if (',，、；;'.includes(last)) {
            pause = 200;
        }
        result.push({ text, pauseHintMs: pause });
    }
    return result;
}