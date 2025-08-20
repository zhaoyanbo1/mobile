package com.kuafu.api.controller;

import com.openai.core.Stream;
import com.openai.models.Responses;
import com.kuafu.llm.service.ChatService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/llm")
@CrossOrigin // 生产请改为白名单域名
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) { this.chatService = chatService; }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void stream(@RequestParam("q") String q, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        var writer = response.getWriter();
        try (Stream<Responses.StreamEvent> stream = chatService.streamChat(q)) {
            for (Responses.StreamEvent event : stream) {
                if (event instanceof Responses.StreamEvent.OutputTextDelta delta) {
                    writer.write("data: " + delta.delta() + "\n\n"); // SSE
                    writer.flush();
                }
                if (event instanceof Responses.StreamEvent.Completed) {
                    writer.write("data: [DONE]\n\n");
                    writer.flush();
                }
            }
        } catch (Exception e) {
            writer.write("event: error\n");
            writer.write("data: " + e.getMessage() + "\n\n");
            writer.flush();
        }
    }
}
