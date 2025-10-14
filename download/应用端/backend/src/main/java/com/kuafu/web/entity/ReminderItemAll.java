package com.kuafu.web.entity;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



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
public class ReminderItemAll {
    @TableField(value="ri.reminder_time")
    @JsonProperty("reminder_time")
    private Date reminderTime;
    @TableField(value="ri.location_address")
    @JsonProperty("location_address")
    private String locationAddress;
    @TableField(value="ri.priority")
    @JsonProperty("priority")
    private String priority;
    @TableField(value="ri.creation_time")
    @JsonProperty("creation_time")
    private Date creationTime;
    @TableField(value="ri.user_info_user_info_id_1")
    @JsonProperty("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @TableField(value="ri.location_latitude")
    @JsonProperty("location_latitude")
    private String locationLatitude;
    @TableField(value="ri.description")
    @JsonProperty("description")
    private String description;
    @TableField(value="ri.title")
    @JsonProperty("title")
    private String title;
    @TableField(value="ui.last_login_date")
    @JsonProperty("last_login_date")
    private Date lastLoginDate;
    @TableField(value="ri.is_completed")
    @JsonProperty("is_completed")
    private Boolean isCompleted;
    @TableField(value="ri.medicine_dosage")
    @JsonProperty("medicine_dosage")
    private String medicineDosage;
    @TableField(value="ri.update_time")
    @JsonProperty("update_time")
    private Date updateTime;
    @TableField(value="ui.password")
    @JsonProperty("password")
    private String password;
    @TableField(value="ui.registration_date")
    @JsonProperty("registration_date")
    private Date registrationDate;
    @TableField(value="ri.reminder_item_id")
    @JsonProperty("reminder_item_id")
    private Integer reminderItemId;
    @TableField(value="ri.diet_recipe_id")
    @JsonProperty("diet_recipe_id")
    private Integer dietRecipeId;
    @TableField(value="ui.phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;
    @TableField(value="ri.reminder_type_enum_id")
    @JsonProperty("reminder_type_enum_id")
    private Integer reminderTypeEnumId;
    @TableField(value="ri.location_longitude")
    @JsonProperty("location_longitude")
    private String locationLongitude;
    @TableField(value="ui.username")
    @JsonProperty("username")
    private String username;


    @TableField(value="medicine_photo")
    @JsonProperty("medicine_photo")
    private List<StaticResource> medicinePhoto;








}
