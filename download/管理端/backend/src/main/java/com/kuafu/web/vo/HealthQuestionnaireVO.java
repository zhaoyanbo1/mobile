package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafu.common.domin.StaticResource;
import java.util.List;

/**
 * <p>健康问卷</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthQuestionnaireVO  {

     @JsonProperty(value = "healthQuestionnaireId")
    private Integer healthQuestionnaireId;
     @JsonProperty(value = "userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
     @JsonProperty(value = "name")
    private String name;
     @JsonProperty(value = "age")
    private Integer age;
     @JsonProperty(value = "residence")
    private String residence;
     @JsonProperty(value = "chronicDisease")
    private String chronicDisease;
     @JsonProperty(value = "allergyHistory")
    private String allergyHistory;
     @JsonProperty(value = "commonMedication")
    private String commonMedication;
     @JsonProperty(value = "dietPreference")
    private String dietPreference;
     @JsonProperty(value = "exerciseFrequency")
    private String exerciseFrequency;
     @JsonProperty(value = "creationTime")
    private Date creationTime;
     @JsonProperty(value = "updateTime")
    private Date updateTime;
     @JsonProperty(value = "version")
    private Integer version;


}
