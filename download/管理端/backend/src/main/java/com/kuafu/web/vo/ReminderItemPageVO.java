package com.kuafu.web.vo;

import java.util.Date;

import com.kuafu.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class ReminderItemPageVO extends PageRequest {

    @JsonProperty(value = "reminderItemId")
    private Integer reminderItemId;
    @JsonProperty(value = "userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "reminderTypeEnumId")
    private Integer reminderTypeEnumId;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "description")
    private String description;
    @JsonProperty(value = "reminderTime")
    private Date reminderTime;
    @JsonProperty(value = "isCompleted")
    private Boolean isCompleted;
    @JsonProperty(value = "medicinePhotoResourceKey")
    private String medicinePhotoResourceKey;
    @JsonProperty(value = "medicineDosage")
    private String medicineDosage;
    @JsonProperty(value = "locationLatitude")
    private String locationLatitude;
    @JsonProperty(value = "locationLongitude")
    private String locationLongitude;
    @JsonProperty(value = "locationAddress")
    private String locationAddress;
    @JsonProperty(value = "dietRecipeId")
    private Integer dietRecipeId;
    @JsonProperty(value = "creationTime")
    private Date creationTime;
    @JsonProperty(value = "updateTime")
    private Date updateTime;

}
