package com.kuafu.web.vo;

public class MedalCollectRespVO {
    private boolean created; // true=新授予；false=之前已有
    public MedalCollectRespVO(boolean created){ this.created = created; }
    public boolean isCreated(){ return created; }
}
