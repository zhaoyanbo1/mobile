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
 * <p>药品推荐</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRecommendationVO  extends BaseEntity {

    @JsonProperty("medicineRecommendationId")
    private Integer medicineRecommendationId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("usageGuide")
    private String usageGuide;
    @JsonProperty("nearbyPharmacyInfo")
    private String nearbyPharmacyInfo;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("creationTime")
    private Date creationTime;




}
