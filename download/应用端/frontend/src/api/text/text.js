import service from "@/utils/request";

const BASE_API = "/api/text";

export default {
    text2text(params = {}) {
        const {text, prompt = ""} = params
        return service({
            url: BASE_API + "/text2text",
            method: "post",
            data: {
                text: text,
                prompt: prompt
            }
        });
    },
    tts(params = {}) {
        const {text} = params
        return service({
            url: BASE_API + "/tts",
            method: "post",
            data: {text: text}
        });
    }
}