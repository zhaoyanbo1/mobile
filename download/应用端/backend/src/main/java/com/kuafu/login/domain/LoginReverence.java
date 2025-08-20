package com.kuafu.login.domain;

import com.kuafu.login.domain.SelectVo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum LoginReverence {

            systemConfig("系统配置","systemConfig"),
        userInfo("用户信息","userInfo"),
        healthQuestionnaire("健康问卷","healthQuestionnaire"),
        reminderItem("提醒事项","reminderItem"),
        reminderTypeEnum("提醒类型枚举","reminderTypeEnum"),
        activityRecommendation("活动推荐","activityRecommendation"),
        dietRecommendation("饮食推荐","dietRecommendation"),
        medicineRecommendation("药品推荐","medicineRecommendation"),
        emergencyContact("紧急联系人","emergencyContact"),
        systemSettings("系统设置","systemSettings"),
    ;


    private String label;
    private String value;

    LoginReverence(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public static List<SelectVo> all() {
        final LoginReverence[] values = LoginReverence.values();
        return Arrays.stream(values).map(r -> {
            return new SelectVo(r.getValue(), r.getLabel(),r.getLabel());
        }).collect(Collectors.toList());
    }
}
