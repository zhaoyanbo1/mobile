package com.kuafu.llm.service;

import com.openai.client.OpenAIClient;
import com.openai.core.http.StreamResponse;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseStreamEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final OpenAIClient client;

    @Value("${openai.model:gpt-4.1-mini}")
    private String model;

    @Value("${openai.temperature:0.7}")
    private Double temperature;

    @Value("${openai.top-p:0.95}")
    private Double topP;

//    @Value("${openai.frequency-penalty:0.3}")
//    private Double frequencyPenalty;
//
//    @Value("${openai.presence-penalty:0.2}")
//    private Double presencePenalty;


    public ChatService(OpenAIClient client) {
        this.client = client;
    }

    /** 返回 OpenAI 的流式响应，交由 Controller 写成 SSE */
    public StreamResponse<ResponseStreamEvent> streamChat(String prompt) {
        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(model)               // 也可用 ChatModel.GPT_4_1_MINI 常量
                .input(prompt)         // 不再需要旧的 Input.ofText(...)
                .temperature(temperature)
                .topP(topP)
//                .frequencyPenalty(frequencyPenalty)
//                .presencePenalty(presencePenalty)
                .build();
        return client.responses().createStreaming(params);
    }
}
