package com.kuafu.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response payload for AI generated todo suggestions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiTodoSuggestionResult {

    @JsonProperty("tasks")
    private List<AiTodoSuggestionTask> tasks;

    @JsonProperty("summary")
    private String summary;
}