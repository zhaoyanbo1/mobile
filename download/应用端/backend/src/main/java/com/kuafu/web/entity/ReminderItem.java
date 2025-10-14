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
public class ReminderItem  {
    @TableId(value = "reminder_item_id", type = IdType.AUTO)
    @JsonProperty("reminder_item_id")



    private Integer reminderItemId;
    @JsonProperty("user_info_user_info_id_1")
    @IsNotNullField(description = "用户ID")


    @TableField("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("reminder_type_enum_id")
    @IsNotNullField(description = "提醒类型枚举ID")


    @TableField("reminder_type_enum_id")
    private Integer reminderTypeEnumId;
    @JsonProperty("title")
    @IsNotNullField(description = "提醒标题")


    @TableField("title")
    private String title;
    @JsonProperty("description")


    @TableField("description")
    private String description;
    @JsonProperty("reminder_time")
    @IsNotNullField(description = "提醒时间")


    @TableField("reminder_time")
    private Date reminderTime;
    @JsonProperty("is_completed")
    @IsNotNullField(description = "是否完成")


    @TableField("is_completed")
    private Boolean isCompleted;
    @JsonProperty("medicine_dosage")


    @TableField("medicine_dosage")
    private String medicineDosage;
    @JsonProperty("location_latitude")


    @TableField("location_latitude")
    private String locationLatitude;
    @JsonProperty("location_longitude")


    @TableField("location_longitude")
    private String locationLongitude;
    @JsonProperty("location_address")


    @TableField("location_address")
    private String locationAddress;
    @JsonProperty("priority")


    @TableField("priority")
    private String priority;
    @JsonProperty("diet_recipe_id")


    @TableField("diet_recipe_id")
    private Integer dietRecipeId;
    @JsonProperty("creation_time")


    @TableField("creation_time")
    private Date creationTime;
    @JsonProperty("update_time")

    @TableField(fill = FieldFill.INSERT_UPDATE)

    private Date updateTime;



    @TableField(exist = false)
    @JsonProperty("medicine_photo")
    private List<StaticResource> medicinePhoto;



}
