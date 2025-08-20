package com.kuafu.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafu.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafu.web.annotation.IsNotNullField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
/**
 * <p>  药品推荐 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("medicine_recommendation")
public class MedicineRecommendation  {
    @TableId(value = "medicine_recommendation_id", type = IdType.AUTO)
    @JsonProperty("medicine_recommendation_id")
    
    
    
    private Integer medicineRecommendationId;
    @JsonProperty("title")
    @IsNotNullField(description = "药品名称")
    
    
    @TableField("title")
    private String title;
    @JsonProperty("usage_guide")
    
    
    @TableField("usage_guide")
    private String usageGuide;
    @JsonProperty("nearby_pharmacy_info")
    
    
    @TableField("nearby_pharmacy_info")
    private String nearbyPharmacyInfo;
    @JsonProperty("creation_time")
    
    
    @TableField("creation_time")
    private Date creationTime;





}
