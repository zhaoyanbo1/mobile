package com.kuafu.llm.config;

import com.openai.OpenAI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Bean
    public OpenAI openAI(@Value("${openai.apiKey}") String apiKey,
                         @Value("${openai.baseUrl:https://api.openai.com}") String baseUrl) {
        return OpenAI.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)   // 默认官方地址；如用代理可改这里
                .build();
    }
}
