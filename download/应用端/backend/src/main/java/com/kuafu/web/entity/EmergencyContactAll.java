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
 * <p>  紧急联系人 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyContactAll {
    @TableField(value="ui.password")
    @JsonProperty("password")
    private String password;
    @TableField(value="ec.user_info_user_info_id_1")
    @JsonProperty("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @TableField(value="ui.registration_date")
    @JsonProperty("registration_date")
    private Date registrationDate;
    @TableField(value="ec.emergency_contact_id")
    @JsonProperty("emergency_contact_id")
    private Integer emergencyContactId;
    @TableField(value="ec.name")
    @JsonProperty("name")
    private String name;
    @TableField(value="ec.phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;
    @TableField(value="ui.last_login_date")
    @JsonProperty("last_login_date")
    private Date lastLoginDate;
    @TableField(value="ui.username")
    @JsonProperty("username")
    private String username;










}
