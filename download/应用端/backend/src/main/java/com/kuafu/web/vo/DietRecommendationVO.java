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
 * <p>饮食推荐</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DietRecommendationVO  extends BaseEntity {

    @JsonProperty("dietRecommendationId")
    private Integer dietRecommendationId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("difficulty")
    private String difficulty;
    @JsonProperty("requiredTime")
    private String requiredTime;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("creationTime")
    private Date creationTime;




}
