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
public class SystemSettings    {
    @TableId(value = "system_settings_id", type = IdType.AUTO)
    @JsonProperty(value = "systemSettingsId")
    @Excel(name = "主键")

    private Integer systemSettingsId;
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "用户ID")
    @ExcelProperty(value = "用户ID")
    @TableField(value = "user_info_user_info_id_1")

    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "reminderVolume")
    @Excel(name = "提醒音量")
    @ExcelProperty(value = "提醒音量")
    @TableField(value = "reminder_volume")

    private Integer reminderVolume;
    @JsonProperty(value = "fontSize")
    @Excel(name = "字体大小")
    @ExcelProperty(value = "字体大小")
    @TableField(value = "font_size")

    private Integer fontSize;
    @JsonProperty(value = "questionnaireExported")
    @Excel(name = "问卷数据是否导出")
    @ExcelProperty(value = "问卷数据是否导出")
    @TableField(value = "questionnaire_exported")

    private Boolean questionnaireExported;


}
