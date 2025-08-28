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

    public ChatService(OpenAIClient client) {
        this.client = client;
    }

    /** 返回 OpenAI 的流式响应，交由 Controller 写成 SSE */
    public StreamResponse<ResponseStreamEvent> streamChat(String userMessage) {
        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(model)               // 也可用 ChatModel.GPT_4_1_MINI 常量
                .input(userMessage)         // 不再需要旧的 Input.ofText(...)
                .build();
        return client.responses().createStreaming(params);
    }
}
