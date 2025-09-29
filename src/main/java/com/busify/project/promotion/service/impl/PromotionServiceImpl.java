package com.busify.project.promotion.service.impl;

import com.busify.project.common.utils.JwtUtils;
import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.request.PromotionFilterRequestDTO;
import com.busify.project.promotion.dto.request.PromotionConditionRequestDTO;
import com.busify.project.promotion.dto.response.*;
import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.entity.PromotionCondition;
import com.busify.project.promotion.entity.UserPromotion;
import com.busify.project.promotion.entity.UserPromotionCondition;
import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.PromotionType;
import com.busify.project.promotion.mapper.PromotionConditionMapper;
import com.busify.project.promotion.mapper.PromotionMapper;
import com.busify.project.promotion.mapper.UserPromotionConditionMapper;
import com.busify.project.promotion.mapper.UserPromotionMapper;
import com.busify.project.promotion.repository.PromotionRepository;
import com.busify.project.promotion.repository.PromotionConditionRepository;
import com.busify.project.promotion.repository.UserPromotionRepository;
import com.busify.project.promotion.repository.UserPromotionConditionRepository;
import com.busify.project.promotion.service.PromotionService;
import com.busify.project.promotion.specification.PromotionSpecification;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionConditionRepository promotionConditionRepository;
    private final UserPromotionRepository userPromotionRepository;
    private final UserPromotionConditionRepository userPromotionConditionRepository;
    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;
    private final AuditLogService auditLogService;

    @Override
    public PromotionResponseDTO createPromotion(PromotionRequesDTO promotion) {
        // Validate promotion type and code logic
        if (promotion.getPromotionType() == PromotionType.auto) {
            // AUTO promotion → code = null, hệ thống tự áp dụng
            promotion.setCode(null);
        } else if (promotion.getPromotionType() == PromotionType.coupon) {
            // COUPON promotion → code bắt buộc
            if (promotion.getCode() == null || promotion.getCode().trim().isEmpty()) {
                String generatedCode = generatePromotionCode();
                promotion.setCode(generatedCode);
            }
        } else {
            throw new RuntimeException("Invalid promotion type");
        }

        Promotion convertToPromotion = PromotionMapper.convertToEntity(promotion);
        Promotion savedPromotion = promotionRepository.save(convertToPromotion);
        log.info("Saved promotion: {}", savedPromotion);

        // Save conditions if provided
        if (promotion.getConditions() != null && !promotion.getConditions().isEmpty()) {
            for (PromotionConditionRequestDTO conditionDTO : promotion.getConditions()) {
                PromotionCondition condition = new PromotionCondition();
                condition.setPromotion(savedPromotion);
                condition.setConditionType(conditionDTO.getConditionType());
                condition.setConditionValue(conditionDTO.getConditionValue());
                condition.setIsRequired(conditionDTO.getIsRequired() != null ? conditionDTO.getIsRequired() : true);
                promotionConditionRepository.save(condition);
            }
        }

        // Audit log for promotion creation
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("CREATE");
            auditLog.setTargetEntity("PROMOTION");
            auditLog.setTargetId(savedPromotion.getPromotionId());
            auditLog.setDetails(String.format(
                    "{\"promotion_id\":%d,\"code\":\"%s\",\"discount_type\":\"%s\",\"discount_value\":%.2f,\"usage_limit\":%d,\"status\":\"%s\",\"action\":\"create\"}",
                    savedPromotion.getPromotionId(), savedPromotion.getCode(), savedPromotion.getDiscountType(),
                    savedPromotion.getDiscountValue(), savedPromotion.getUsageLimit(), savedPromotion.getStatus()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for promotion creation: " + e.getMessage());
        }

        return PromotionMapper.convertToDTO(savedPromotion);
    }

    private String generatePromotionCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }

    @Override
    public PromotionResponseDTO getPromotionById(Long id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        return promotion.map(PromotionMapper::convertToDTO).orElse(null);
    }

    @Override
    public PromotionResponseDTO getPromotionByCode(String code) {

        Optional<Promotion> promotion = promotionRepository.findByCode(code);
        return promotion.map(PromotionMapper::convertToDTO).orElse(null);
    }

    @Override
    public List<PromotionResponseDTO> getAllPromotions() {

        List<Promotion> promotions = promotionRepository.findAll();
        return promotions.stream()
                .map(PromotionMapper::convertToDTO)
                .toList();
    }

    @Override
    public PromotionFilterResponseDTO filterPromotions(PromotionFilterRequestDTO filter, int page, int size) {
        // Tạo Pageable với sort theo id desc (mới nhất trước)
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("promotionId").descending());

        // Sử dụng Specification để filter
        Page<Promotion> promotions = promotionRepository.findAll(
                PromotionSpecification.withFilter(filter),
                pageable);

        // Convert sang DTO
        List<PromotionResponseDTO> promotionDTOs = promotions.getContent().stream()
                .map(PromotionMapper::convertToDTO)
                .toList();

        // Trả về response với thông tin phân trang
        return PromotionFilterResponseDTO.builder()
                .promotions(promotionDTOs)
                .currentPage(promotions.getNumber() + 1) // Convert từ 0-based sang 1-based
                .totalPages(promotions.getTotalPages())
                .totalElements(promotions.getTotalElements())
                .pageSize(promotions.getSize())
                .hasNext(promotions.hasNext())
                .hasPrevious(promotions.hasPrevious())
                .build();
    }

    @Override
    @Transactional
    public PromotionResponseDTO updatePromotion(Long id, PromotionRequesDTO promotion) {
        Optional<Promotion> existing = promotionRepository.findById(id);
        if (existing.isPresent()) {
            Promotion toUpdate = existing.get();
            toUpdate.setCode(existing.get().getCode());
            toUpdate.setPromotionType(promotion.getPromotionType());
            String oldStatus = toUpdate.getStatus().toString();
            toUpdate.setDiscountType(promotion.getDiscountType());
            toUpdate.setDiscountValue(promotion.getDiscountValue());
            toUpdate.setMinOrderValue(promotion.getMinOrderValue());
            toUpdate.setStartDate(promotion.getStartDate());
            toUpdate.setEndDate(promotion.getEndDate());
            toUpdate.setUsageLimit(promotion.getUsageLimit());
            toUpdate.setPriority(promotion.getPriority());
            toUpdate.setStatus(promotion.getStatus());

            Promotion updated = promotionRepository.save(toUpdate);

            // Update conditions if provided
            if (promotion.getConditions() != null) {
                // Validate trước khi update conditions
                validateConditionUpdates(updated.getPromotionId(), promotion.getConditions());

                log.info("conditions to update: {}", promotion.getConditions());

                // Get existing conditions
                List<PromotionCondition> existingConditions = promotionConditionRepository
                        .findPromotionByPromotionId(updated.getPromotionId());

                log.info("Existing conditions count: {}", existingConditions.size());
                for (PromotionCondition existingcondition : existingConditions) {
                    log.info("Existing condition: ID={}, Type={}", existingcondition.getId(),
                            existingcondition.getConditionType());
                }

                // Collect IDs of conditions in the request
                List<Long> requestConditionIds = promotion.getConditions().stream()
                        .map(PromotionConditionRequestDTO::getConditionId)
                        .filter(conditionId -> conditionId != null)
                        .toList();

                log.info("Request condition IDs: {}", requestConditionIds);

                // Process conditions from request
                for (PromotionConditionRequestDTO conditionDTO : promotion.getConditions()) {
                    if (conditionDTO.getConditionId() != null) {
                        // Update existing condition
                        Optional<PromotionCondition> promotionConditionOpt = promotionConditionRepository
                                .findById(conditionDTO.getConditionId());
                        if (promotionConditionOpt.isPresent()) {
                            PromotionCondition promotionCondition = promotionConditionOpt.get();
                            promotionCondition.setConditionType(conditionDTO.getConditionType());
                            promotionCondition.setConditionValue(conditionDTO.getConditionValue());
                            promotionCondition.setIsRequired(
                                    conditionDTO.getIsRequired() != null ? conditionDTO.getIsRequired() : true);
                            promotionConditionRepository.save(promotionCondition);
                        } else {
                            throw new RuntimeException("Condition not found with ID: " + conditionDTO.getConditionId());
                        }
                    } else {
                        // Create new condition
                        PromotionCondition condition = new PromotionCondition();
                        condition.setPromotion(updated);
                        condition.setConditionType(conditionDTO.getConditionType());
                        condition.setConditionValue(conditionDTO.getConditionValue());
                        condition.setIsRequired(
                                conditionDTO.getIsRequired() != null ? conditionDTO.getIsRequired() : true);
                        promotionConditionRepository.save(condition);
                    }
                }

                // Delete conditions that are no longer in the request
                for (PromotionCondition existingCondition : existingConditions) {
                    if (!requestConditionIds.contains(existingCondition.getId())) {
                        log.info("Deleting condition ID: {}", existingCondition.getId());
                        // Check if any user is working on this condition
                        if (hasUsersWorkingOnCondition(existingCondition.getId())) {
                            throw new RuntimeException("Cannot delete condition with ID " + existingCondition.getId() +
                                    " because users are currently working on it");
                        }

                        // Delete all user progress for this condition first
                        deleteUserProgressForCondition(existingCondition.getId());

                        Long existConditionId = existingCondition.getId();
                        log.info("Existing condition ID: {}", existConditionId);
                        if (existConditionId == null) {
                            log.warn("ViewCount was null for post {}, setting to 0", existConditionId);
                        } else {
                            // Then delete the condition
                            promotionConditionRepository.deleteConditionById(existConditionId);
                        }
                    }
                }
            }

            // Audit log for promotion update
            try {
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("UPDATE");
                auditLog.setTargetEntity("PROMOTION");
                auditLog.setTargetId(updated.getPromotionId());
                auditLog.setDetails(String.format(
                        "{\"promotion_id\":%d,\"code\":\"%s\",\"discount_type\":\"%s\",\"discount_value\":%.2f,\"old_status\":\"%s\",\"new_status\":\"%s\",\"action\":\"update\"}",
                        updated.getPromotionId(), updated.getCode(), updated.getDiscountType(),
                        updated.getDiscountValue(), oldStatus, updated.getStatus()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);
            } catch (Exception e) {
                System.err.println("Failed to create audit log for promotion update: " + e.getMessage());
            }

            return PromotionMapper.convertToDTO(updated);
        }
        return null;
    }

    @Override
    public void deletePromotion(Long id) {
        // Get promotion details before deletion for audit log
        Optional<Promotion> promotionOpt = promotionRepository.findById(id);

        if (promotionOpt.isPresent()) {
            Promotion promotion = promotionOpt.get();

            // Audit log for promotion deletion (before deletion)
            try {
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("DELETE");
                auditLog.setTargetEntity("PROMOTION");
                auditLog.setTargetId(promotion.getPromotionId());
                auditLog.setDetails(String.format(
                        "{\"promotion_id\":%d,\"code\":\"%s\",\"discount_type\":\"%s\",\"discount_value\":%.2f,\"status\":\"%s\",\"action\":\"hard_delete\"}",
                        promotion.getPromotionId(), promotion.getCode(), promotion.getDiscountType(),
                        promotion.getDiscountValue(), promotion.getStatus()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);
            } catch (Exception e) {
                System.err.println("Failed to create audit log for promotion deletion: " + e.getMessage());
            }
        }

        promotionRepository.deleteById(id);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *") // Chạy mỗi ngày lúc 0h
    public void updateStatusExpiredPromotions() {
        List<Promotion> expiredPromotions = promotionRepository.findAllExpiredButNotUpdated();
        for (Promotion promotion : expiredPromotions) {
            promotion.setStatus(PromotionStatus.expired);
        }
        if (!expiredPromotions.isEmpty()) {
            promotionRepository.saveAll(expiredPromotions);
        }
    }

    @Override
    public UserPromotionResponseDTO claimPromotion(String promotionCode, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userId));

        // Kiểm tra promotion tồn tại và còn hiệu lực
        Promotion promotion = promotionRepository.findByCode(promotionCode)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        // Kiểm tra promotion còn hiệu lực
        if (promotion.getStatus() != PromotionStatus.active) {
            throw new RuntimeException("Promotion is not active");
        }

        if (promotion.getEndDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Promotion has expired");
        }

        if (promotion.getStartDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Promotion is not yet available");
        }

        // Kiểm tra user đã claim promotion này chưa
        if (userPromotionRepository.existsByUserIdAndPromotionId(user.getId(), promotion.getPromotionId())) {
            throw new RuntimeException("You have already claimed this promotion");
        }

        // Kiểm tra usage limit (chỉ check khi có giới hạn)
        if (promotion.getUsageLimit() != null && promotion.getUsageLimit() > 0) {
            long usedCount = userPromotionRepository.countUsedByPromotionId(promotion.getPromotionId());
            if (usedCount >= promotion.getUsageLimit()) {
                throw new RuntimeException("Promotion usage limit reached");
            }
        }

        // Kiểm tra tất cả conditions đã được meet chưa
        if (!areAllConditionsMet(user.getId(), promotion.getPromotionId())) {
            throw new RuntimeException("All promotion conditions must be met before claiming");
        }

        // Claim promotion cho user
        UserPromotion userPromotion = new UserPromotion((Profile) user, promotion);
        UserPromotion saved = userPromotionRepository.save(userPromotion);

        // Audit log for promotion claim
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("CLAIM");
            auditLog.setTargetEntity("USER_PROMOTION");
            auditLog.setTargetId(promotion.getPromotionId()); // Use promotion ID as target
            auditLog.setDetails(String.format(
                    "{\"user_id\":%d,\"promotion_id\":%d,\"promotion_code\":\"%s\",\"discount_value\":%.2f,\"action\":\"claim_promotion\"}",
                    promotion.getPromotionId(), promotion.getCode(), promotion.getDiscountValue()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for promotion claim: " + e.getMessage());
        }

        return UserPromotionMapper.convertToDTO(saved);
    }

    @Override
    public List<UserPromotionResponseDTO> getUserPromotions(Long userId) {
        List<UserPromotion> userPromotions = userPromotionRepository.findAllByUserId(userId);
        return userPromotions.stream()
                .map(UserPromotionMapper::convertToDTO)
                .toList();
    }

    @Override
    public List<UserPromotionResponseDTO> getUserAvailablePromotions(Long userId) {
        List<UserPromotion> availablePromotions = userPromotionRepository.findUnusedPromotionsByUserId(userId);
        return availablePromotions.stream()
                .map(UserPromotionMapper::convertToDTO)
                .toList();
    }

    @Override
    public List<UserPromotionResponseDTO> getUserUsedPromotions() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        List<UserPromotion> usedPromotions = userPromotionRepository.findUsedPromotionsByUserId(user.getId());
        return usedPromotions.stream()
                .map(UserPromotionMapper::convertToDTO)
                .toList();
    }

    @Override
    public boolean canUsePromotion(Long userId, String promotionCode) {
        Optional<UserPromotion> userPromotion = userPromotionRepository
                .findAvailablePromotionForUser(userId, promotionCode);

        if (userPromotion.isEmpty()) {
            return false;
        }

        Promotion promotion = userPromotion.get().getPromotion();

        // Kiểm tra promotion còn hiệu lực
        return promotion.getStatus() == PromotionStatus.active &&
                !promotion.getEndDate().isBefore(LocalDate.now()) &&
                !promotion.getStartDate().isAfter(LocalDate.now());
    }

    @Override
    public void markPromotionAsUsed(Long userId, String promotionCode) {
        UserPromotion userPromotion = userPromotionRepository
                .findAvailablePromotionForUser(userId, promotionCode)
                .orElseThrow(() -> new RuntimeException("Promotion not available for this user"));

        userPromotion.markAsUsed();
        userPromotionRepository.save(userPromotion);

        // Audit log for promotion usage
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("USE");
            auditLog.setTargetEntity("USER_PROMOTION");
            auditLog.setTargetId(userPromotion.getPromotion().getPromotionId());
            auditLog.setDetails(String.format(
                    "{\"user_id\":%d,\"promotion_code\":\"%s\",\"promotion_id\":%d,\"discount_value\":%.2f,\"action\":\"mark_as_used\"}",
                    userId, promotionCode, userPromotion.getPromotion().getPromotionId(),
                    userPromotion.getPromotion().getDiscountValue()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for promotion usage: " + e.getMessage());
        }
    }

    @Override
    public void removeMarkPromotionAsUsed(Long userId, String promotionCode) {
        UserPromotion userPromotion = userPromotionRepository
                .findUsedPromotionForUser(userId, promotionCode)
                .orElseThrow(() -> new RuntimeException("Promotion not found for this user"));

        userPromotion.removeMarkAsUsed();
        userPromotionRepository.save(userPromotion);

        // Audit log for promotion un-usage
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("UNUSE");
            auditLog.setTargetEntity("USER_PROMOTION");
            auditLog.setTargetId(userPromotion.getPromotion().getPromotionId());
            auditLog.setDetails(String.format(
                    "{\"user_id\":%d,\"promotion_code\":\"%s\",\"promotion_id\":%d,\"discount_value\":%.2f,\"action\":\"remove_mark_as_used\"}",
                    userId, promotionCode, userPromotion.getPromotion().getPromotionId(),
                    userPromotion.getPromotion().getDiscountValue()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for promotion un-usage: " + e.getMessage());
        }
    }

    // Helper method to get current user from SecurityContext
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("No authenticated user found");
        }

        String email = authentication.getName();
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    @Override
    public List<PromotionResponseDTO> findActiveAutoPromotions(BigDecimal orderValue) {
        List<Promotion> autoPromotions = promotionRepository.findActiveAutoPromotions(orderValue);
        return autoPromotions.stream()
                .map(PromotionMapper::convertToDTO)
                .toList();
    }

    @Override
    public PromotionResponseDTO findBestAutoPromotion(BigDecimal orderValue) {
        List<Promotion> autoPromotions = promotionRepository.findActiveAutoPromotions(orderValue);

        if (autoPromotions.isEmpty()) {
            return null;
        }

        // Trả về promotion đầu tiên (đã được sort theo priority và discount value)
        Promotion bestPromotion = autoPromotions.get(0);
        return PromotionMapper.convertToDTO(bestPromotion);
    }

    @Override
    public PromotionResponseDTO validateAndApplyPromotion(Long userId, String promotionCode, BigDecimal orderValue) {
        if (promotionCode == null || promotionCode.trim().isEmpty()) {
            throw new RuntimeException("Promotion code is required");
        }

        // Tìm Promotion theo code + check điều kiện
        Promotion promotion = promotionRepository.findByCode(promotionCode)
                .orElseThrow(() -> new RuntimeException("Promotion code '" + promotionCode + "' not found"));

        // Validate promotion
        validatePromotionConditions(promotion, orderValue);

        if (promotion.getPromotionType() == PromotionType.coupon) {
            // COUPON: check user đã claim và chưa sử dụng
            if (!canUsePromotion(userId, promotionCode)) {
                throw new RuntimeException("Promotion not available for this user");
            }
        }

        return PromotionMapper.convertToDTO(promotion);
    }

    private void validatePromotionConditions(Promotion promotion, BigDecimal orderValue) {
        // Check status
        if (promotion.getStatus() != PromotionStatus.active) {
            throw new RuntimeException("Promotion is not active");
        }

        // Check time validity
        LocalDate now = LocalDate.now();
        if (promotion.getEndDate().isBefore(now)) {
            throw new RuntimeException("Promotion has expired");
        }

        if (promotion.getStartDate().isAfter(now)) {
            throw new RuntimeException("Promotion is not yet available");
        }

        // Check minimum order value
        if (promotion.getMinOrderValue() != null &&
                orderValue.compareTo(promotion.getMinOrderValue()) < 0) {
            throw new RuntimeException("Order value does not meet minimum requirement");
        }

        // Check usage limit
        if (promotion.getUsageLimit() != null && promotion.getUsageLimit() > 0) {
            long usedCount = userPromotionRepository.countUsedByPromotionId(promotion.getPromotionId());
            if (usedCount >= promotion.getUsageLimit()) {
                throw new RuntimeException("Promotion usage limit reached");
            }
        }
    }

    @Override
    public PromotionResponseDTO validateAndApplyPromotionById(Long userId, Long promotionId, BigDecimal orderValue) {
        if (promotionId == null) {
            throw new RuntimeException("Promotion ID is required");
        }

        // Tìm promotion theo ID
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + promotionId));

        // Validate promotion
        validatePromotionConditions(promotion, orderValue);

        if (promotion.getPromotionType() == PromotionType.coupon) {
            // COUPON: check user đã claim và chưa sử dụng
            if (!canUsePromotion(userId, promotion.getCode())) {
                throw new RuntimeException("Promotion not available for this user");
            }
        } else if (promotion.getPromotionType() == PromotionType.auto) {
            // AUTO: check user chưa sử dụng promotion này (1 lần/user)
            if (hasUserUsedAutoPromotion(userId, promotion.getPromotionId())) {
                throw new RuntimeException("You have already used this promotion");
            }

            // AUTO: check tất cả conditions đã được meet chưa
            if (!areAllConditionsMet(userId, promotion.getPromotionId())) {
                throw new RuntimeException("All promotion conditions must be met before applying this promotion");
            }
        }

        return PromotionMapper.convertToDTO(promotion);
    }

    /**
     * Check if user has already used an AUTO promotion (1 time per user limit)
     */
    private boolean hasUserUsedAutoPromotion(Long userId, Long promotionId) {
        // Check if user has a used record for this AUTO promotion
        return userPromotionRepository.existsByUserIdAndPromotionIdAndIsUsed(userId, promotionId, true);
    }

    @Override
    public void createAndMarkAutoPromotionAsUsed(Long userId, Long promotionId) {
        UserPromotion userPromotion = userPromotionRepository.findByUserIdAndPromotionIdAndIsUsed(userId, promotionId);
        if(userPromotion != null) {
            userPromotion.setIsUsed(true); // Mark as used immediately
            userPromotion.setUsedAt(LocalDateTime.now());
        }else{
            // Create new UserPromotion record and mark as used
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            Promotion promotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + promotionId));
            userPromotion = new UserPromotion((Profile) user, promotion);
            userPromotion.setClaimedAt(LocalDateTime.now());
            userPromotion.setIsUsed(true);
            userPromotion.setUsedAt(LocalDateTime.now());
        }

        userPromotionRepository.save(userPromotion);
    }

    @Override
    @Transactional
    public void removeAutoPromotionUsage(Long userId, Long promotionId) {
        // Delete UserPromotion record for AUTO promotions to allow reuse
        userPromotionRepository.deleteByUserIdAndPromotionIdAndIsUsed(userId, promotionId, true);
    }

    @Override
    public List<PromotionResponseDTO> getAllCurrentPromotions() {
        List<Promotion> currentPromotions = promotionRepository.findAllCurrentPromotions();
        return currentPromotions.stream()
                .map(PromotionMapper::convertToDTO)
                .toList();
    }

    // Implementation of condition-related methods
    @Override
    public boolean areAllConditionsMet(Long userId, Long promotionId) {
        List<PromotionCondition> conditions = promotionConditionRepository.findPromotionByPromotionId(promotionId);
        for (PromotionCondition condition : conditions) {
            if (condition.getIsRequired()) {
                boolean isCompleted = userPromotionConditionRepository
                        .existsByUserIdAndConditionIdAdIsCompleted(userId, condition.getId(), true);
                if (!isCompleted) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void updateConditionProgress(Long conditionId, String progressData) {

        // Get current user
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Long userId = user.getId();

        UserPromotionCondition userCondition = userPromotionConditionRepository
                .findUserPromotionConditionByUserAndConditionId(userId, conditionId)
                .orElseGet(() -> {
                    // Create new if not exists
                    UserPromotionCondition newCondition = new UserPromotionCondition();
                    Profile userProfile = userRepository.findById(userId).map(u -> (Profile) u).orElseThrow();
                    PromotionCondition condition = promotionConditionRepository.findById(conditionId).orElseThrow();
                    newCondition.setUser(userProfile);
                    newCondition.setPromotionCondition(condition);
                    newCondition.setIsCompleted(false);
                    return newCondition;
                });

        // Update progress
        userCondition.setProgressData(progressData);

        // Check if condition is met based on type and progress
        boolean isCompleted = checkConditionCompleted(userCondition.getPromotionCondition(), progressData);
        if (isCompleted && !userCondition.getIsCompleted()) {
            userCondition.setIsCompleted(true);
            userCondition.setCompletedAt(LocalDateTime.now());
        }

        // Save condition first
        userPromotionConditionRepository.save(userCondition);

        // Then check for auto-claiming after saving
        if (isCompleted && userCondition.getIsCompleted()) {
            autoClaimEligiblePromotions(userId);
        }
    }

    private boolean checkConditionCompleted(PromotionCondition condition, String progressData) {
        // Simple implementation - in real app, this would validate based on external
        // APIs
        switch (condition.getConditionType()) {
            case VIEW_VIDEO:
                return progressData != null && progressData.contains("\"viewed\":true");
            case COMPLETE_SURVEY:
                return progressData != null && progressData.contains("\"completed\":true");
            case REFERRAL_COUNT:
                // Assume progressData contains referral count
                try {
                    int count = Integer.parseInt(progressData);
                    int required = Integer.parseInt(condition.getConditionValue());
                    return count >= required;
                } catch (NumberFormatException e) {
                    return false;
                }
            case FIRST_PURCHASE:
                return progressData != null && progressData.contains("\"purchased\":true");
            case WATCH_AD:
                return progressData != null && progressData.contains("\"watched\":true");
            default:
                return false;
        }
    }

    @Override
    public List<PromotionConditionResponseDTO> getPromotionConditions(Long promotionId) {
        List<PromotionCondition> promotionConditions = promotionConditionRepository
                .findPromotionByPromotionId(promotionId);
        return promotionConditions.stream()
                .map(PromotionConditionMapper::toResponseDTO)
                .toList();
    }

    @Override
    public void autoClaimEligiblePromotions(Long userId) {
        try {
            // Step 1: Find all eligible AUTO promotions with conditions
            List<Promotion> eligibleAutoPromotions = findEligibleAutoPromotionsWithConditions();

            if (eligibleAutoPromotions.isEmpty()) {
                System.out.println("No eligible AUTO promotions found for auto-claiming");
                return;
            }

            // Step 2: Get user profile
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            if (!(user instanceof Profile profile)) {
                throw new RuntimeException("User must be a Profile instance for auto-claiming");
            }

            // Step 3: Process each eligible promotion
            int claimedCount = 0;
            for (Promotion promotion : eligibleAutoPromotions) {
                try {
                    if (canAutoClaimPromotion(userId, promotion)) {
                        if (promotion.getCode() != null && promotion.getPromotionType() == PromotionType.coupon) {
                            claimPromotion(promotion.getCode(), userId);
                            claimedCount++;
                        } else {
                            autoClaimSinglePromotion(profile, promotion);
                            claimedCount++;
                        }

                        System.out.println("Successfully auto-claimed promotion: " +
                                (promotion.getCode() != null ? promotion.getCode()
                                        : "AUTO-" + promotion.getPromotionId())
                                +
                                " for user: " + userId);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to auto-claim promotion " + promotion.getPromotionId() +
                            " for user " + userId + ": " + e.getMessage());
                }
            }

            System.out.println("Auto-claim process completed. Total claimed: " + claimedCount + " promotions");

        } catch (Exception e) {
            System.err.println("Error in autoClaimEligiblePromotions for user " + userId + ": " + e.getMessage());
        }
    }

    @Override
    public List<UserPromotionConditionResponseDTO> getAllUserPromotionConditions() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Long userId = user.getId();

        List<UserPromotionCondition> userConditions = userPromotionConditionRepository
                .findAllPromotionConditionsByUserId(userId);

        return userConditions.stream()
                .map(UserPromotionConditionMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<PromotionResponseDTO> getAutoPromotionsWithCompletedConditions() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Long userId = user.getId();

        // Use new repository query to get auto promotions with completed conditions
        List<Promotion> eligiblePromotions = promotionRepository
                .findAutoPromotionsWithCompletedConditionsForUser(userId);

        return eligiblePromotions.stream()
                .map(PromotionMapper::convertToDTO)
                .toList();
    }

    /**
     * Find all active AUTO promotions that have conditions and are currently valid
     */
    private List<Promotion> findEligibleAutoPromotionsWithConditions() {
        LocalDate today = LocalDate.now();

        // Use repository method for better performance instead of findAll + filter
        return promotionRepository.findAll().stream()
                .filter(p -> !p.getEndDate().isBefore(today))
                .filter(p -> !p.getStartDate().isAfter(today))
                .filter(p -> hasConditions(p.getPromotionId()))
                .toList();
    }

    /**
     * Check if promotion has any conditions
     */
    private boolean hasConditions(Long promotionId) {
        return !promotionConditionRepository.findPromotionByPromotionId(promotionId).isEmpty();
    }

    /**
     * Check if user can auto-claim a specific promotion
     */
    private boolean canAutoClaimPromotion(Long userId, Promotion promotion) {
        // 1. Check if user already claimed this promotion
        if (userPromotionRepository.existsByUserIdAndPromotionId(userId, promotion.getPromotionId())) {
            System.out.println("User " + userId + " already claimed promotion " + promotion.getPromotionId());
            return false;
        }

        // 2. Check if all conditions are met
        if (!areAllConditionsMet(userId, promotion.getPromotionId())) {
            System.out.println(
                    "User " + userId + " hasn't met all conditions for promotion " + promotion.getPromotionId());
            return false;
        }

        // 3. Check usage limit (if exists)
        if (promotion.getUsageLimit() != null && promotion.getUsageLimit() > 0) {
            long currentUsageCount = userPromotionRepository.countUsedByPromotionId(promotion.getPromotionId());
            if (currentUsageCount >= promotion.getUsageLimit()) {
                System.out.println("Promotion " + promotion.getPromotionId() + " has reached usage limit");
                return false;
            }
        }

        return true;
    }

    /**
     * Auto-claim a single promotion for user
     */
    private void autoClaimSinglePromotion(Profile profile, Promotion promotion) {
        UserPromotion userPromotion = new UserPromotion(profile, promotion);
        userPromotionRepository.save(userPromotion);

        // Create audit log for auto-claim action
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("AUTO_CLAIM");
            auditLog.setTargetEntity("USER_PROMOTION");
            auditLog.setTargetId(promotion.getPromotionId());
            auditLog.setDetails(String.format(
                    "{\"user_id\":%d,\"promotion_id\":%d,\"promotion_code\":\"%s\",\"discount_value\":%.2f,\"action\":\"auto_claim_promotion\"}",
                    profile.getId(), promotion.getPromotionId(),
                    promotion.getCode() != null ? promotion.getCode() : "AUTO-" + promotion.getPromotionId(),
                    promotion.getDiscountValue()));
            auditLog.setUser(profile);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for auto-claim: " + e.getMessage());
        }
    }

    /**
     * Validate trước khi update conditions của promotion
     * Kiểm tra xem có thể thay đổi conditions không khi có user đang thực hiện
     */
    private void validateConditionUpdates(Long promotionId, List<PromotionConditionRequestDTO> newConditions) {
        // Kiểm tra promotion có đang trong thời gian active không
        Promotion promotion = promotionRepository.findById(promotionId).orElseThrow();
        LocalDate today = LocalDate.now();

        boolean isActivePromotion = promotion.getStatus() == PromotionStatus.active &&
                !promotion.getEndDate().isBefore(today) &&
                !promotion.getStartDate().isAfter(today);

        if (!isActivePromotion) {
            // Nếu promotion không active thì có thể update thoải mái
            return;
        }

        // Get existing conditions
        List<PromotionCondition> existingConditions = promotionConditionRepository
                .findPromotionByPromotionId(promotionId);

        log.info("Validating condition updates for promotion {}: existingConditions={}, newConditions={}",
                promotionId, existingConditions, newConditions.size());

        // Collect IDs from request
        List<Long> requestConditionIds = newConditions.stream()
                .map(PromotionConditionRequestDTO::getConditionId)
                .filter(conditionId -> conditionId != null)
                .toList();

        log.info("Request condition IDs: {}", requestConditionIds);

        // Check for conditions being deleted or significantly modified
        for (PromotionCondition existingCondition : existingConditions) {
            if (!requestConditionIds.contains(existingCondition.getId())) {
                // Condition sẽ bị xóa - check có user nào đang làm không
                if (hasUsersWorkingOnCondition(existingCondition.getId())) {
                    throw new RuntimeException("Cannot delete condition '" + existingCondition.getConditionType() +
                            "' because users are currently working on it. Please wait until all users complete this condition.");
                }
            } else {
                // Condition sẽ bị update - check có thay đổi quan trọng không
                PromotionConditionRequestDTO newCondition = newConditions.stream()
                        .filter(c -> existingCondition.getId().equals(c.getConditionId()))
                        .findFirst()
                        .orElse(null);

                if (newCondition != null && hasSignificantChanges(existingCondition, newCondition)) {
                    if (hasUsersWorkingOnCondition(existingCondition.getId())) {
                        throw new RuntimeException("Cannot modify condition '" + existingCondition.getConditionType() +
                                "' significantly because users are currently working on it. Changes: type or value modification.");
                    }
                }
            }
        }
    }

    /**
     * Kiểm tra có user nào đang làm condition này không
     */
    private boolean hasUsersWorkingOnCondition(Long conditionId) {
        List<UserPromotionCondition> userConditions = userPromotionConditionRepository.findAll()
                .stream()
                .filter(upc -> upc.getPromotionCondition().getId().equals(conditionId))
                .filter(upc -> !upc.getIsCompleted()) // Chỉ check những user chưa hoàn thành
                .toList();

        return !userConditions.isEmpty();
    }

    /**
     * Kiểm tra có thay đổi quan trọng trong condition không
     */
    private boolean hasSignificantChanges(PromotionCondition existing, PromotionConditionRequestDTO newCondition) {
        // Thay đổi type hoặc value được coi là significant
        return !existing.getConditionType().equals(newCondition.getConditionType()) ||
                !existing.getConditionValue().equals(newCondition.getConditionValue());
    }

    /**
     * Xóa tất cả progress của user cho condition này
     */
    private void deleteUserProgressForCondition(Long conditionId) {
        List<UserPromotionCondition> userConditions = userPromotionConditionRepository.findAll()
                .stream()
                .filter(upc -> upc.getPromotionCondition().getId().equals(conditionId))
                .toList();

        if (!userConditions.isEmpty()) {
            userPromotionConditionRepository.deleteAll(userConditions);
            System.out.println(
                    "Deleted " + userConditions.size() + " user progress records for condition " + conditionId);
        }
    }
}
