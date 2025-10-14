package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;

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
public class ReminderItemAllPageVO extends BaseEntity {

    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("reminderTime")
    private Date reminderTime;
    @JsonProperty("locationAddress")
    private String locationAddress;
    @JsonProperty("priority")
    private String priority;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("creationTime")
    private Date creationTime;
    @JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("locationLatitude")
    private String locationLatitude;
    @JsonProperty("description")
    private String description;
    @JsonProperty("title")
    private String title;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("lastLoginDate")
    private Date lastLoginDate;
    @JsonProperty("isCompleted")
    private Boolean isCompleted;
    @JsonProperty("medicineDosage")
    private String medicineDosage;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("updateTime")
    private Date updateTime;
    @JsonProperty("password")
    private String password;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("registrationDate")
    private Date registrationDate;
    @JsonProperty("reminderItemId")
    private Integer reminderItemId;
    @JsonProperty("dietRecipeId")
    private Integer dietRecipeId;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("reminderTypeEnumId")
    private Integer reminderTypeEnumId;
    @JsonProperty("locationLongitude")
    private String locationLongitude;
    @JsonProperty("username")
    private String username;

}
