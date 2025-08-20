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
public class EmergencyContactAll  {
    @TableField(value="ec.user_info_user_info_id_1")
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "用户ID")
    private Integer userInfoUserInfoId1;
    @TableField(value="ui.password")
    @JsonProperty(value = "password")
    @Excel(name = "密码")
    private String password;
    @TableField(value="ec.emergency_contact_id")
    @JsonProperty(value = "emergencyContactId")
    @Excel(name = "主键")
    private Integer emergencyContactId;
    @TableField(value="ui.last_login_date")
    @JsonProperty(value = "lastLoginDate")
    @Excel(name = "最后登录时间")
    private Date lastLoginDate;
    @TableField(value="ec.name")
    @JsonProperty(value = "name")
    @Excel(name = "联系人姓名")
    private String name;
    @TableField(value="ec.phone_number")
    @JsonProperty(value = "phoneNumber")
    @Excel(name = "联系电话")
    private String phoneNumber;
    @TableField(value="phone_number_ui")
    @JsonProperty(value = "phoneNumberUi")
    @Excel(name = "手机号")
    private String phoneNumberUi;
    @TableField(value="ui.registration_date")
    @JsonProperty(value = "registrationDate")
    @Excel(name = "注册时间")
    private Date registrationDate;
    @TableField(value="ui.username")
    @JsonProperty(value = "username")
    @Excel(name = "用户名")
    private String username;

}
