package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafu.common.domin.StaticResource;
import java.util.List;

/**
 * <p>提醒类型枚举</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderTypeEnumVO  {

     @JsonProperty(value = "reminderTypeEnumId")
    private Integer reminderTypeEnumId;
     @JsonProperty(value = "typeName")
    private String typeName;


}
