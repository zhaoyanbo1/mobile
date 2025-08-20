package com.kuafu.web.vo;

import java.util.Date;

import com.kuafu.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>系统设置-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SystemSettingsPageVO extends PageRequest {

    @JsonProperty(value = "systemSettingsId")
    private Integer systemSettingsId;
    @JsonProperty(value = "userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "reminderVolume")
    private Integer reminderVolume;
    @JsonProperty(value = "fontSize")
    private Integer fontSize;
    @JsonProperty(value = "questionnaireExported")
    private Boolean questionnaireExported;

}
