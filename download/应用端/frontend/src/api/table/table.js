import service from "@/utils/request";

const BASE_API = "/data/invoke";

export default {
    add(params={}) {
        const {table_name, param={}}= params;
        return service({
            url: BASE_API + `?table=${table_name}&method=add`,
            method: "post",
            data: param
        });
    },
    delete(params={}) {
        const {table_name, param={}}= params;
        return service({
            url: BASE_API + `?table=${table_name}&method=delete`,
            method: "post",
            data: param
        });
    },
    update(params={}) {
        const {table_name, param={}}= params;
        return service({
            url: BASE_API + `?table=${table_name}&method=update`,
            method: "post",
            data: param
        });
    },

    get(params={}) {
        const {table_name, param={}}= params;
        return service({
            url: BASE_API + `?table=${table_name}&method=get`,
            method: "post",
            data: param
        });
    },

    // page(params={}) {
    //     const {table_name, param={}}= params;
    //     return service({
    //         url: BASE_API + `?table=${table_name}&method=page`,
    //         method: "post",
    //         data: param
    //     });
    // },

    page(params = {}) {
        const { table_name, param = {}, order_by, sort, page = 1, page_size = 10 } = params;
        const payload = { ...param, __order_by: order_by, __sort: sort, __page: page, __page_size: page_size };
        return service({
              url: BASE_API + `?table=${table_name}&method=page`,
              method: "post",
              data: payload
        });
    },


    // list(params={}) {
    //     const {table_name, param={}}= params;
    //     return service({
    //         url: BASE_API + `?table=${table_name}&method=list`,
    //         method: "post",
    //         data: param
    //     });
    // },

    list(params = {}) {
        const { table_name, param = {}, order_by, sort, limit, page, page_size, order } = params;
       // 兼容老用法：过滤条件仍然放在“顶层”
            const payload = { ...param };
        if (order_by)  payload.__order_by  = order_by;
        if (sort)      payload.__sort      = sort;
        if (order)     payload.__order     = order;      // 兼容 'col desc' 写法
        if (limit!=null)      payload.__limit     = limit;
        if (page!=null)       payload.__page      = page;
        if (page_size!=null)  payload.__page_size = page_size;
        return service({
              url: BASE_API + `?table=${table_name}&method=list`,
             method: "post",
              data: payload
        });
    },
}