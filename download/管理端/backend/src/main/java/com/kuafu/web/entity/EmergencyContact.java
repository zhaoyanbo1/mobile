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
public class EmergencyContact    {
    @TableId(value = "emergency_contact_id", type = IdType.AUTO)
    @JsonProperty(value = "emergencyContactId")
    @Excel(name = "主键")

    private Integer emergencyContactId;
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "用户ID")
    @ExcelProperty(value = "用户ID")
    @TableField(value = "user_info_user_info_id_1")

    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "name")
    @Excel(name = "联系人姓名")
    @ExcelProperty(value = "联系人姓名")
    @TableField(value = "name")

    private String name;
    @JsonProperty(value = "phoneNumber")
    @Excel(name = "联系电话")
    @ExcelProperty(value = "联系电话")
    @TableField(value = "phone_number")

    private String phoneNumber;


}
