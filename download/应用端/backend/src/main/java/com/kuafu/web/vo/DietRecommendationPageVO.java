package com.kuafu.web.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.entity.BaseEntity;
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
public class DietRecommendationPageVO extends BaseEntity {

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
