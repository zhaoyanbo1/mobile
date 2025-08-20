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
 * <p>  提醒类型枚举 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("reminder_type_enum")
public class ReminderTypeEnum  {
    @TableId(value = "reminder_type_enum_id", type = IdType.AUTO)
    @JsonProperty("reminder_type_enum_id")
    
    
    
    private Integer reminderTypeEnumId;
    @JsonProperty("type_name")
    @IsNotNullField(description = "类型名称")
    
    
    @TableField("type_name")
    private String typeName;





}
