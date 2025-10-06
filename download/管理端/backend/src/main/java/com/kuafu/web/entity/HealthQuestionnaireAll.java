package com.kuafu.web.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuafu.common.annotation.Excel;
import com.kuafu.common.domin.StaticResource;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kuafu.common.annotation.Excel.*;

/**
 * <p>  健康问卷 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthQuestionnaireAll  {
    @TableField(value="hq.user_info_user_info_id_1")
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "用户ID")
    private Integer userInfoUserInfoId1;
    @TableField(value="hq.health_questionnaire_id")
    @JsonProperty(value = "healthQuestionnaireId")
    @Excel(name = "主键")
    @TableField(value="hq.adl")
    @JsonProperty(value = "adl")
    @Excel(name = "ADL")
    private Integer adl;
    @TableField(value="hq.mobility_out")
    @JsonProperty(value = "mobilityOut")
    @Excel(name = "Mobility out")
    private Integer mobilityOut;
    @TableField(value="hq.falls")
    @JsonProperty(value = "falls")
    @Excel(name = "Falls")
    private Integer falls;
    @TableField(value="hq.weight_loss")
    @JsonProperty(value = "weightLoss")
    @Excel(name = "Weight loss")
    private Integer weightLoss;
    @TableField(value="hq.diseases")
    @JsonProperty(value = "diseases")
    @Excel(name = "Diseases")
    private String diseases;
    @TableField(value="hq.pa_minutes")
    @JsonProperty(value = "paMinutes")
    @Excel(name = "PA minutes")
    private Integer paMinutes;
    @TableField(value="hq.pa_willingness")
    @JsonProperty(value = "paWillingness")
    @Excel(name = "PA willingness")
    private Integer paWillingness;
    @TableField(value="hq.flu_vaccine")
    @JsonProperty(value = "fluVaccine")
    @Excel(name = "Flu vaccine")
    private Integer fluVaccine;
    @TableField(value="hq.polypharmacy")
    @JsonProperty(value = "polypharmacy")
    @Excel(name = "Polypharmacy")
    private Integer polypharmacy;
    @TableField(value="hq.social")
    @JsonProperty(value = "social")
    @Excel(name = "Social")
    private Integer social;
    @TableField(value="hq.fv_serves")
    @JsonProperty(value = "fvServes")
    @Excel(name = "FV serves")
    private Integer fvServes;
    @TableField(value="hq.total_score")
    @JsonProperty(value = "totalScore")
    @Excel(name = "Total score")
    private Integer totalScore;
    @TableField(value="hq.risk_level")
    @JsonProperty(value = "riskLevel")
    @Excel(name = "Risk level")
    private String riskLevel;
    @TableField(value="hq.answers_json")
    @JsonProperty(value = "answersJson")
    @Excel(name = "Answers")
    private String answersJson;
    private Integer healthQuestionnaireId;
    @TableField(value="ui.last_login_date")
    @JsonProperty(value = "lastLoginDate")
    @Excel(name = "最后登录时间")
    private Date lastLoginDate;
    @TableField(value="ui.phone_number")
    @JsonProperty(value = "phoneNumber")
    @Excel(name = "手机号")
    private String phoneNumber;
    @TableField(value="ui.registration_date")
    @JsonProperty(value = "registrationDate")
    @Excel(name = "注册时间")
    private Date registrationDate;
    @TableField(value="hq.version")
    @JsonProperty(value = "version")
    @Excel(name = "版本号")
    private Integer version;
    @TableField(value="hq.chronic_disease")
    @JsonProperty(value = "chronicDisease")
    @Excel(name = "慢性病")
    private String chronicDisease;
    @TableField(value="hq.common_medication")
    @JsonProperty(value = "commonMedication")
    @Excel(name = "常用药")
    private String commonMedication;
    @TableField(value="hq.exercise_frequency")
    @JsonProperty(value = "exerciseFrequency")
    @Excel(name = "运动频率")
    private String exerciseFrequency;
    @TableField(value="ui.password")
    @JsonProperty(value = "password")
    @Excel(name = "密码")
    private String password;
    @TableField(value="hq.allergy_history")
    @JsonProperty(value = "allergyHistory")
    @Excel(name = "过敏史")
    private String allergyHistory;
    @TableField(value="hq.name")
    @JsonProperty(value = "name")
    @Excel(name = "姓名")
    private String name;
    @TableField(value="hq.diet_preference")
    @JsonProperty(value = "dietPreference")
    @Excel(name = "饮食偏好")
    private String dietPreference;
    @TableField(value="hq.creation_time")
    @JsonProperty(value = "creationTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "创建时间")
    private Date creationTime;
    @TableField(value="hq.residence")
    @JsonProperty(value = "residence")
    @Excel(name = "居住情况")
    private String residence;
    @TableField(value="hq.update_time")
    @JsonProperty(value = "updateTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "更新时间")
    private Date updateTime;
    @TableField(value="hq.age")
    @JsonProperty(value = "age")
    @Excel(name = "年龄")
    private Integer age;
    @TableField(value="ui.username")
    @JsonProperty(value = "username")
    @Excel(name = "用户名")
    private String username;

}
