package com.busify.project.promotion.service.impl;

import com.busify.project.common.utils.JwtUtils;
import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.request.PromotionFilterRequestDTO;
import com.busify.project.promotion.dto.response.PromotionFilterResponseDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.dto.response.UserPromotionResponseDTO;
import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.entity.UserPromotion;
import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.enums.PromotionType;
import com.busify.project.promotion.mapper.PromotionMapper;
import com.busify.project.promotion.mapper.UserPromotionMapper;
import com.busify.project.promotion.repository.PromotionRepository;
import com.busify.project.promotion.repository.UserPromotionRepository;
import com.busify.project.promotion.service.PromotionService;
import com.busify.project.promotion.specification.PromotionSpecification;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
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

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final UserPromotionRepository userPromotionRepository;
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
    public PromotionResponseDTO updatePromotion(Long id, PromotionRequesDTO promotion) {
        Optional<Promotion> existing = promotionRepository.findById(id);
        if (existing.isPresent()) {
            Promotion toUpdate = existing.get();
            toUpdate.setCode(existing.get().getCode());
            toUpdate.setPromotionType(promotion.getPromotionType());
            String oldStatus = toUpdate.getStatus().toString();

            toUpdate.setCode(promotion.getCode());
            toUpdate.setDiscountType(promotion.getDiscountType());
            toUpdate.setDiscountValue(promotion.getDiscountValue());
            toUpdate.setMinOrderValue(promotion.getMinOrderValue());
            toUpdate.setStartDate(promotion.getStartDate());
            toUpdate.setEndDate(promotion.getEndDate());
            toUpdate.setUsageLimit(promotion.getUsageLimit());
            toUpdate.setPriority(promotion.getPriority());
            toUpdate.setStatus(promotion.getStatus());

            Promotion updated = promotionRepository.save(toUpdate);

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
    public UserPromotionResponseDTO claimPromotion(String promotionCode) {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

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
        // Find promotion
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + promotionId));

        // Find user (cast to Profile as required by UserPromotion)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (!(user instanceof Profile)) {
            throw new RuntimeException("User must be a Profile instance");
        }
        Profile profile = (Profile) user;

        // Create UserPromotion record and mark as used immediately
        UserPromotion userPromotion = new UserPromotion();
        userPromotion.setUserId(userId);
        userPromotion.setPromotionId(promotionId);
        userPromotion.setUser(profile);
        userPromotion.setPromotion(promotion);
        userPromotion.setIsUsed(true); // Mark as used immediately
        userPromotion.setClaimedAt(LocalDateTime.now());
        userPromotion.setUsedAt(LocalDateTime.now());

        userPromotionRepository.save(userPromotion);
    }

    @Override
    @Transactional
    public void removeAutoPromotionUsage(Long userId, Long promotionId) {
        // Delete UserPromotion record for AUTO promotions to allow reuse
        userPromotionRepository.deleteByUserIdAndPromotionIdAndIsUsed(userId, promotionId, true);
    }
}
