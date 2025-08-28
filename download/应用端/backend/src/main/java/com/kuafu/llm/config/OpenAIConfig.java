package com.kuafu.llm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;

@Configuration
public class OpenAIConfig {

    @Bean
    public OpenAIClient openAIClient(
            @Value("${openai.apiKey}") String apiKey,
            @Value("${openai.baseUrl:https://api.openai.com/v1}") String baseUrl // 可留空，默认 https://api.openai.com/v1
    ) {
        OpenAIOkHttpClient.Builder builder = OpenAIOkHttpClient.builder().apiKey(apiKey);
        if (baseUrl != null && !baseUrl.trim().isEmpty()) {
            String url = baseUrl.endsWith("/v1")
                    ? baseUrl
                    : (baseUrl.endsWith("/") ? baseUrl + "v1" : baseUrl + "/v1");
            builder.baseUrl(url);
        }
        return builder.build();
    }
}
