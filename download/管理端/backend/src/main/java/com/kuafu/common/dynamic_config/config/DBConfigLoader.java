package com.kuafu.common.dynamic_config.config;


import com.kuafu.web.entity.SystemConfig;
import com.kuafu.web.service.ISystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化加载配置
 */
@Component
@Slf4j
@DependsOn(value = {"flywayInitializer"})
public class DBConfigLoader  {

    @Autowired
    private ISystemConfigService systemConfigService;

    @Autowired
    private ConfigurableEnvironment environment;

    @PostConstruct
    public void run() {

        log.info("开始加载数据库配置...");
        List<SystemConfig> systemConfigs = systemConfigService.list();

        if (ObjectUtils.isEmpty(systemConfigs)){
            return;
        }
        Map<String, Object> props = new HashMap<>();
        for (SystemConfig p : systemConfigs) {
            props.put(p.getName(), p.getContent());
        }
        // 添加到环境中
        MapPropertySource propertySource = new MapPropertySource("dbConfig", props);

        environment.getPropertySources().addFirst(propertySource);

        log.info("结束加载数据库配置...");
    }
}

