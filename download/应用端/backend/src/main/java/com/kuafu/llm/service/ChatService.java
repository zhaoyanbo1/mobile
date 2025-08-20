package com.kuafu.llm.service;

import com.openai.OpenAI;
import com.openai.core.Stream;
import com.openai.models.Input;
import com.openai.models.Responses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final OpenAI openAI;

    @Value("${openai.model:gpt-4.1-mini}")
    private String model;

    public ChatService(OpenAI openAI) {
        this.openAI = openAI;
    }

    public Stream<Responses.StreamEvent> streamChat(String userMessage) {
        return openAI.responses()
                .stream(Responses.StreamRequest.builder()
                        .model(model)
                        .input(Input.ofText(userMessage))
                        .build());
    }
}
