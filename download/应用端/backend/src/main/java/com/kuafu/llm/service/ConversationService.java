package com.kuafu.llm.service;

import com.kuafu.llm.model.ChatMessage;
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

    private static final int MAX_HISTORY = 32;
    private static final int MAX_CONVERSATIONS = 1000;

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
        store.put(id, new ConcurrentLinkedDeque<>());
        return id;
    }

    /** reset conversation */
    public synchronized void reset(String conversationId) {
        store.remove(conversationId);
    }

    /** append single message */
    public synchronized void addMessage(String conversationId, ChatMessage message) {
        if (!StringUtils.hasText(conversationId) || message == null) {
            return;
        }
        Deque<ChatMessage> deque = store.computeIfAbsent(conversationId, k -> new ConcurrentLinkedDeque<>());
        deque.addLast(message);
        while (deque.size() > MAX_HISTORY) {
            deque.removeFirst();
        }
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
            sb.append(m.getRole()).append(": ").append(m.getContent()).append("\n");
        }
        return sb.toString();
    }
}