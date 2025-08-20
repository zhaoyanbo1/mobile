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
 * <p>  系统设置 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("system_settings")
public class SystemSettings  {
    @TableId(value = "system_settings_id", type = IdType.AUTO)
    @JsonProperty("system_settings_id")
    
    
    
    private Integer systemSettingsId;
    @JsonProperty("user_info_user_info_id_1")
    @IsNotNullField(description = "用户ID")
    
    
    @TableField("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("reminder_volume")
    
    
    @TableField("reminder_volume")
    private Integer reminderVolume;
    @JsonProperty("font_size")
    
    
    @TableField("font_size")
    private Integer fontSize;
    @JsonProperty("questionnaire_exported")
    
    
    @TableField("questionnaire_exported")
    private Boolean questionnaireExported;





}
