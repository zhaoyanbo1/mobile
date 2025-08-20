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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
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
@TableName("health_questionnaire")
public class HealthQuestionnaire  {
    @TableId(value = "health_questionnaire_id", type = IdType.AUTO)
    @JsonProperty("health_questionnaire_id")
    
    
    
    private Integer healthQuestionnaireId;
    @JsonProperty("user_info_user_info_id_1")
    @IsNotNullField(description = "用户ID")
    
    
    @TableField("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("name")
    @IsNotNullField(description = "姓名")
    
    
    @TableField("name")
    private String name;
    @JsonProperty("age")
    @IsNotNullField(description = "年龄")
    
    
    @TableField("age")
    private Integer age;
    @JsonProperty("residence")
    
    
    @TableField("residence")
    private String residence;
    @JsonProperty("chronic_disease")
    
    
    @TableField("chronic_disease")
    private String chronicDisease;
    @JsonProperty("allergy_history")
    
    
    @TableField("allergy_history")
    private String allergyHistory;
    @JsonProperty("common_medication")
    
    
    @TableField("common_medication")
    private String commonMedication;
    @JsonProperty("diet_preference")
    
    
    @TableField("diet_preference")
    private String dietPreference;
    @JsonProperty("exercise_frequency")
    
    
    @TableField("exercise_frequency")
    private String exerciseFrequency;
    @JsonProperty("creation_time")
    
    
    @TableField("creation_time")
    private Date creationTime;
    @JsonProperty("update_time")
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    
    private Date updateTime;
    @JsonProperty("version")
    
    
    @TableField("version")
    private Integer version;





}
