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
public class MedicineRecommendation    {
    @TableId(value = "medicine_recommendation_id", type = IdType.AUTO)
    @JsonProperty(value = "medicineRecommendationId")
    @Excel(name = "主键")

    private Integer medicineRecommendationId;
    @JsonProperty(value = "title")
    @Excel(name = "药品名称")
    @ExcelProperty(value = "药品名称")
    @TableField(value = "title")

    private String title;
    @JsonProperty(value = "usageGuide")
    @Excel(name = "使用指南")
    @ExcelProperty(value = "使用指南")
    @TableField(value = "usage_guide")

    private String usageGuide;
    @JsonProperty(value = "nearbyPharmacyInfo")
    @Excel(name = "附近药店信息")
    @ExcelProperty(value = "附近药店信息")
    @TableField(value = "nearby_pharmacy_info")

    private String nearbyPharmacyInfo;
    @JsonProperty(value = "creationTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "创建时间")
    @ExcelProperty(value = "创建时间")
    @TableField(value = "creation_time")

    private Date creationTime;


}
