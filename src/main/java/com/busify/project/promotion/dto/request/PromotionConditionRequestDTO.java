package com.busify.project.promotion.dto.request;

import com.busify.project.promotion.enums.ConditionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PromotionConditionRequestDTO {
    private Long conditionId;

    @NotNull(message = "Condition type is mandatory")
    private ConditionType conditionType;

    private String conditionValue; // e.g., video_id, survey_id, referral_count

    private Boolean isRequired = true;
}