package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * <p>提醒事项-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReminderItemAllPageVO extends PageRequest {

    @JsonProperty(value = "userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "medicinePhotoResourceKey")
    private String medicinePhotoResourceKey;
    @JsonProperty(value = "locationAddress")
    private String locationAddress;
    @JsonProperty(value = "medicineDosage")
    private String medicineDosage;
    @JsonProperty(value = "lastLoginDate")
    private Date lastLoginDate;
    @JsonProperty(value = "dietRecipeId")
    private Integer dietRecipeId;
    @JsonProperty(value = "phoneNumber")
    private String phoneNumber;
    @JsonProperty(value = "description")
    private String description;
    @JsonProperty(value = "locationLongitude")
    private String locationLongitude;
    @JsonProperty(value = "registrationDate")
    private Date registrationDate;
    @JsonProperty(value = "reminderItemId")
    private Integer reminderItemId;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "reminderTime")
    private Date reminderTime;
    @JsonProperty(value = "isCompleted")
    private Boolean isCompleted;
    @JsonProperty(value = "locationLatitude")
    private String locationLatitude;
    @JsonProperty(value = "password")
    private String password;
    @JsonProperty(value = "creationTime")
    private Date creationTime;
    @JsonProperty(value = "updateTime")
    private Date updateTime;
    @JsonProperty(value = "reminderTypeEnumId")
    private Integer reminderTypeEnumId;
    @JsonProperty(value = "username")
    private String username;

}
