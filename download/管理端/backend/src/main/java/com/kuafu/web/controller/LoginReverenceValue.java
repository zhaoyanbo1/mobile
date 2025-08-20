package com.kuafu.web.controller;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum LoginReverenceValue {
userInfo("userInfo","system_settings_all.get_user_info_list"),dynamic_api_setting("dynamic_api_setting","dynamic_api_setting.get_select_list"),activity_recommendation("activity_recommendation","activity_recommendation.get_select_list"),health_questionnaire("health_questionnaire","health_questionnaire.get_select_list"),system_config("system_config","system_config.get_select_list"),reminder_type_enum("reminder_type_enum","reminder_type_enum.get_select_list"),system_settings("system_settings","system_settings.get_select_list"),user_info("user_info","user_info.get_select_list"),reminder_item("reminder_item","reminder_item.get_select_list"),diet_recommendation("diet_recommendation","diet_recommendation.get_select_list"),medicine_recommendation("medicine_recommendation","medicine_recommendation.get_select_list"),emergency_contact("emergency_contact","emergency_contact.get_select_list"),;
    private String label;
    private String value;


    LoginReverenceValue(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static String getValue(String label) {
        for (LoginReverenceValue item : values()) {
            if (StringUtils.equalsIgnoreCase(item.getLabel(), label)) {
                return item.getValue();
            }
        }
        return null;
    }
}
