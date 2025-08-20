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
public class HealthQuestionnairePageVO extends BaseEntity {

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
