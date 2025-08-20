package com.kuafu.web.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuafu.common.annotation.Excel;
import com.kuafu.common.domin.StaticResource;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class ReminderItemAll  {
    @TableField(value="ri.user_info_user_info_id_1")
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "用户ID")
    private Integer userInfoUserInfoId1;
    @TableField(value="ri.medicine_photo_resource_key")
    @JsonProperty(value = "medicinePhotoResourceKey")
    @Excel(name = "药品照片")
    private String medicinePhotoResourceKey;
    @TableField(value="ri.location_address")
    @JsonProperty(value = "locationAddress")
    @Excel(name = "地点详细地址")
    private String locationAddress;
    @TableField(value="ri.medicine_dosage")
    @JsonProperty(value = "medicineDosage")
    @Excel(name = "用药量")
    private String medicineDosage;
    @TableField(value="ui.last_login_date")
    @JsonProperty(value = "lastLoginDate")
    @Excel(name = "最后登录时间")
    private Date lastLoginDate;
    @TableField(value="ri.diet_recipe_id")
    @JsonProperty(value = "dietRecipeId")
    @Excel(name = "关联食谱ID")
    private Integer dietRecipeId;
    @TableField(value="ui.phone_number")
    @JsonProperty(value = "phoneNumber")
    @Excel(name = "手机号")
    private String phoneNumber;
    @TableField(value="ri.description")
    @JsonProperty(value = "description")
    @Excel(name = "提醒内容")
    private String description;
    @TableField(value="ri.location_longitude")
    @JsonProperty(value = "locationLongitude")
    @Excel(name = "地点经度")
    private String locationLongitude;
    @TableField(value="ui.registration_date")
    @JsonProperty(value = "registrationDate")
    @Excel(name = "注册时间")
    private Date registrationDate;
    @TableField(value="ri.reminder_item_id")
    @JsonProperty(value = "reminderItemId")
    @Excel(name = "主键")
    private Integer reminderItemId;
    @TableField(value="ri.title")
    @JsonProperty(value = "title")
    @Excel(name = "提醒标题")
    private String title;
    @TableField(value="ri.reminder_time")
    @JsonProperty(value = "reminderTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "提醒时间")
    private Date reminderTime;
    @TableField(value="ri.is_completed")
    @JsonProperty(value = "isCompleted")
    @Excel(name = "是否完成")
    private Boolean isCompleted;
    @TableField(value="ri.location_latitude")
    @JsonProperty(value = "locationLatitude")
    @Excel(name = "地点纬度")
    private String locationLatitude;
    @TableField(value="ui.password")
    @JsonProperty(value = "password")
    @Excel(name = "密码")
    private String password;
    @TableField(value="ri.creation_time")
    @JsonProperty(value = "creationTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "创建时间")
    private Date creationTime;
    @TableField(value="ri.update_time")
    @JsonProperty(value = "updateTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "更新时间")
    private Date updateTime;
    @TableField(value="ri.reminder_type_enum_id")
    @JsonProperty(value = "reminderTypeEnumId")
    @Excel(name = "提醒类型枚举ID")
    private Integer reminderTypeEnumId;
    @TableField(value="ui.username")
    @JsonProperty(value = "username")
    @Excel(name = "用户名")
    private String username;

    @TableField(value="medicine_photo")
    @JsonProperty("medicinePhoto")
    private List<StaticResource> medicinePhoto;
}
