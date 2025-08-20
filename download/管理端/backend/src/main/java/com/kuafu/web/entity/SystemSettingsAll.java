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
public class SystemSettingsAll  {
    @TableField(value="ss.user_info_user_info_id_1")
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "用户ID")
    private Integer userInfoUserInfoId1;
    @TableField(value="ui.password")
    @JsonProperty(value = "password")
    @Excel(name = "密码")
    private String password;
    @TableField(value="ss.reminder_volume")
    @JsonProperty(value = "reminderVolume")
    @Excel(name = "提醒音量")
    private Integer reminderVolume;
    @TableField(value="ui.last_login_date")
    @JsonProperty(value = "lastLoginDate")
    @Excel(name = "最后登录时间")
    private Date lastLoginDate;
    @TableField(value="ui.phone_number")
    @JsonProperty(value = "phoneNumber")
    @Excel(name = "手机号")
    private String phoneNumber;
    @TableField(value="ss.font_size")
    @JsonProperty(value = "fontSize")
    @Excel(name = "字体大小")
    private Integer fontSize;
    @TableField(value="ss.system_settings_id")
    @JsonProperty(value = "systemSettingsId")
    @Excel(name = "主键")
    private Integer systemSettingsId;
    @TableField(value="ui.registration_date")
    @JsonProperty(value = "registrationDate")
    @Excel(name = "注册时间")
    private Date registrationDate;
    @TableField(value="ss.questionnaire_exported")
    @JsonProperty(value = "questionnaireExported")
    @Excel(name = "问卷数据是否导出")
    private Boolean questionnaireExported;
    @TableField(value="ui.username")
    @JsonProperty(value = "username")
    @Excel(name = "用户名")
    private String username;

}
