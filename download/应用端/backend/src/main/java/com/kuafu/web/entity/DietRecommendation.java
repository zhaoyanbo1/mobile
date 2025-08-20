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
 * <p>  饮食推荐 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("diet_recommendation")
public class DietRecommendation  {
    @TableId(value = "diet_recommendation_id", type = IdType.AUTO)
    @JsonProperty("diet_recommendation_id")
    
    
    
    private Integer dietRecommendationId;
    @JsonProperty("title")
    @IsNotNullField(description = "食谱标题")
    
    
    @TableField("title")
    private String title;
    @JsonProperty("difficulty")
    
    
    @TableField("difficulty")
    private String difficulty;
    @JsonProperty("required_time")
    
    
    @TableField("required_time")
    private String requiredTime;
    @JsonProperty("creation_time")
    
    
    @TableField("creation_time")
    private Date creationTime;





}
