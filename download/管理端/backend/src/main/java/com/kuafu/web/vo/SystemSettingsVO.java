package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafu.common.domin.StaticResource;
import java.util.List;

/**
 * <p>系统设置</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingsVO  {

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
