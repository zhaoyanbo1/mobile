package com.kuafu.web.vo;

import java.util.Date;

import com.kuafu.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>药品推荐-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedicineRecommendationPageVO extends PageRequest {

    @JsonProperty(value = "medicineRecommendationId")
    private Integer medicineRecommendationId;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "usageGuide")
    private String usageGuide;
    @JsonProperty(value = "nearbyPharmacyInfo")
    private String nearbyPharmacyInfo;
    @JsonProperty(value = "creationTime")
    private Date creationTime;

}
