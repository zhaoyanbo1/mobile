package com.kuafu.web.vo;

import java.util.Date;

import com.kuafu.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>饮食推荐-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DietRecommendationPageVO extends PageRequest {

    @JsonProperty(value = "dietRecommendationId")
    private Integer dietRecommendationId;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "difficulty")
    private String difficulty;
    @JsonProperty(value = "requiredTime")
    private String requiredTime;
    @JsonProperty(value = "creationTime")
    private Date creationTime;

}
