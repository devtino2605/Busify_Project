package com.busify.project.promotion.specification;

import com.busify.project.promotion.dto.request.PromotionFilterRequestDTO;
import com.busify.project.promotion.entity.Promotion;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PromotionSpecification {

    public static Specification<Promotion> withFilter(PromotionFilterRequestDTO filter) {
        return (Root<Promotion> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search by code only (since Promotion entity doesn't have description field)
            if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty()) {
                String searchPattern = "%" + filter.getSearch().toLowerCase() + "%";
                Predicate codePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("code")), searchPattern);
                predicates.add(codePredicate);
            }

            // Filter by status
            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }

            // Filter by type
            if (filter.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("promotionType"), filter.getType()));
            }

            // Filter by minimum discount value
            if (filter.getMinDiscount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("discountValue"), filter.getMinDiscount()));
            }

            // Filter by maximum discount value
            if (filter.getMaxDiscount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("discountValue"), filter.getMaxDiscount()));
            }

            // Filter by start date (promotions starting after this date)
            if (filter.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("startDate"), filter.getStartDate()));
            }

            // Filter by end date (promotions ending before this date)
            if (filter.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("endDate"), filter.getEndDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
