package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafu.common.domin.StaticResource;
import java.util.List;

/**
 * <p>活动推荐</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRecommendationVO  {

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
