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
 * <p>健康问卷-分页列表-响应参数</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HealthQuestionnaireAllPageVO extends BaseEntity {

    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("creationTime")
    private Date creationTime;
@JsonProperty("healthQuestionnaireId")
    private Integer healthQuestionnaireId;
@JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
@JsonProperty("allergyHistory")
    private String allergyHistory;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("lastLoginDate")
    private Date lastLoginDate;
@JsonProperty("version")
    private Integer version;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("updateTime")
    private Date updateTime;
@JsonProperty("password")
    private String password;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("registrationDate")
    private Date registrationDate;
@JsonProperty("commonMedication")
    private String commonMedication;
@JsonProperty("name")
    private String name;
@JsonProperty("exerciseFrequency")
    private String exerciseFrequency;
@JsonProperty("phoneNumber")
    private String phoneNumber;
@JsonProperty("residence")
    private String residence;
@JsonProperty("chronicDisease")
    private String chronicDisease;
@JsonProperty("age")
    private Integer age;
@JsonProperty("dietPreference")
    private String dietPreference;
@JsonProperty("username")
    private String username;

}
