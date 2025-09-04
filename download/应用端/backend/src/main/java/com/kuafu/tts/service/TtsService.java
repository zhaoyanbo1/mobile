package com.kuafu.tts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Service
public class TtsService {

    private static final MediaType JSON = MediaType.parse("application/json");
    private static final String OPENAI_TTS_URL = "https://api.openai.com/v1/audio/speech";
    // 统一用同一把 key；读不到再试环境变量
    @Value("${openai.apiKey:}")
    private String apiKey;

    // 如你的账户是 org/project 级别，可按需启用
    @Value("${openai.organization:}")
    private String organization;
    @Value("${openai.project:}")
    private String project;

    private final OkHttpClient http;
    private final ObjectMapper mapper = new ObjectMapper();

    public TtsService() {
        this.http = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(15))
                .readTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .build();
    }

    /**
     * 调用 OpenAI TTS，返回音频字节（mp3）
     */
    public byte[] synthesize(String text, String voice, Double speed, String emotion) {
        // 兜底从环境变量取
        String key = (apiKey == null || apiKey.isBlank())
                ? System.getenv("OPENAI_API_KEY")
                : apiKey;

        if (key == null || key.isBlank()) {
            log.error("OpenAI apiKey is empty for TTS");
            return new byte[0];
        }

        // OpenAI /audio/speech 最小可用参数
        String model = "gpt-4o-mini-tts";      // 你也可以做成可配置
        String useVoice = (voice == null || voice.isBlank()) ? "alloy" : voice;

        try {
            ObjectNode root = mapper.createObjectNode();
            root.put("model", model);
            root.put("input", text);
            root.put("voice", useVoice);
            root.put("format", "mp3");
            // OpenAI 目前未正式支持 speed/emotion 参数；如果你在上层做了分段/韵律，这里先忽略它们

            Request.Builder rb = new Request.Builder()
                    .url(OPENAI_TTS_URL)
                    .header("Authorization", "Bearer " + key)
                    .header("Content-Type", "application/json")
                    .header("Accept", "audio/mpeg")
                    .post(RequestBody.create(mapper.writeValueAsBytes(root), JSON));

            // 若你使用了 org/project 级别的访问控制，可以加上下面两个头
            if (organization != null && !organization.isBlank()) {
                rb.header("OpenAI-Organization", organization);
            }
            if (project != null && !project.isBlank()) {
                rb.header("OpenAI-Project", project);
            }

            Request req = rb.build();
            log.debug("TTS request -> model:{}, voice:{}, text.len:{}", model, useVoice, text == null ? 0 : text.length());

            try (Response resp = http.newCall(req).execute()) {
                if (!resp.isSuccessful() || resp.body() == null) {
                    String body = safeBody(resp);
                    log.error("TTS request failed: code={}, url={}, body={}",
                            resp.code(), OPENAI_TTS_URL, body);
                    return new byte[0];
                }
                return resp.body().bytes();
            }
        } catch (IOException e) {
            log.error("TTS synthesize error", e);
            return new byte[0];
        }
    }

    private static String safeBody(Response resp) {
        try {
            return resp.body() != null ? resp.body().string() : "";
        } catch (Exception ignore) {
            return "";
        }
    }
}
