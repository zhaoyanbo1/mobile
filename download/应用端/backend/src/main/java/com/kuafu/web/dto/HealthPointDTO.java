package com.kuafu.web.dto;

import lombok.Data;

@Data
public class HealthPointDTO {
    private String day;   // MM/dd
    private Integer score;
    private Long ts;      // 毫秒时间戳，前端需要可用
}
