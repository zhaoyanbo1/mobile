package com.kuafu.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafu.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafu.web.annotation.IsNotNullField;
import com.baomidou.mybatisplus.annotation.FieldFill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
/**
 * <p>  健康问卷 </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("health_questionnaire")
public class HealthQuestionnaire {
    @TableId(value = "health_questionnaire_id", type = IdType.AUTO)
    @JsonProperty("health_questionnaire_id")
    private Integer healthQuestionnaireId;

    @TableField("user_info_user_info_id_1")
    @JsonProperty("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;

    @TableField("adl")
    @JsonProperty("adl")
    private Integer adl;

    @TableField("mobility_out")
    @JsonProperty("mobility_out")
    private Integer mobilityOut;

    @TableField("falls")
    @JsonProperty("falls")
    private Integer falls;

    @TableField("weight_loss")
    @JsonProperty("weight_loss")
    private Integer weightLoss;

    @TableField("diseases")
    @JsonProperty("diseases")
    private String diseases;

    @TableField("pa_minutes")
    @JsonProperty("pa_minutes")
    private Integer paMinutes;

    @TableField("pa_willingness")
    @JsonProperty("pa_willingness")
    private Integer paWillingness;

    @TableField("flu_vaccine")
    @JsonProperty("flu_vaccine")
    private Integer fluVaccine;

    @TableField("polypharmacy")
    @JsonProperty("polypharmacy")
    private Integer polypharmacy;

    @TableField("social")
    @JsonProperty("social")
    private Integer social;

    @TableField("fv_serves")
    @JsonProperty("fv_serves")
    private Integer fvServes;

    @TableField("total_score")
    @JsonProperty("total_score")
    private Integer totalScore;

    @TableField("risk_level")
    @JsonProperty("risk_level")
    private String riskLevel;

    @TableField("answers_json")
    @JsonProperty("answers_json")
    private String answersJson;

    @TableField("creation_time")
    @JsonProperty("creation_time")
    private Date creationTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)

    @JsonProperty("update_time")
    private Date updateTime;

    @TableField("version")
    @JsonProperty("version")
    private Integer version;

}
