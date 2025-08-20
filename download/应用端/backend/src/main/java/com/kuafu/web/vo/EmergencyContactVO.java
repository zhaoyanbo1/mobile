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
 * <p>紧急联系人</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/08/19 20:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactVO  extends BaseEntity {

    @JsonProperty("emergencyContactId")
    private Integer emergencyContactId;
    @JsonProperty("userInfoUserInfoId1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("name")
    private String name;
    @JsonProperty("phoneNumber")
    private String phoneNumber;




}
