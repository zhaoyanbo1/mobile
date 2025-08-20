package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * <p>紧急联系人-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmergencyContactAllPageVO extends PageRequest {

    @JsonProperty(value = "userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "password")
    private String password;
    @JsonProperty(value = "emergencyContactId")
    private Integer emergencyContactId;
    @JsonProperty(value = "lastLoginDate")
    private Date lastLoginDate;
    @JsonProperty(value = "name")
    private String name;
    @JsonProperty(value = "phoneNumber")
    private String phoneNumber;
    @JsonProperty(value = "phoneNumberUi")
    private String phoneNumberUi;
    @JsonProperty(value = "registrationDate")
    private Date registrationDate;
    @JsonProperty(value = "username")
    private String username;

}
