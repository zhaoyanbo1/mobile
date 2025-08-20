package com.kuafu.web.vo;

import java.util.Date;

import com.kuafu.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>活动推荐-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ActivityRecommendationPageVO extends PageRequest {

    @JsonProperty(value = "activityRecommendationId")
    private Integer activityRecommendationId;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "activityTime")
    private Date activityTime;
    @JsonProperty(value = "locationLatitude")
    private String locationLatitude;
    @JsonProperty(value = "locationLongitude")
    private String locationLongitude;
    @JsonProperty(value = "locationAddress")
    private String locationAddress;
    @JsonProperty(value = "suitablePeople")
    private String suitablePeople;
    @JsonProperty(value = "creationTime")
    private Date creationTime;

}
