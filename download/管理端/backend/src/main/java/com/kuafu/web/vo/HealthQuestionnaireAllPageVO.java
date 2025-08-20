package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * <p>健康问卷-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HealthQuestionnaireAllPageVO extends PageRequest {

    @JsonProperty(value = "userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "healthQuestionnaireId")
    private Integer healthQuestionnaireId;
    @JsonProperty(value = "lastLoginDate")
    private Date lastLoginDate;
    @JsonProperty(value = "phoneNumber")
    private String phoneNumber;
    @JsonProperty(value = "registrationDate")
    private Date registrationDate;
    @JsonProperty(value = "version")
    private Integer version;
    @JsonProperty(value = "chronicDisease")
    private String chronicDisease;
    @JsonProperty(value = "commonMedication")
    private String commonMedication;
    @JsonProperty(value = "exerciseFrequency")
    private String exerciseFrequency;
    @JsonProperty(value = "password")
    private String password;
    @JsonProperty(value = "allergyHistory")
    private String allergyHistory;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "dietPreference")
    private String dietPreference;
    @JsonProperty(value = "creationTime")
    private Date creationTime;
    @JsonProperty(value = "residence")
    private String residence;
    @JsonProperty(value = "updateTime")
    private Date updateTime;
    @JsonProperty(value = "age")
    private Integer age;
    @JsonProperty(value = "username")
    private String username;

}
