package com.kuafu.web.dto.team;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * Request payload for updating an existing activity.
 */
@Data
public class UpdateTeamActivityRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String time;

    @NotBlank
    @Size(max = 255)
    private String location;

    @NotNull
    @Min(2)
    @Max(10)
    private Integer minParticipants;

    @NotNull
    @Min(2)
    @Max(10)
    private Integer maxParticipants;

    private Long userId;
}