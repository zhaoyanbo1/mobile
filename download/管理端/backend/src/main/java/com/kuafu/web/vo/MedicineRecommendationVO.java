package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafu.common.domin.StaticResource;
import java.util.List;

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
public class MedicineRecommendationVO  {

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
