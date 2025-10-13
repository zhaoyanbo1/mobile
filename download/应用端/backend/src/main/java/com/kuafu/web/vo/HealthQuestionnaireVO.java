package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.kuafu.common.entity.StaticResource;
import java.util.List;
import java.util.Date;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.entity.BaseEntity;
/**
 * <p>健康问卷</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthQuestionnaireVO extends BaseEntity {

    @JsonProperty("healthQuestionnaireId")
    private Integer healthQuestionnaireId;
    @JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
//    @JsonProperty("name")
//    private String name;
//    @JsonProperty("age")
//    private Integer age;
//    @JsonProperty("residence")
//    private String residence;
//    @JsonProperty("chronicDisease")
//    private String chronicDisease;
//    @JsonProperty("allergyHistory")
//    private String allergyHistory;
//    @JsonProperty("commonMedication")
//    private String commonMedication;
//    @JsonProperty("dietPreference")
//    private String dietPreference;
//    @JsonProperty("exerciseFrequency")
//    private String exerciseFrequency;
    @JsonProperty("adl")
    private Integer adl;
    @JsonProperty("mobilityOut")
    private Integer mobilityOut;
    @JsonProperty("falls")
    private Integer falls;
    @JsonProperty("weightLoss")
    private Integer weightLoss;
    @JsonProperty("diseases")
    private String diseases;
    @JsonProperty("paMinutes")
    private Integer paMinutes;
    @JsonProperty("paWillingness")
    private Integer paWillingness;
    @JsonProperty("fluVaccine")
    private Integer fluVaccine;
    @JsonProperty("polypharmacy")
    private Integer polypharmacy;
    @JsonProperty("social")
    private Integer social;
    @JsonProperty("fvServes")
    private Integer fvServes;
    @JsonProperty("totalScore")
    private Integer totalScore;
    @JsonProperty("riskLevel")
    private String riskLevel;
    @JsonProperty("answersJson")
    private String answersJson;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("creationTime")
    private Date creationTime;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("updateTime")
    private Date updateTime;
    @JsonProperty("version")
    private Integer version;

}
