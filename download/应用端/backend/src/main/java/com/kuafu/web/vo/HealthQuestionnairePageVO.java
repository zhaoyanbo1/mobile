package com.kuafu.web.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.entity.BaseEntity;
/**
 * <p>健康问卷-分页列表-响应参数</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HealthQuestionnairePageVO extends BaseEntity {

    @JsonProperty("healthQuestionnaireId")
    private Integer healthQuestionnaireId;
    @JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
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
