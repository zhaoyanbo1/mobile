import service from "@/utils/request";

export default {
    logout(params = {}) {
        return service({
            url: "/logout",
            method: "get",
        });
    }
}