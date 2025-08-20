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
 * <p>  系统配置 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("system_config")
public class SystemConfig  {
    @TableId(value = "id", type = IdType.AUTO)
    @JsonProperty("id")
    
    
    
    private Integer id;
    @JsonProperty("name")
    @IsNotNullField(description = "配置项名称")
    
    
    @TableField("name")
    private String name;
    @JsonProperty("chinese_name")
    
    
    @TableField("chinese_name")
    private String chineseName;
    @JsonProperty("description")
    
    
    @TableField("description")
    private String description;
    @JsonProperty("content")
    
    
    @TableField("content")
    private String content;
    @JsonProperty("remark")
    
    
    @TableField("remark")
    private String remark;
    @JsonProperty("type")
    
    
    @TableField("type")
    private String type;





}
