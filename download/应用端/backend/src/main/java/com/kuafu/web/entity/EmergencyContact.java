package com.kuafu.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafu.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafu.web.annotation.IsNotNullField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
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
@TableName("emergency_contact")
public class EmergencyContact  {
    @TableId(value = "emergency_contact_id", type = IdType.AUTO)
    @JsonProperty("emergency_contact_id")
    
    
    
    private Integer emergencyContactId;
    @JsonProperty("user_info_user_info_id_1")
    @IsNotNullField(description = "用户ID")
    
    
    @TableField("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("name")
    @IsNotNullField(description = "联系人姓名")
    
    
    @TableField("name")
    private String name;
    @JsonProperty("phone_number")
    @IsNotNullField(description = "联系电话")
    
    
    @TableField("phone_number")
    private String phoneNumber;





}
