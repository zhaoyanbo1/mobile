import service from "@/utils/request";

export default {
    loginWxWeb(data) {
        return service({
            url: "/login/wxWeb",
            method: "post",
            data: data
        });
    },
    loginPasswd(data) {
        return service({
            url: "/login/passwd",
            method: "post",
            data: data
        });
    },
    getMessage() {
        return service({
            url: "/getLoginUser",
            method: "get",

        });
    },
    getLoginUser() {
        return service({
            url: "/getUserInfo",
            method: "get",
        });
    },
    getCodeByPhone(phone) {
        return service({
            url: "/login/sms/code?phone=" + phone,
            method: "get",

        });
    },
    loginBySms(data) {
        return service({
            url: "/login/sms",
            method: "post",
            data: data
        });
    },
    getOpenIdByCode(code){
        return service({
            url: "/login/openid?code=" + code,
            method: "get",
        });
    },
    getMpUrl(){
        return service({
            url: "/get_mp_url",
            method: "post",
        });
    }
};