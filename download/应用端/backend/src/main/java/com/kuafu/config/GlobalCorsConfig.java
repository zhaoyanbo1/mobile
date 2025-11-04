package com.kuafu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // allowed front-end origins
        config.setAllowedOrigins(List.of(
                "http://localhost",
                "http://localhost:5173",
                "capacitor://localhost",
                "http://192.168.1.201:6789"   // 你也可以加自己
        ));

        // allow cookie / token
        config.setAllowCredentials(true);

        // allow all headers
        config.addAllowedHeader("*");

        // allow all methods
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // apply to all endpoints
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
