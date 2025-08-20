package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafu.common.domin.StaticResource;
import java.util.List;

/**
 * <p>用户信息</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO  {

     @JsonProperty(value = "userInfoId")
    private Integer userInfoId;
     @JsonProperty(value = "phoneNumber")
    private String phoneNumber;
     @JsonProperty(value = "username")
    private String username;
     @JsonProperty(value = "password")
    private String password;
     @JsonProperty(value = "registrationDate")
    private Date registrationDate;
     @JsonProperty(value = "lastLoginDate")
    private Date lastLoginDate;


}
