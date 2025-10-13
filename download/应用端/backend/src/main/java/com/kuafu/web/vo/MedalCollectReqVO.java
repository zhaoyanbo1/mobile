package com.kuafu.web.vo;

import javax.validation.constraints.NotNull;

public class MedalCollectReqVO {
    // 如果你已接入 JWT，可不传 userId，从 token 取；这里保留可选 userId 以兼容现状
    private Long userId;

    @NotNull
    private Long medalId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getMedalId() { return medalId; }
    public void setMedalId(Long medalId) { this.medalId = medalId; }
}