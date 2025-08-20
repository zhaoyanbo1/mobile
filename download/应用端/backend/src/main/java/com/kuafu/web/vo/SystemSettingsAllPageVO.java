package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;

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
public class SystemSettingsAllPageVO extends BaseEntity {

@JsonProperty("password")
    private String password;
@JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("registrationDate")
    private Date registrationDate;
@JsonProperty("reminderVolume")
    private Integer reminderVolume;
@JsonProperty("systemSettingsId")
    private Integer systemSettingsId;
@JsonProperty("fontSize")
    private Integer fontSize;
@JsonProperty("questionnaireExported")
    private Boolean questionnaireExported;
@JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("lastLoginDate")
    private Date lastLoginDate;
@JsonProperty("username")
    private String username;

}
