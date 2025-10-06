package com.kuafu.llm.service;

import com.kuafu.llm.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory conversation storage. Later can be swapped to Redis.
 */
@Service
public class ConversationService {
    private static final Logger log = LoggerFactory.getLogger(ConversationService.class);

    /** maximum number of non-system messages to retain */
    private static final int MAX_HISTORY = 32;
    private static final int MAX_CONVERSATIONS = 1000;
    private static final String SYSTEM_ROLE = "system";
    private static final String SYSTEM_NAME = "livewell-v1";
    private static final String SYSTEM_PROMPT = String.join("\n",
            "You are LiveWell Coach, a supportive guide for adults aged 65+.",
            "Core priorities:",
            "1. Keep language warm, calm, and easy to follow with short sentences and clear pacing.",
            "2. Ask only one question at a time and invite gentle reflection.",
            "3. Offer practical wellness, habit, memory, lifestyle, simple tech, and emotional support suggestions.",
            "4. Avoid medical, legal, and financial advice; redirect to trusted professionals when asked.",
            "5. If a user explicitly requests a direct response, reply plainly without the softened tone.",
            "Always close with an encouraging note."
    );

    private final Map<String, Deque<ChatMessage>> store = Collections.synchronizedMap(
            new LinkedHashMap<String, Deque<ChatMessage>>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, Deque<ChatMessage>> eldest) {
                    return size() > MAX_CONVERSATIONS;
                }
            }
    );

    /** create a new conversation id */
    public synchronized String newConversation() {
        String id = UUID.randomUUID().toString();
        store.put(id, createDequeWithSystemMessage());
        return id;
    }

    /** reset conversation */
    public synchronized void reset(String conversationId) {

        if (!StringUtils.hasText(conversationId)) {
            return;
        }
        store.put(conversationId, createDequeWithSystemMessage());
    }

    /** append single message */
    public synchronized void addMessage(String conversationId, ChatMessage message) {
        if (!StringUtils.hasText(conversationId) || message == null) {
            return;
        }
//        Deque<ChatMessage> deque = store.computeIfAbsent(conversationId, k -> new ConcurrentLinkedDeque<>());
//        deque.addLast(message);
//        while (deque.size() > MAX_HISTORY) {
//            deque.removeFirst();
        Deque<ChatMessage> deque = store.computeIfAbsent(conversationId, k -> createDequeWithSystemMessage());

        if (SYSTEM_ROLE.equals(message.getRole())) {
            log.debug("Ignoring attempt to append system message to conversation {}", conversationId);
            return;
        }

        ChatMessage stored = copyMessage(message);
        deque.addLast(stored);
        trimHistory(conversationId, deque);
    }

    /** append list of messages */
    public synchronized void addMessages(String conversationId, List<ChatMessage> messages) {
        if (messages == null) {
            return;
        }
        for (ChatMessage m : messages) {
            addMessage(conversationId, m);
        }
    }

    /** get copy of messages */
    public synchronized List<ChatMessage> getMessages(String conversationId) {
        Deque<ChatMessage> deque = store.get(conversationId);
        if (deque == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(deque);
    }

    /** build text prompt from conversation history */
    public synchronized String buildPrompt(String conversationId) {
        StringBuilder sb = new StringBuilder();
        for (ChatMessage m : getMessages(conversationId)) {
            //sb.append(m.getRole()).append(": ").append(m.getContent()).append("\n");
            if (!StringUtils.hasText(m.getContent())) {
                continue;
            }
            if (StringUtils.hasText(m.getName())) {
                sb.append(m.getRole()).append("(").append(m.getName()).append(")");
            } else {
                sb.append(m.getRole());
            }
            sb.append(": ").append(m.getContent()).append("\n");
        }
        return sb.toString();
    }
    private Deque<ChatMessage> createDequeWithSystemMessage() {
        Deque<ChatMessage> deque = new ConcurrentLinkedDeque<>();
        deque.add(createSystemMessage());
        return deque;
    }

    private ChatMessage createSystemMessage() {
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole(SYSTEM_ROLE);
        systemMessage.setName(SYSTEM_NAME);
        systemMessage.setContent(SYSTEM_PROMPT);
        return systemMessage;
    }

    private ChatMessage copyMessage(ChatMessage source) {
        ChatMessage copy = new ChatMessage();
        copy.setRole(source.getRole());
        copy.setName(source.getName());
        copy.setContent(source.getContent());
        return copy;
    }

    private void trimHistory(String conversationId, Deque<ChatMessage> deque) {
        int nonSystemCount = (int) deque.stream()
                .filter(message -> !SYSTEM_ROLE.equals(message.getRole()))
                .count();

        if (nonSystemCount <= MAX_HISTORY) {
            logHistory(conversationId, deque, "no-trim");
            return;
        }

        Iterator<ChatMessage> iterator = deque.iterator();
        if (iterator.hasNext()) {
            ChatMessage first = iterator.next();
            if (!SYSTEM_ROLE.equals(first.getRole())) {
                iterator.remove();
                nonSystemCount--;
            }
        }

        while (nonSystemCount > MAX_HISTORY && iterator.hasNext()) {
            ChatMessage message = iterator.next();
            if (SYSTEM_ROLE.equals(message.getRole())) {
                continue;
            }
            iterator.remove();
            nonSystemCount--;
        }

        // ensure system message stays at the front
        ChatMessage systemMessage = deque.peekFirst();
        if (systemMessage == null || !SYSTEM_ROLE.equals(systemMessage.getRole())) {
            deque.removeIf(message -> SYSTEM_ROLE.equals(message.getRole()));
            deque.addFirst(createSystemMessage());
        }

        logHistory(conversationId, deque, "trimmed");
    }

    private void logHistory(String conversationId, Deque<ChatMessage> deque, String stage) {
        if (!log.isDebugEnabled()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[conversation=").append(conversationId).append("] [stage=").append(stage).append("] ");
        int index = 0;
        for (ChatMessage message : deque) {
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