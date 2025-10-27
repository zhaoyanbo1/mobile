package com.kuafu.web.vo;

import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MyMedalStatVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long medalCount;
    private Date lastMedalAt;
    private Long rank;
}
