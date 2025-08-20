package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafu.common.domin.StaticResource;
import java.util.List;

/**
 * <p>提醒事项</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderItemVO  {

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

    @JsonProperty("medicinePhoto")
    private List<StaticResource> medicinePhoto;

}
