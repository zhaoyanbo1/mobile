package com.kuafu.web.dto.team;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request payload for approving or rejecting an application.
 */
@Data
public class DecideTeamActivityRequest {

    @NotNull
    private Long applicantId;

    private Boolean approve;

    /** Optional host supplied reason when rejecting. */
    private String reason;

    private Long userId;
}