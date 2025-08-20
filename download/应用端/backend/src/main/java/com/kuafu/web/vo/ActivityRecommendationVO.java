package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.kuafu.common.entity.StaticResource;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.entity.BaseEntity;
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
public class ActivityRecommendationVO  extends BaseEntity {

    @JsonProperty("activityRecommendationId")
    private Integer activityRecommendationId;
    @JsonProperty("title")
    private String title;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("activityTime")
    private Date activityTime;
    @JsonProperty("locationLatitude")
    private String locationLatitude;
    @JsonProperty("locationLongitude")
    private String locationLongitude;
    @JsonProperty("locationAddress")
    private String locationAddress;
    @JsonProperty("suitablePeople")
    private String suitablePeople;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("creationTime")
    private Date creationTime;




}
