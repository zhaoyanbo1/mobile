package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.kuafu.common.entity.StaticResource;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.entity.BaseEntity;
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
public class HealthQuestionnaireVO  extends BaseEntity {

    @JsonProperty("healthQuestionnaireId")
    private Integer healthQuestionnaireId;
    @JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("name")
    private String name;
    @JsonProperty("age")
    private Integer age;
    @JsonProperty("residence")
    private String residence;
    @JsonProperty("chronicDisease")
    private String chronicDisease;
    @JsonProperty("allergyHistory")
    private String allergyHistory;
    @JsonProperty("commonMedication")
    private String commonMedication;
    @JsonProperty("dietPreference")
    private String dietPreference;
    @JsonProperty("exerciseFrequency")
    private String exerciseFrequency;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("creationTime")
    private Date creationTime;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("updateTime")
    private Date updateTime;
    @JsonProperty("version")
    private Integer version;




}
