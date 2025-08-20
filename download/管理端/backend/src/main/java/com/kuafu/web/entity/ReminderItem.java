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
 * <p>  提醒事项 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("reminder_item")
public class ReminderItem    {
    @TableId(value = "reminder_item_id", type = IdType.AUTO)
    @JsonProperty(value = "reminderItemId")
    @Excel(name = "主键")

    private Integer reminderItemId;
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "用户ID")
    @ExcelProperty(value = "用户ID")
    @TableField(value = "user_info_user_info_id_1")

    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "reminderTypeEnumId")
    @Excel(name = "提醒类型枚举ID")
    @ExcelProperty(value = "提醒类型枚举ID")
    @TableField(value = "reminder_type_enum_id")

    private Integer reminderTypeEnumId;
    @JsonProperty(value = "title")
    @Excel(name = "提醒标题")
    @ExcelProperty(value = "提醒标题")
    @TableField(value = "title")

    private String title;
    @JsonProperty(value = "description")
    @Excel(name = "提醒内容")
    @ExcelProperty(value = "提醒内容")
    @TableField(value = "description")

    private String description;
    @JsonProperty(value = "reminderTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "提醒时间")
    @ExcelProperty(value = "提醒时间")
    @TableField(value = "reminder_time")

    private Date reminderTime;
    @JsonProperty(value = "isCompleted")
    @Excel(name = "是否完成")
    @ExcelProperty(value = "是否完成")
    @TableField(value = "is_completed")

    private Boolean isCompleted;
    @JsonProperty(value = "medicinePhotoResourceKey")
    @Excel(name = "药品照片")
    @ExcelProperty(value = "药品照片")
    @TableField(value = "medicine_photo_resource_key")

    private String medicinePhotoResourceKey;
    @JsonProperty(value = "medicineDosage")
    @Excel(name = "用药量")
    @ExcelProperty(value = "用药量")
    @TableField(value = "medicine_dosage")

    private String medicineDosage;
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
    @JsonProperty(value = "dietRecipeId")
    @Excel(name = "关联食谱ID")
    @ExcelProperty(value = "关联食谱ID")
    @TableField(value = "diet_recipe_id")

    private Integer dietRecipeId;
    @JsonProperty(value = "creationTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "创建时间")
    @ExcelProperty(value = "创建时间")
    @TableField(value = "creation_time")

    private Date creationTime;
    @JsonProperty(value = "updateTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "更新时间")
    @ExcelProperty(value = "更新时间")
    @TableField(value = "update_time")

    private Date updateTime;

    @TableField(exist = false)
    private List<StaticResource> medicinePhoto;

}
