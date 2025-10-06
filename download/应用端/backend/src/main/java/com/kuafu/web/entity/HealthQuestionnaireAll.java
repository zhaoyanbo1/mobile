package com.kuafu.web.entity;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * <p>  健康问卷 </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthQuestionnaireAll {
    @TableField(value="hq.creation_time")
    @JsonProperty("creation_time")
    private Date creationTime;
    @TableField(value="hq.health_questionnaire_id")
    @JsonProperty("health_questionnaire_id")
    private Integer healthQuestionnaireId;
    @TableField(value="hq.user_info_user_info_id_1")
    @JsonProperty("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @TableField(value="hq.adl")
    @JsonProperty("adl")
    private Integer adl;
    @TableField(value="hq.mobility_out")
    @JsonProperty("mobility_out")
    private Integer mobilityOut;
    @TableField(value="hq.falls")
    @JsonProperty("falls")
    private Integer falls;
    @TableField(value="hq.weight_loss")
    @JsonProperty("weight_loss")
    private Integer weightLoss;
    @TableField(value="hq.diseases")
    @JsonProperty("diseases")
    private String diseases;
    @TableField(value="hq.pa_minutes")
    @JsonProperty("pa_minutes")
    private Integer paMinutes;
    @TableField(value="hq.pa_willingness")
    @JsonProperty("pa_willingness")
    private Integer paWillingness;
    @TableField(value="hq.flu_vaccine")
    @JsonProperty("flu_vaccine")
    private Integer fluVaccine;
    @TableField(value="hq.polypharmacy")
    @JsonProperty("polypharmacy")
    private Integer polypharmacy;
    @TableField(value="hq.social")
    @JsonProperty("social")
    private Integer social;
    @TableField(value="hq.fv_serves")
    @JsonProperty("fv_serves")
    private Integer fvServes;
    @TableField(value="hq.total_score")
    @JsonProperty("total_score")
    private Integer totalScore;
    @TableField(value="hq.risk_level")
    @JsonProperty("risk_level")
    private String riskLevel;
    @TableField(value="hq.answers_json")
    @JsonProperty("answers_json")
    private String answersJson;
    @TableField(value="hq.allergy_history")
    @JsonProperty("allergy_history")
    private String allergyHistory;
    @TableField(value="ui.last_login_date")
    @JsonProperty("last_login_date")
    private Date lastLoginDate;
    @TableField(value="hq.version")
    @JsonProperty("version")
    private Integer version;
    @TableField(value="hq.update_time")
    @JsonProperty("update_time")
    private Date updateTime;
    @TableField(value="ui.password")
    @JsonProperty("password")
    private String password;
    @TableField(value="ui.registration_date")
    @JsonProperty("registration_date")
    private Date registrationDate;
    @TableField(value="hq.common_medication")
    @JsonProperty("common_medication")
    private String commonMedication;
    @TableField(value="hq.name")
    @JsonProperty("name")
    private String name;
    @TableField(value="hq.exercise_frequency")
    @JsonProperty("exercise_frequency")
    private String exerciseFrequency;
    @TableField(value="ui.phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;
    @TableField(value="hq.residence")
    @JsonProperty("residence")
    private String residence;
    @TableField(value="hq.chronic_disease")
    @JsonProperty("chronic_disease")
    private String chronicDisease;
    @TableField(value="hq.age")
    @JsonProperty("age")
    private Integer age;
    @TableField(value="hq.diet_preference")
    @JsonProperty("diet_preference")
    private String dietPreference;
    @TableField(value="ui.username")
    @JsonProperty("username")
    private String username;










}
