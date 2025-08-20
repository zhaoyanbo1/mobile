package com.kuafu.web.entity;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuafu.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kuafu.common.domin.StaticResource;
import java.util.List;
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
@TableName("health_questionnaire")
public class HealthQuestionnaire    {
    @TableId(value = "health_questionnaire_id", type = IdType.AUTO)
    @JsonProperty(value = "healthQuestionnaireId")
    @Excel(name = "主键")

    private Integer healthQuestionnaireId;
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "用户ID")
    @ExcelProperty(value = "用户ID")
    @TableField(value = "user_info_user_info_id_1")

    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "name")
    @Excel(name = "姓名")
    @ExcelProperty(value = "姓名")
    @TableField(value = "name")

    private String name;
    @JsonProperty(value = "age")
    @Excel(name = "年龄")
    @ExcelProperty(value = "年龄")
    @TableField(value = "age")

    private Integer age;
    @JsonProperty(value = "residence")
    @Excel(name = "居住情况")
    @ExcelProperty(value = "居住情况")
    @TableField(value = "residence")

    private String residence;
    @JsonProperty(value = "chronicDisease")
    @Excel(name = "慢性病")
    @ExcelProperty(value = "慢性病")
    @TableField(value = "chronic_disease")

    private String chronicDisease;
    @JsonProperty(value = "allergyHistory")
    @Excel(name = "过敏史")
    @ExcelProperty(value = "过敏史")
    @TableField(value = "allergy_history")

    private String allergyHistory;
    @JsonProperty(value = "commonMedication")
    @Excel(name = "常用药")
    @ExcelProperty(value = "常用药")
    @TableField(value = "common_medication")

    private String commonMedication;
    @JsonProperty(value = "dietPreference")
    @Excel(name = "饮食偏好")
    @ExcelProperty(value = "饮食偏好")
    @TableField(value = "diet_preference")

    private String dietPreference;
    @JsonProperty(value = "exerciseFrequency")
    @Excel(name = "运动频率")
    @ExcelProperty(value = "运动频率")
    @TableField(value = "exercise_frequency")

    private String exerciseFrequency;
    @JsonProperty(value = "creationTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "创建时间")
    @ExcelProperty(value = "创建时间")
    @TableField(value = "creation_time")

    private Date creationTime;
    @JsonProperty(value = "updateTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "更新时间")
    @ExcelProperty(value = "更新时间")
    @TableField(value = "update_time")

    private Date updateTime;
    @JsonProperty(value = "version")
    @Excel(name = "版本号")
    @ExcelProperty(value = "版本号")
    @TableField(value = "version")

    private Integer version;


}
