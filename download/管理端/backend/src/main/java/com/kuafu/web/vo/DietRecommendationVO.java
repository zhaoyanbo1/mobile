package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafu.common.domin.StaticResource;
import java.util.List;

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
public class DietRecommendationVO  {

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
