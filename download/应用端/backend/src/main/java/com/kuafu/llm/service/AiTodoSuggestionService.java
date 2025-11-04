package com.kuafu.llm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuafu.api.dto.AiTodoSuggestionResult;
import com.kuafu.api.dto.AiTodoSuggestionTask;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.llm.entity.AiConversation;
import com.kuafu.llm.entity.AiMessage;
import com.kuafu.llm.mapper.AiConversationMapper;
import com.kuafu.llm.mapper.AiMessageMapper;
import com.kuafu.llm.model.MessageStatus;
import com.kuafu.web.entity.HealthQuestionnaire;
import com.kuafu.web.entity.ReminderItem;
import com.kuafu.web.service.IHealthQuestionnaireService;
import com.kuafu.web.service.IReminderItemService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Generates personalised todo suggestions with help from the AI model
 * and persists them as reminder items.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiTodoSuggestionService {

    private static final List<String> CATEGORY_ORDER = List.of("activity", "social", "diet");
    private static final Set<String> CATEGORY_SET = Collections.unmodifiableSet(new LinkedHashSet<>(CATEGORY_ORDER));
    private static final Map<String, String> QUESTION_TEXTS;
    private static final Map<String, Map<Integer, String>> QUESTION_OPTIONS;

    static {
        Map<String, String> questionTexts = new LinkedHashMap<>();
        questionTexts.put("adl", "Can you independently complete basic daily activities (such as dressing, bathing and eating)?");
        questionTexts.put("mobility_out", "Can you go out independently (for a walk, shopping, community activities)?");
        questionTexts.put("falls", "Have you ever fallen down in the past year?");
        questionTexts.put("weight_loss", "Have you lost more than 4 kilograms in weight within 3 months?");
        questionTexts.put("diseases", "Do you have any of the following major diseases?");
        questionTexts.put("pa_minutes", "What is your average daily exercise or walking time?");
        questionTexts.put("pa_willingness", "Would you like to try some light exercises (walking, stretching, Tai Chi)?");
        questionTexts.put("flu_vaccine", "Did you get the flu vaccine last year?");
        questionTexts.put("polypharmacy", "Are you currently taking three or more medications every day?");
        questionTexts.put("social", "How many times do you communicate with your family or friends each week?");
        questionTexts.put("fv_serves", "How many servings of fruits/vegetables do you eat approximately every day?");
        QUESTION_TEXTS = Collections.unmodifiableMap(questionTexts);

        Map<String, Map<Integer, String>> optionTexts = new LinkedHashMap<>();
        optionTexts.put("adl", mapOf(
                0, "Completely independent",
                2, "Need a little help",
                3, "Need a lot of help",
                4, "Completely dependent"
        ));
        optionTexts.put("mobility_out", mapOf(
                0, "Often can",
                1, "Occasionally",
                2, "Rarely",
                3, "Completely impossible"
        ));
        optionTexts.put("falls", mapOf(
                0, "No",
                2, "Once",
                4, "Twice or more"
        ));
        optionTexts.put("weight_loss", mapOf(
                0, "No",
                3, "Yes"
        ));
        optionTexts.put("pa_minutes", mapOf(
                0, "More than 1 hour",
                1, "30–60 Minutes",
                2, "10–30 Minutes",
                3, "Less than 10 Minutes"
        ));
        optionTexts.put("pa_willingness", mapOf(
                0, "Yes",
                1, "Not sure",
                2, "No"
        ));
        optionTexts.put("flu_vaccine", mapOf(
                0, "Yes",
                1, "No"
        ));
        optionTexts.put("polypharmacy", mapOf(
                0, "No",
                2, "Yes"
        ));
        optionTexts.put("social", mapOf(
                0, "Almost every day",
                1, "2 to 3 times a week",
                2, "1 time a week",
                3, "Rarely or none"
        ));
        optionTexts.put("fv_serves", mapOf(
                0, "5 or more",
                1, "3–4",
                2, "2–3",
                3, "0–1"
        ));
        QUESTION_OPTIONS = Collections.unmodifiableMap(optionTexts);
    }

    private final ChatService chatService;
    private final IReminderItemService reminderItemService;
    private final IHealthQuestionnaireService healthQuestionnaireService;
    private final AiConversationMapper conversationMapper;
    private final AiMessageMapper messageMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private enum GenerationProfile {
        REGULAR("high", "Keep the plan gentle and easy to complete so the user feels successful.", 3, true),
        BONUS("superhigh", "These are bonus challenges. Make them more demanding than usual while staying safe and realistic for the user.", 1, false);

        private final String priority;
        private final String guidance;
        private final int expectedTaskCount;
        private final boolean requireAllCategories;

        GenerationProfile(String priority, String guidance, int expectedTaskCount, boolean requireAllCategories) {
            this.priority = priority;
            this.guidance = guidance;
            this.expectedTaskCount = expectedTaskCount;
            this.requireAllCategories = requireAllCategories;
        }

        public String getPriority() {
            return priority;
        }

        public String getGuidance() {
            return guidance;
        }

        public int getExpectedTaskCount() {
            return expectedTaskCount;
        }

        public boolean isRequireAllCategories() {
            return requireAllCategories;
        }
    }

    public AiTodoSuggestionResult generateSuggestions(String userId) {
        return generateForProfile(userId, GenerationProfile.REGULAR);
    }

    public AiTodoSuggestionResult generateBonusSuggestions(String userId) {
        return generateForProfile(userId, GenerationProfile.BONUS);
    }

    private AiTodoSuggestionResult generateForProfile(String userId, GenerationProfile profile) {
        Integer numericUserId = parseUserId(userId);
        String prompt = buildPrompt(numericUserId, profile);
        String rawResponse = callModel(prompt);
        AiResponsePayload payload = parseAiResponse(rawResponse, profile);
        List<AiGeneratedTask> orderedTasks = payload.getOrderedTasks();
        List<ReminderItem> savedItems = persistTasks(numericUserId, orderedTasks, profile);
        List<AiTodoSuggestionTask> taskViews = IntStream.range(0, Math.min(savedItems.size(), orderedTasks.size()))
                .mapToObj(index -> {
                    ReminderItem item = savedItems.get(index);
                    AiGeneratedTask task = orderedTasks.get(index);
                    return AiTodoSuggestionTask.builder()
                            .reminderItemId(item.getReminderItemId())
                            .category(task.getCategory())
                            .title(item.getTitle())
                            .description(task.getDescription())
                            .dueTime(item.getReminderTime())
                            .priority(item.getPriority())
                            .build();
                })
                .collect(Collectors.toList());

        return AiTodoSuggestionResult.builder()
                .tasks(taskViews)
                .summary(payload.getSummary())
                .build();
    }

    private Integer parseUserId(String userId) {
        try {
            return Integer.valueOf(userId);
        } catch (NumberFormatException ex) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Invalid user id");
        }
    }

    private String buildPrompt(Integer userId, GenerationProfile profile) {
        StringBuilder builder = new StringBuilder();
        builder.append("You are LiveWell Coach, an AI assistant who creates gentle, practical wellbeing tasks for older adults.\n");
        if (profile.isRequireAllCategories()) {
            builder.append("Create exactly three actionable tasks tailored for the user. The tasks must cover these categories: activity, social, and diet.\n");
        } else {
            builder.append("Create exactly one actionable task tailored for the user. The task must belong to one of these categories: activity, social, or diet. Choose the category that best suits the user's situation for a challenging bonus goal.\n");
        }
        builder.append("Each task should be safe, realistic for the user, and phrased in encouraging language.\n");
        builder.append(profile.getGuidance()).append("\n");
        builder.append("Avoid recommending foods or activities that the user has indicated they cannot do or are allergic to in their conversations or questionnaire responses.\n");
        builder.append("Respond ONLY with valid JSON matching this structure without code fences: {\"tasks\":[{\"category\":\"activity|social|diet\",\"title\":\"...\",\"description\":\"...\"},...],\"summary\":\"...\"}.\n");
        builder.append("Keep descriptions concise (max 120 characters).\n\n");

        builder.append("=== Questionnaire Responses ===\n");
        builder.append(buildQuestionnaireSection(userId)).append("\n\n");

        builder.append("=== Conversation History ===\n");
        builder.append(buildConversationSection(userId)).append("\n\n");

        if (profile.isRequireAllCategories()) {
            builder.append("Remember: provide exactly one task for each category (activity, social, diet).");
        } else {
            builder.append("Remember: provide exactly one task in total, selecting whichever of the categories (activity, social, diet) fits the user's needs best.");
        }
        return builder.toString();
    }

    private String buildQuestionnaireSection(Integer userId) {
        HealthQuestionnaire questionnaire = healthQuestionnaireService.lambdaQuery()
                .eq(HealthQuestionnaire::getUserInfoUserInfoId1, userId)
                .orderByDesc(HealthQuestionnaire::getUpdateTime)
                .orderByDesc(HealthQuestionnaire::getCreationTime)
                .last("LIMIT 1")
                .one();
        if (questionnaire == null) {
            return "No questionnaire responses available.";
        }
        StringBuilder builder = new StringBuilder();
        QUESTION_TEXTS.forEach((field, question) -> {
            String answer = describeAnswer(field, questionnaire);
            if (StringUtils.hasText(answer)) {
                builder.append("- ").append(question).append(" Answer: ").append(answer).append("\n");
            }
        });
        if (StringUtils.hasText(questionnaire.getAnswersJson())) {
            builder.append("Raw answers JSON: ").append(questionnaire.getAnswersJson()).append("\n");
        }
        return builder.toString().trim();
    }

    private String describeAnswer(String field, HealthQuestionnaire questionnaire) {
        switch (field) {
            case "diseases":
                if (!StringUtils.hasText(questionnaire.getDiseases())) {
                    return null;
                }
                return Arrays.stream(questionnaire.getDiseases().split(","))
                        .map(String::trim)
                        .filter(StringUtils::hasText)
                        .collect(Collectors.joining(", "));
            default:
                Integer value = extractNumeric(field, questionnaire);
                if (value == null) {
                    return null;
                }
                Map<Integer, String> options = QUESTION_OPTIONS.get(field);
                if (options != null && options.containsKey(value)) {
                    return options.get(value);
                }
                return String.valueOf(value);
        }
    }

    private Integer extractNumeric(String field, HealthQuestionnaire questionnaire) {
        switch (field) {
            case "adl":
                return questionnaire.getAdl();
            case "mobility_out":
                return questionnaire.getMobilityOut();
            case "falls":
                return questionnaire.getFalls();
            case "weight_loss":
                return questionnaire.getWeightLoss();
            case "pa_minutes":
                return questionnaire.getPaMinutes();
            case "pa_willingness":
                return questionnaire.getPaWillingness();
            case "flu_vaccine":
                return questionnaire.getFluVaccine();
            case "polypharmacy":
                return questionnaire.getPolypharmacy();
            case "social":
                return questionnaire.getSocial();
            case "fv_serves":
                return questionnaire.getFvServes();
            default:
                return null;
        }
    }

    private String buildConversationSection(Integer userId) {
        List<AiConversation> conversations = conversationMapper.selectList(
                com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery(AiConversation.class)
                        .eq(AiConversation::getUserId, String.valueOf(userId))
                        .orderByAsc(AiConversation::getCreateTime)
        );
        if (CollectionUtils.isEmpty(conversations)) {
            return "No chat history yet.";
        }
        StringBuilder builder = new StringBuilder();
        for (AiConversation conversation : conversations) {
            builder.append("Conversation ").append(conversation.getConversationId());
            if (StringUtils.hasText(conversation.getTitle())) {
                builder.append(" (title: ").append(conversation.getTitle()).append(")");
            }
            builder.append(':').append("\n");
            List<AiMessage> messages = messageMapper.selectList(
                    com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery(AiMessage.class)
                            .eq(AiMessage::getConversationId, conversation.getConversationId())
                            .ne(AiMessage::getStatus, MessageStatus.STREAMING.name())
                            .orderByAsc(AiMessage::getMessageId)
            );
            if (CollectionUtils.isEmpty(messages)) {
                builder.append("  (no messages)\n");
                continue;
            }
            for (AiMessage message : messages) {
                if (!StringUtils.hasText(message.getContent())) {
                    continue;
                }
                builder.append("  ").append(message.getRole() == null ? "assistant" : message.getRole().toLowerCase(Locale.ENGLISH));
                builder.append(": ");
                builder.append(message.getContent().replace('\n', ' ')).append("\n");
            }
            builder.append("\n");
        }
        return builder.toString().trim();
    }

    private String callModel(String prompt) {
        StringBuilder builder = new StringBuilder();
        try (com.openai.core.http.StreamResponse<com.openai.models.responses.ResponseStreamEvent> stream = chatService.streamChat(prompt)) {
            stream.stream().forEach(event -> event.outputTextDelta().ifPresent(delta -> builder.append(delta.delta())));
        } catch (Exception ex) {
            log.error("Failed to call AI model for todo suggestions", ex);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to contact AI");
        }
        return builder.toString();
    }

    private AiResponsePayload parseAiResponse(String raw, GenerationProfile profile) {
        if (!StringUtils.hasText(raw)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI returned empty response");
        }
        String sanitised = sanitiseJson(raw);
        try {
            JsonNode root = objectMapper.readTree(sanitised);
            JsonNode tasksNode = root.path("tasks");
            if (!tasksNode.isArray()) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI response missing tasks array");
            }
            Map<String, AiGeneratedTask> byCategory = new LinkedHashMap<>();
            List<AiGeneratedTask> orderedByAppearance = new ArrayList<>();
            tasksNode.forEach(node -> {
                String category = textOrNull(node, "category");
                String title = textOrNull(node, "title");
                String description = textOrNull(node, "description");
                if (!StringUtils.hasText(description)) {
                    description = textOrNull(node, "details");
                }
                if (!StringUtils.hasText(category) || !StringUtils.hasText(title)) {
                    return;
                }
                String normalised = category.trim().toLowerCase(Locale.ENGLISH);
                if (!CATEGORY_SET.contains(normalised)) {
                    return;
                }
                if (byCategory.containsKey(normalised)) {
                    return;
                }
                AiGeneratedTask task = AiGeneratedTask.builder()
                        .category(normalised)
                        .title(title.trim())
                        .description(StringUtils.hasText(description) ? description.trim() : title.trim())
                        .build();
                byCategory.put(normalised, task);
                orderedByAppearance.add(task);
            });
            List<AiGeneratedTask> ordered;
            if (profile.isRequireAllCategories()) {
                if (!byCategory.keySet().containsAll(CATEGORY_SET)) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI response missing required task categories");
                }
                ordered = CATEGORY_ORDER.stream()
                        .map(byCategory::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            } else {
                if (orderedByAppearance.isEmpty()) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI response missing tasks");
                }
                ordered = new ArrayList<>(orderedByAppearance.subList(0, Math.min(profile.getExpectedTaskCount(), orderedByAppearance.size())));
            }
            if (ordered.size() < profile.getExpectedTaskCount()) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI response missing tasks");
            }
            String summary = textOrNull(root, "summary");
            return AiResponsePayload.builder()
                    .orderedTasks(ordered)
                    .summary(summary)
                    .build();
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse AI todo response: {}", raw);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to parse AI response");
        }
    }

    private List<ReminderItem> persistTasks(Integer userId, List<AiGeneratedTask> tasks, GenerationProfile profile) {
        if (CollectionUtils.isEmpty(tasks)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "No tasks to persist");
        }
        LocalDateTime dueAt = LocalDate.now().atTime(23, 59);
        Date dueDate = Date.from(dueAt.atZone(ZoneId.systemDefault()).toInstant());
        Date now = new Date();
        List<ReminderItem> entities = new ArrayList<>();
        for (AiGeneratedTask task : tasks) {
            ReminderItem item = ReminderItem.builder()
                    .userInfoUserInfoId1(userId)
                    .reminderTypeEnumId(resolveReminderType(task.getCategory()))
                    .title(task.getTitle())
                    .description(formatDescription(task))
                    .reminderTime(dueDate)
                    .isCompleted(Boolean.FALSE)
                    .priority(profile.getPriority())
                    .creationTime(now)
                    .updateTime(now)
                    .build();
            entities.add(item);
        }
        if (!reminderItemService.saveBatch(entities)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Failed to save AI tasks");
        }
        return entities;
    }

    private int resolveReminderType(String category) {
        if (!StringUtils.hasText(category)) {
            return 2; // default to activity
        }
        String normalised = category.trim().toLowerCase(Locale.ENGLISH);
        if ("diet".equals(normalised)) {
            return 3;
        }
        // Treat social as an activity-style reminder for now
        return 2;
    }

    private String formatDescription(AiGeneratedTask task) {
        String prefix = "[" + task.getCategory() + "] ";
        if (!StringUtils.hasText(task.getDescription())) {
            return prefix + task.getTitle();
        }
        return prefix + task.getDescription();
    }

    private String sanitiseJson(String raw) {
        String trimmed = raw.trim();
        if (trimmed.startsWith("```") && trimmed.endsWith("```")) {
            trimmed = trimmed.substring(3, trimmed.length() - 3).trim();
        }
        int start = trimmed.indexOf('{');
        int end = trimmed.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return trimmed.substring(start, end + 1);
        }
        return trimmed;
    }

    private String textOrNull(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null || value.isNull()) {
            return null;
        }
        String text = value.asText();
        return StringUtils.hasText(text) ? text : null;
    }

    private static Map<Integer, String> mapOf(Object... values) {
        Map<Integer, String> map = new LinkedHashMap<>();
        for (int i = 0; i < values.length - 1; i += 2) {
            Integer key = (Integer) values[i];
            String value = (String) values[i + 1];
            map.put(key, value);
        }
        return map;
    }

    @Data
    @Builder
    private static class AiResponsePayload {
        private List<AiGeneratedTask> orderedTasks;
        private String summary;
    }

    @Data
    @Builder
    private static class AiGeneratedTask {
        private String category;
        private String title;
        private String description;
    }
}