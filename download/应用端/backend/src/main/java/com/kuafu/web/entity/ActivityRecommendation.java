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
 * <p>  活动推荐 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("activity_recommendation")
public class ActivityRecommendation  {
    @TableId(value = "activity_recommendation_id", type = IdType.AUTO)
    @JsonProperty("activity_recommendation_id")
    
    
    
    private Integer activityRecommendationId;
    @JsonProperty("title")
    @IsNotNullField(description = "活动标题")
    
    
    @TableField("title")
    private String title;
    @JsonProperty("activity_time")
    @IsNotNullField(description = "活动时间")
    
    
    @TableField("activity_time")
    private Date activityTime;
    @JsonProperty("location_latitude")
    @IsNotNullField(description = "地点纬度")
    
    
    @TableField("location_latitude")
    private String locationLatitude;
    @JsonProperty("location_longitude")
    @IsNotNullField(description = "地点经度")
    
    
    @TableField("location_longitude")
    private String locationLongitude;
    @JsonProperty("location_address")
    @IsNotNullField(description = "地点详细地址")
    
    
    @TableField("location_address")
    private String locationAddress;
    @JsonProperty("suitable_people")
    
    
    @TableField("suitable_people")
    private String suitablePeople;
    @JsonProperty("creation_time")
    
    
    @TableField("creation_time")
    private Date creationTime;





}
