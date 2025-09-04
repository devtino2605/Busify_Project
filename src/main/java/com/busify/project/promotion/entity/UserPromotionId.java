package com.busify.project.promotion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPromotionId implements Serializable {
    private Long userId;
    private Long promotionId;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPromotionId that = (UserPromotionId) o;
        return userId.equals(that.userId) && promotionId.equals(that.promotionId);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(userId, promotionId);
    }
}