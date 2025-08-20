package com.kuafu.web.entity;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuafu.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kuafu.common.domin.StaticResource;
import java.util.List;
import com.kuafu.common.annotation.Excel.*;



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
public class DietRecommendation    {
    @TableId(value = "diet_recommendation_id", type = IdType.AUTO)
    @JsonProperty(value = "dietRecommendationId")
    @Excel(name = "主键")

    private Integer dietRecommendationId;
    @JsonProperty(value = "title")
    @Excel(name = "食谱标题")
    @ExcelProperty(value = "食谱标题")
    @TableField(value = "title")

    private String title;
    @JsonProperty(value = "difficulty")
    @Excel(name = "烹饪难度")
    @ExcelProperty(value = "烹饪难度")
    @TableField(value = "difficulty")

    private String difficulty;
    @JsonProperty(value = "requiredTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "所需时间")
    @ExcelProperty(value = "所需时间")
    @TableField(value = "required_time")

    private String requiredTime;
    @JsonProperty(value = "creationTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "创建时间")
    @ExcelProperty(value = "创建时间")
    @TableField(value = "creation_time")

    private Date creationTime;


}
