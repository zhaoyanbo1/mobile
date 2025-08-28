package com.kuafu.api.controller;

import com.kuafu.llm.service.ChatService;
import com.openai.core.http.StreamResponse;
import com.openai.models.responses.ResponseStreamEvent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping("/api/llm")
@CrossOrigin
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void stream(@RequestParam("q") String q, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");

        PrintWriter writer = resp.getWriter();

        try (StreamResponse<ResponseStreamEvent> stream = chatService.streamChat(q)) {
            stream.stream().forEach(event -> {
                // 只取文本增量事件并逐段写出
                event.outputTextDelta().ifPresent(delta -> {
                    String chunk = delta.delta().replace("\n", "\\n");
                    writer.write("data: " + chunk + "\n\n");
                    writer.flush();
                });
            });
            writer.write("data: [DONE]\n\n"); // 结束标记
            writer.flush();
        } catch (Exception e) {
            writer.write("event: error\n");
            writer.write("data: " + e.getMessage() + "\n\n");
            writer.flush();
        }
    }
}
