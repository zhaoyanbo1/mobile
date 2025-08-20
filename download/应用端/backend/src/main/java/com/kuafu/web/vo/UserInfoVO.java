package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.kuafu.common.entity.StaticResource;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.entity.BaseEntity;
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
public class UserInfoVO  extends BaseEntity {

    @JsonProperty("userInfoId")
    private Integer userInfoId;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("registrationDate")
    private Date registrationDate;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("lastLoginDate")
    private Date lastLoginDate;




}
