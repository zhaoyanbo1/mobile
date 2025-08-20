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
public class ActivityRecommendation    {
    @TableId(value = "activity_recommendation_id", type = IdType.AUTO)
    @JsonProperty(value = "activityRecommendationId")
    @Excel(name = "主键")

    private Integer activityRecommendationId;
    @JsonProperty(value = "title")
    @Excel(name = "活动标题")
    @ExcelProperty(value = "活动标题")
    @TableField(value = "title")

    private String title;
    @JsonProperty(value = "activityTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "活动时间")
    @ExcelProperty(value = "活动时间")
    @TableField(value = "activity_time")

    private Date activityTime;
    @JsonProperty(value = "locationLatitude")
    @Excel(name = "地点纬度")
    @ExcelProperty(value = "地点纬度")
    @TableField(value = "location_latitude")

    private String locationLatitude;
    @JsonProperty(value = "locationLongitude")
    @Excel(name = "地点经度")
    @ExcelProperty(value = "地点经度")
    @TableField(value = "location_longitude")

    private String locationLongitude;
    @JsonProperty(value = "locationAddress")
    @Excel(name = "地点详细地址")
    @ExcelProperty(value = "地点详细地址")
    @TableField(value = "location_address")

    private String locationAddress;
    @JsonProperty(value = "suitablePeople")
    @Excel(name = "适宜人群")
    @ExcelProperty(value = "适宜人群")
    @TableField(value = "suitable_people")

    private String suitablePeople;
    @JsonProperty(value = "creationTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "创建时间")
    @ExcelProperty(value = "创建时间")
    @TableField(value = "creation_time")

    private Date creationTime;


}
