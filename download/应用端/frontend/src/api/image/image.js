import service from "@/utils/request";

const BASE_API = "/api/pic";

export default {
    word2pic(params = {}) {
        const {text} = params
        return service({
            url: BASE_API + "/word2pic",
            method: "post",
            data: {text: text}
        });
    },
    pic2word(params = {}) {
        const {url} = params
        return service({
            url: BASE_API + "/pic2word",
            method: "post",
            data: {file: url}
        });
    },
    ocr(params = {}) {
        const {url} = params
        return service({
            url: BASE_API + "/ocr",
            method: "post",
            data: {file: url}
        });
    }
}