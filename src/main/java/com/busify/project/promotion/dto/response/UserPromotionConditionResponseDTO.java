package com.busify.project.promotion.dto.response;

import com.busify.project.promotion.enums.ConditionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPromotionConditionResponseDTO {
    private Long userPromotionConditionId;
    private Long promotionId;
    private String promotionCode;
    private String promotionType;
    private Long conditionId;
    private ConditionType conditionType;
    private String conditionValue;
    private Boolean isRequired;
    private Boolean isCompleted;
    private String currentProgress;
    private String completedAt;
}