package com.kuafu.llm.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuafu.api.dto.ConversationMessageDTO;
import com.kuafu.api.dto.ConversationSummaryDTO;
import com.kuafu.api.dto.MessagePageResponse;
import com.kuafu.api.dto.PageResult;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.llm.entity.AiConversation;
import com.kuafu.llm.entity.AiMessage;
import com.kuafu.llm.mapper.AiConversationMapper;
import com.kuafu.llm.mapper.AiMessageMapper;
import com.kuafu.llm.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kuafu.llm.model.ConversationStatus;
import com.kuafu.llm.model.MessageStatus;
import com.kuafu.llm.util.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Conversation management backed by the database.
 */
@Service
public class ConversationService {
    //    private static final Logger log = LoggerFactory.getLogger(ConversationService.class);
//
//    /** maximum number of non-system messages to retain */
//    private static final int MAX_HISTORY = 32;
//    private static final int MAX_CONVERSATIONS = 1000;
    private static final int CONTEXT_HISTORY_LIMIT = 10;
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

    //    private final Map<String, Deque<ChatMessage>> store = Collections.synchronizedMap(
//            new LinkedHashMap<String, Deque<ChatMessage>>(16, 0.75f, true) {
//                @Override
//                protected boolean removeEldestEntry(Map.Entry<String, Deque<ChatMessage>> eldest) {
//                    return size() > MAX_CONVERSATIONS;
//                }
//            }
//    );
    private final AiConversationMapper conversationMapper;
    private final AiMessageMapper messageMapper;

    /** create a new conversation id */
    public ConversationService(AiConversationMapper conversationMapper, AiMessageMapper messageMapper) {
        this.conversationMapper = conversationMapper;
        this.messageMapper = messageMapper;
    }

    @Transactional
    public String newConversation(String userId, String title) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "userId不能为空");
        }
        String conversationId = UUID.randomUUID().toString();
        Date now = new Date();

        AiConversation conversation = AiConversation.builder()
                .conversationId(conversationId)
                .userId(userId)
                .title(StringUtils.hasText(title) ? title.trim() : null)
                .status(ConversationStatus.ACTIVE.name())
                .messageCount(0)
                .createTime(now)
                .updateTime(now)
                .build();
        conversationMapper.insert(conversation);
//        AiMessage system = AiMessage.builder()
//                .conversationId(conversationId)
//                .role(SYSTEM_ROLE)
//                .name(SYSTEM_NAME)
//                .content(SYSTEM_PROMPT)
//                .status(MessageStatus.FINAL.name())
//                .finishReason("system")
//                .promptTokens(0)
//                .completionTokens(TokenUtils.estimateTokens(SYSTEM_PROMPT))
//                .totalTokens(TokenUtils.estimateTokens(SYSTEM_PROMPT))
//                .createTime(now)
//                .updateTime(now)
//                .build();
//        messageMapper.insert(system);
//
//        LambdaUpdateWrapper<AiConversation> initUpdate = Wrappers.lambdaUpdate(AiConversation.class)
//                .eq(AiConversation::getConversationId, conversationId)
//                .set(AiConversation::getMessageCount, 1)
//                .set(AiConversation::getLastMessageTime, now)
//                .set(AiConversation::getUpdateTime, now);
//        conversationMapper.update(null, initUpdate);
        return conversationId;
    }

    public AiConversation requireConversation(String conversationId, String userId) {
        AiConversation conversation = conversationMapper.selectById(conversationId);
        if (conversation == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "The conversation does not exist.");
        }
        if (StringUtils.hasText(userId) && !userId.equals(conversation.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "No access to this session is authorized");
        }
        return conversation;
    }

    /** append single message */
//    public synchronized void addMessage(String conversationId, ChatMessage message) {
//        if (!StringUtils.hasText(conversationId) || message == null) {
    public AiConversation requireActiveConversation(String conversationId, String userId) {
        AiConversation conversation = requireConversation(conversationId, userId);
        if (ConversationStatus.DELETED.name().equals(conversation.getStatus())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "The conversation has been deleted.");
        }
        if (ConversationStatus.ARCHIVED.name().equals(conversation.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "The conversation has been archived and cannot continue");
        }
        return conversation;
    }

    @Transactional
    public void appendUserMessage(String conversationId, String userId, String content) {
        if (!StringUtils.hasText(content)) {
            return;
        }
//        Deque<ChatMessage> deque = store.computeIfAbsent(conversationId, k -> new ConcurrentLinkedDeque<>());
//        deque.addLast(message);
//        while (deque.size() > MAX_HISTORY) {
//            deque.removeFirst();
        //Deque<ChatMessage> deque = store.computeIfAbsent(conversationId, k -> createDequeWithSystemMessage());
        AiConversation conversation = requireActiveConversation(conversationId, userId);
        Date now = new Date();
        AiMessage message = AiMessage.builder()
                .conversationId(conversationId)
                .role("user")
                .content(content)
                .status(MessageStatus.FINAL.name())
                .finishReason("user")
                .completionTokens(TokenUtils.estimateTokens(content))
                .totalTokens(TokenUtils.estimateTokens(content))
                .createTime(now)
                .updateTime(now)
                .build();
        messageMapper.insert(message);

        LambdaUpdateWrapper<AiConversation> updateWrapper = Wrappers.lambdaUpdate(AiConversation.class)
                .eq(AiConversation::getConversationId, conversationId)
                .setSql("message_count = message_count + 1")
                .set(AiConversation::getLastMessageTime, now)
                .set(AiConversation::getUpdateTime, now);
        if (conversation != null && !StringUtils.hasText(conversation.getTitle())) {
            updateWrapper.set(AiConversation::getTitle, deriveTitle(content));
        }
        conversationMapper.update(null, updateWrapper);
    }

    @Transactional
    public Long beginAssistantMessage(String conversationId, String userId) {
        requireActiveConversation(conversationId, userId);
        Date now = new Date();
        AiMessage message = AiMessage.builder()
                .conversationId(conversationId)
                .role("assistant")
                .status(MessageStatus.STREAMING.name())
                .createTime(now)
                .updateTime(now)
                .build();
        messageMapper.insert(message);

        LambdaUpdateWrapper<AiConversation> updateWrapper = Wrappers.lambdaUpdate(AiConversation.class)
                .eq(AiConversation::getConversationId, conversationId)
                .setSql("message_count = message_count + 1")
                .set(AiConversation::getLastMessageTime, now)
                .set(AiConversation::getUpdateTime, now);
        conversationMapper.update(null, updateWrapper);
        return message.getMessageId();
    }

    //        if (SYSTEM_ROLE.equals(message.getRole())) {
//            log.debug("Ignoring attempt to append system message to conversation {}", conversationId);
    @Transactional
    public void updateAssistantMessageContent(Long messageId, String content) {
        if (messageId == null) {
            return;
        }
        LambdaUpdateWrapper<AiMessage> updateWrapper = Wrappers.lambdaUpdate(AiMessage.class)
                .eq(AiMessage::getMessageId, messageId)
                .set(AiMessage::getContent, content)
                .set(AiMessage::getUpdateTime, new Date());
        messageMapper.update(null, updateWrapper);
    }

    //        ChatMessage stored = copyMessage(message);
//        deque.addLast(stored);
//        trimHistory(conversationId, deque);
    @Transactional
    public void finalizeAssistantMessage(Long messageId, String content, Integer promptTokens, Integer completionTokens, String finishReason) {
        if (messageId == null) {
            return;
        }
        AiMessage existing = messageMapper.selectById(messageId);
        Integer total = null;
        if (promptTokens != null || completionTokens != null) {
            total = (promptTokens == null ? 0 : promptTokens) + (completionTokens == null ? 0 : completionTokens);
        }
        Date now = new Date();
        LambdaUpdateWrapper<AiMessage> updateWrapper = Wrappers.lambdaUpdate(AiMessage.class)
                .eq(AiMessage::getMessageId, messageId)
                .set(AiMessage::getContent, content)
                .set(AiMessage::getStatus, MessageStatus.FINAL.name())
                .set(AiMessage::getFinishReason, finishReason)
                .set(AiMessage::getPromptTokens, promptTokens)
                .set(AiMessage::getCompletionTokens, completionTokens)
                .set(AiMessage::getTotalTokens, total)
                .set(AiMessage::getUpdateTime, now);
        messageMapper.update(null, updateWrapper);
        if (existing != null) {
            LambdaUpdateWrapper<AiConversation> conversationUpdate = Wrappers.lambdaUpdate(AiConversation.class)
                    .eq(AiConversation::getConversationId, existing.getConversationId())
                    .set(AiConversation::getLastMessageTime, now)
                    .set(AiConversation::getUpdateTime, now);
            conversationMapper.update(null, conversationUpdate);
        }
    }

    /** append list of messages */
//    public synchronized void addMessages(String conversationId, List<ChatMessage> messages) {
//        if (messages == null) {
    @Transactional
    public void markAssistantMessageError(Long messageId, String content, String errorMessage) {
        if (messageId == null) {
            return;
        }
//        for (ChatMessage m : messages) {
//            addMessage(conversationId, m);
        AiMessage existing = messageMapper.selectById(messageId);
        Date now = new Date();
        LambdaUpdateWrapper<AiMessage> updateWrapper = Wrappers.lambdaUpdate(AiMessage.class)
                .eq(AiMessage::getMessageId, messageId)
                .set(AiMessage::getContent, content)
                .set(AiMessage::getStatus, MessageStatus.ERROR.name())
                .set(AiMessage::getErrorMessage, errorMessage)
                .set(AiMessage::getFinishReason, "error")
                .set(AiMessage::getUpdateTime, now);
        messageMapper.update(null, updateWrapper);
        if (existing != null) {
            LambdaUpdateWrapper<AiConversation> conversationUpdate = Wrappers.lambdaUpdate(AiConversation.class)
                    .eq(AiConversation::getConversationId, existing.getConversationId())
                    .set(AiConversation::getLastMessageTime, now)
                    .set(AiConversation::getUpdateTime, now);
            conversationMapper.update(null, conversationUpdate);
        }
    }

    /** get copy of messages */
//    public synchronized List<ChatMessage> getMessages(String conversationId) {
//        Deque<ChatMessage> deque = store.get(conversationId);
//        if (deque == null) {
//            return Collections.emptyList();
    public List<ChatMessage> getMessagesForPrompt(String conversationId) {
        List<ChatMessage> result = new ArrayList<>();
        ChatMessage system = new ChatMessage();
        system.setRole(SYSTEM_ROLE);
        system.setName(SYSTEM_NAME);
        system.setContent(SYSTEM_PROMPT);
        result.add(system);

        List<AiMessage> latest = messageMapper.selectList(Wrappers.lambdaQuery(AiMessage.class)
                .eq(AiMessage::getConversationId, conversationId)
                .ne(AiMessage::getRole, SYSTEM_ROLE)
                .eq(AiMessage::getStatus, MessageStatus.FINAL.name())
                .orderByDesc(AiMessage::getMessageId)
                .last("LIMIT " + CONTEXT_HISTORY_LIMIT));
        Collections.reverse(latest);
        for (AiMessage message : latest) {
            result.add(toChatMessage(message));
        }
        return result;
    }

    /** build text prompt from conversation history */
    public String buildPrompt(String conversationId) {
        StringBuilder sb = new StringBuilder();
//        for (ChatMessage m : getMessages(conversationId)) {
//            //sb.append(m.getRole()).append(": ").append(m.getContent()).append("\n");
//            if (!StringUtils.hasText(m.getContent())) {
        for (ChatMessage message : getMessagesForPrompt(conversationId)) {
            if (!StringUtils.hasText(message.getContent())) {
                continue;
            }
            if (StringUtils.hasText(message.getName())) {
                sb.append(message.getRole()).append("(").append(message.getName()).append(")");
            } else {
                sb.append(message.getRole());
            }
            sb.append(": ").append(message.getContent()).append("\n");
        }
        return sb.toString();
    }
    public List<ConversationMessageDTO> getRecentMessages(String conversationId) {
        List<AiMessage> messages = messageMapper.selectList(Wrappers.lambdaQuery(AiMessage.class)
                .eq(AiMessage::getConversationId, conversationId)
                .ne(AiMessage::getStatus, MessageStatus.STREAMING.name())
                .orderByDesc(AiMessage::getMessageId)
                .last("LIMIT " + (CONTEXT_HISTORY_LIMIT + 1)));
        Collections.reverse(messages);
        AiMessage system = messageMapper.selectOne(Wrappers.lambdaQuery(AiMessage.class)
                .eq(AiMessage::getConversationId, conversationId)
                .eq(AiMessage::getRole, SYSTEM_ROLE)
                .orderByAsc(AiMessage::getMessageId)
                .last("LIMIT 1"));
        if (system != null && messages.stream().noneMatch(m -> m.getMessageId().equals(system.getMessageId()))) {
            messages.add(0, system);
        }
        return messages.stream().map(this::toDto).collect(Collectors.toList());
    }

    public PageResult<ConversationSummaryDTO> pageConversations(String userId, long current, long size, ConversationStatus status) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "userId不能为空");
        }
        Page<AiConversation> page = new Page<>(current, size);
        LambdaQueryWrapper<AiConversation> queryWrapper = Wrappers.lambdaQuery(AiConversation.class)
                .eq(AiConversation::getUserId, userId)
                .orderByDesc(AiConversation::getUpdateTime);
        if (status != null) {
            queryWrapper.eq(AiConversation::getStatus, status.name());
        }
        Page<AiConversation> data = conversationMapper.selectPage(page, queryWrapper);
        List<ConversationSummaryDTO> records = data.getRecords().stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
        return PageResult.<ConversationSummaryDTO>builder()
                .current(data.getCurrent())
                .size(data.getSize())
                .total(data.getTotal())
                .pages(data.getPages())
                .hasNext(data.getCurrent() < data.getPages())
                .records(records)
                .build();
    }

    public ConversationSummaryDTO getConversationSummary(String conversationId, String userId) {
        AiConversation conversation = requireConversation(conversationId, userId);
        return toSummary(conversation);
    }

    public MessagePageResponse pageMessages(String conversationId, String userId, Long cursor, Integer limit) {
        requireConversation(conversationId, userId);
        int pageSize = (limit == null || limit <= 0) ? 20 : Math.min(limit, 100);
        LambdaQueryWrapper<AiMessage> queryWrapper = Wrappers.lambdaQuery(AiMessage.class)
                .eq(AiMessage::getConversationId, conversationId)
                .orderByDesc(AiMessage::getMessageId);
        if (cursor != null) {
            queryWrapper.lt(AiMessage::getMessageId, cursor);
        }

        queryWrapper.last("LIMIT " + pageSize);
        List<AiMessage> rows = messageMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(rows)) {
            return MessagePageResponse.builder()
                    .records(Collections.emptyList())
                    .hasMore(false)
                    .build();
        }
        boolean hasMore = rows.size() == pageSize;
        Long nextCursor = hasMore ? rows.get(rows.size() - 1).getMessageId() : null;
        Collections.reverse(rows);
        List<ConversationMessageDTO> dtos = rows.stream().map(this::toDto).collect(Collectors.toList());
        return MessagePageResponse.builder()
                .records(dtos)
                .nextCursor(nextCursor)
                .hasMore(hasMore)
                .build();
    }

    @Transactional
    public void renameConversation(String conversationId, String userId, String title) {
        AiConversation conversation = requireConversation(conversationId, userId);
        LambdaUpdateWrapper<AiConversation> updateWrapper = Wrappers.lambdaUpdate(AiConversation.class)
                .eq(AiConversation::getConversationId, conversationId)
                .set(AiConversation::getTitle, StringUtils.hasText(title) ? title.trim() : conversation.getTitle())
                .set(AiConversation::getUpdateTime, new Date());
        conversationMapper.update(null, updateWrapper);
    }


    @Transactional
    public void archiveConversation(String conversationId, String userId) {
        requireConversation(conversationId, userId);
        LambdaUpdateWrapper<AiConversation> updateWrapper = Wrappers.lambdaUpdate(AiConversation.class)
                .eq(AiConversation::getConversationId, conversationId)
                .set(AiConversation::getStatus, ConversationStatus.ARCHIVED.name())
                .set(AiConversation::getArchivedAt, new Date())
                .set(AiConversation::getUpdateTime, new Date());
        conversationMapper.update(null, updateWrapper);
    }

    @Transactional
    public void restoreConversation(String conversationId, String userId) {
        requireConversation(conversationId, userId);
        LambdaUpdateWrapper<AiConversation> updateWrapper = Wrappers.lambdaUpdate(AiConversation.class)
                .eq(AiConversation::getConversationId, conversationId)
                .set(AiConversation::getStatus, ConversationStatus.ACTIVE.name())
                .set(AiConversation::getArchivedAt, null)
                .set(AiConversation::getUpdateTime, new Date());
        conversationMapper.update(null, updateWrapper);
    }

    //    private void logHistory(String conversationId, Deque<ChatMessage> deque, String stage) {
//        if (!log.isDebugEnabled()) {
//            return;
//        }
//        StringBuilder sb = new StringBuilder();
//        sb.append("[conversation=").append(conversationId).append("] [stage=").append(stage).append("] ");
//        int index = 0;
//        for (ChatMessage message : deque) {
//            sb.append("#").append(index++)
//                    .append(" role=").append(message.getRole());
//            if (StringUtils.hasText(message.getName())) {
//                sb.append(" name=").append(message.getName());
//            }
//            sb.append(" content=")
//                    .append(message.getContent() == null ? "" : message.getContent().replace('\n', ' '))
//                    .append(" | ");
    @Transactional
    public void deleteConversation(String conversationId, String userId) {
        requireConversation(conversationId, userId);
        Date now = new Date();
        LambdaUpdateWrapper<AiConversation> updateWrapper = Wrappers.lambdaUpdate(AiConversation.class)
                .eq(AiConversation::getConversationId, conversationId)
                .set(AiConversation::getStatus, ConversationStatus.DELETED.name())
                .set(AiConversation::getDeletedAt, now)
                .set(AiConversation::getUpdateTime, now);
        conversationMapper.update(null, updateWrapper);
    }

    private ChatMessage toChatMessage(AiMessage message) {
        ChatMessage copy = new ChatMessage();
        copy.setRole(message.getRole());
        copy.setName(message.getName());
        copy.setContent(message.getContent());
        return copy;
    }

    private ConversationMessageDTO toDto(AiMessage message) {
        return ConversationMessageDTO.builder()
                .messageId(message.getMessageId())
                .conversationId(message.getConversationId())
                .role(message.getRole())
                .name(message.getName())
                .content(message.getContent())
                .status(message.getStatus())
                .finishReason(message.getFinishReason())
                .promptTokens(message.getPromptTokens())
                .completionTokens(message.getCompletionTokens())
                .totalTokens(message.getTotalTokens())
                .errorMessage(message.getErrorMessage())
                .createTime(message.getCreateTime())
                .updateTime(message.getUpdateTime())
                .build();
    }

    public ConversationSummaryDTO toSummary(AiConversation conversation) {
        return ConversationSummaryDTO.builder()
                .conversationId(conversation.getConversationId())
                .userId(conversation.getUserId())
                .title(conversation.getTitle())
                .status(conversation.getStatus())
                .messageCount(conversation.getMessageCount())
                .lastMessageTime(conversation.getLastMessageTime())
                .archivedAt(conversation.getArchivedAt())
                .deletedAt(conversation.getDeletedAt())
                .createTime(conversation.getCreateTime())
                .updateTime(conversation.getUpdateTime())
                .build();
    }

    private String deriveTitle(String content) {
        String normalized = content.replaceAll("\\s+", " ").trim();
        if (normalized.length() <= 40) {
            return normalized;
        }
        return normalized.substring(0, 40) + "...";
    }
}