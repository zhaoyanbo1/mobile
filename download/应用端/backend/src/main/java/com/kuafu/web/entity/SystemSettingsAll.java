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
public class SystemSettingsAll {
    @TableField(value="ui.password")
    @JsonProperty("password")
    private String password;
    @TableField(value="ss.user_info_user_info_id_1")
    @JsonProperty("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @TableField(value="ui.registration_date")
    @JsonProperty("registration_date")
    private Date registrationDate;
    @TableField(value="ss.reminder_volume")
    @JsonProperty("reminder_volume")
    private Integer reminderVolume;
    @TableField(value="ss.system_settings_id")
    @JsonProperty("system_settings_id")
    private Integer systemSettingsId;
    @TableField(value="ss.font_size")
    @JsonProperty("font_size")
    private Integer fontSize;
    @TableField(value="ss.questionnaire_exported")
    @JsonProperty("questionnaire_exported")
    private Boolean questionnaireExported;
    @TableField(value="ui.phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;
    @TableField(value="ui.last_login_date")
    @JsonProperty("last_login_date")
    private Date lastLoginDate;
    @TableField(value="ui.username")
    @JsonProperty("username")
    private String username;










}
