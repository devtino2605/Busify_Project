package com.busify.project.promotion.dto.response;

import com.busify.project.promotion.enums.ConditionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionConditionResponseDTO {
    private Long conditionId;
    private ConditionType conditionType;
    private String conditionValue;
    private Boolean isRequired;
}
