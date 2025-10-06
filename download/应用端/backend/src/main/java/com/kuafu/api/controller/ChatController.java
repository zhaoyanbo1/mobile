package com.kuafu.api.controller;

import com.kuafu.api.dto.ChatRequest;
import com.kuafu.llm.model.ChatMessage;
import com.kuafu.llm.service.ChatService;
import com.kuafu.llm.service.ConversationService;
import com.openai.core.http.StreamResponse;
import com.openai.models.responses.ResponseStreamEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Chat API that keeps conversation context by conversationId.
 */
@RestController
//@RequestMapping("/api/llm")
@RequestMapping("/api")
@CrossOrigin
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;
    private final ConversationService conversationService;

//    public ChatController(ChatService chatService) {
//        this.chatService = chatService;
//    }
    public ChatController(ChatService chatService, ConversationService conversationService) {
        this.chatService = chatService;
        this.conversationService = conversationService;
    }

//    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public void stream(@RequestParam("q") String q, HttpServletResponse resp) throws IOException {
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void stream(@RequestBody ChatRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");

        PrintWriter writer = resp.getWriter();

//        try (StreamResponse<ResponseStreamEvent> stream = chatService.streamChat(q)) {
        String conversationId = req.getConversationId();
        if (!StringUtils.hasText(conversationId)) {
            conversationId = conversationService.newConversation();
        }

        // merge history from client and current question
        conversationService.addMessages(conversationId, req.getMessages());
        ChatMessage userMsg = new ChatMessage();
        userMsg.setRole("user");
        userMsg.setContent(req.getQuery());
        conversationService.addMessage(conversationId, userMsg);

        // send conversationId event so client can persist
        writer.write("event: conversation\n");
        writer.write("data: " + conversationId + "\n\n");
        writer.flush();

        var messages = conversationService.getMessages(conversationId);
        logConversationState(conversationId, messages);

        String prompt = conversationService.buildPrompt(conversationId);

        StringBuilder answerBuilder = new StringBuilder();
        try (StreamResponse<ResponseStreamEvent> stream = chatService.streamChat(prompt)) {
            stream.stream().forEach(event -> {
                // 只取文本增量事件并逐段写出
                event.outputTextDelta().ifPresent(delta -> {
                    String chunk = delta.delta().replace("\n", "\\n");
                    answerBuilder.append(delta.delta());
                    writer.write("event: message\n");
                    writer.write("data: " + chunk + "\n\n");
                    writer.flush();
                });
            });
            //writer.write("data: [DONE]\n\n"); // 结束标记
            writer.write("event: done\n");
            writer.write("data: [DONE]\n\n");
            writer.flush();
        } catch (Exception e) {
            writer.write("event: error\n");
            writer.write("data: " + e.getMessage() + "\n\n");
            writer.flush();
        }

        ChatMessage assistantMsg = new ChatMessage();
        assistantMsg.setRole("assistant");
        assistantMsg.setContent(answerBuilder.toString());
        conversationService.addMessage(conversationId, assistantMsg);
    }

    private void logConversationState(String conversationId, java.util.List<ChatMessage> messages) {
        if (!log.isDebugEnabled()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[conversation=").append(conversationId).append("] [stage=pre-request] ");
        int index = 0;
        for (ChatMessage message : messages) {
            sb.append("#").append(index++)
                    .append(" role=").append(message.getRole());
            if (StringUtils.hasText(message.getName())) {
                sb.append(" name=").append(message.getName());
            }
            sb.append(" content=")
                    .append(message.getContent() == null ? "" : message.getContent().replace('\n', ' '))
                    .append(" | ");
        }
        log.debug(sb.toString());
    }
}
