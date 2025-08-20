package com.kuafu.web.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.entity.BaseEntity;
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
public class ReminderItemPageVO extends BaseEntity {

@JsonProperty("reminderItemId")
    private Integer reminderItemId;
@JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
@JsonProperty("reminderTypeEnumId")
    private Integer reminderTypeEnumId;
@JsonProperty("title")
    private String title;
@JsonProperty("description")
    private String description;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("reminderTime")
    private Date reminderTime;
@JsonProperty("isCompleted")
    private Boolean isCompleted;
@JsonProperty("medicineDosage")
    private String medicineDosage;
@JsonProperty("locationLatitude")
    private String locationLatitude;
@JsonProperty("locationLongitude")
    private String locationLongitude;
@JsonProperty("locationAddress")
    private String locationAddress;
@JsonProperty("dietRecipeId")
    private Integer dietRecipeId;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("creationTime")
    private Date creationTime;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("updateTime")
    private Date updateTime;

private List<StaticResource> medicinePhoto;

}
