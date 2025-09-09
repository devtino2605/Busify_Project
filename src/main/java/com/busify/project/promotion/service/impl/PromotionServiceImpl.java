package com.busify.project.promotion.service.impl;

import com.busify.project.promotion.dto.request.PromotionRequesDTO;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.dto.response.UserPromotionResponseDTO;
import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.entity.UserPromotion;
import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.mapper.PromotionMapper;
import com.busify.project.promotion.mapper.UserPromotionMapper;
import com.busify.project.promotion.repository.PromotionRepository;
import com.busify.project.promotion.repository.UserPromotionRepository;
import com.busify.project.promotion.service.PromotionService;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.ProfileRepository;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final UserPromotionRepository userPromotionRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    @Override
    public PromotionResponseDTO createPromotion(PromotionRequesDTO promotion) {
        String code = genneratePromotionCode();
        promotion.setCode(code);
        Promotion convertToPromotion = PromotionMapper.convertToEntity(promotion);
        Promotion savedPromotion = promotionRepository.save(convertToPromotion);

        // Audit log for promotion creation
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("CREATE");
            auditLog.setTargetEntity("PROMOTION");
            auditLog.setTargetId(savedPromotion.getPromotionId());
            auditLog.setDetails(String.format("{\"promotion_id\":%d,\"code\":\"%s\",\"discount_type\":\"%s\",\"discount_value\":%.2f,\"usage_limit\":%d,\"status\":\"%s\",\"action\":\"create\"}", 
                    savedPromotion.getPromotionId(), savedPromotion.getCode(), savedPromotion.getDiscountType(), 
                    savedPromotion.getDiscountValue(), savedPromotion.getUsageLimit(), savedPromotion.getStatus()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for promotion creation: " + e.getMessage());
        }

        return PromotionMapper.convertToDTO(savedPromotion);
    }

    private String genneratePromotionCode() {
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
    public PromotionResponseDTO updatePromotion(Long id, PromotionRequesDTO promotion) {
        Optional<Promotion> existing = promotionRepository.findById(id);
        if (existing.isPresent()) {
            Promotion toUpdate = existing.get();
            String oldStatus = toUpdate.getStatus().toString();
            
            toUpdate.setCode(promotion.getCode());
            toUpdate.setDiscountType(promotion.getDiscountType());
            toUpdate.setDiscountValue(promotion.getDiscountValue());
            toUpdate.setStartDate(promotion.getStartDate());
            toUpdate.setEndDate(promotion.getEndDate());
            toUpdate.setUsageLimit(promotion.getUsageLimit());
            toUpdate.setStatus(promotion.getStatus());

            Promotion updated = promotionRepository.save(toUpdate);

            // Audit log for promotion update
            try {
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("UPDATE");
                auditLog.setTargetEntity("PROMOTION");
                auditLog.setTargetId(updated.getPromotionId());
                auditLog.setDetails(String.format("{\"promotion_id\":%d,\"code\":\"%s\",\"discount_type\":\"%s\",\"discount_value\":%.2f,\"old_status\":\"%s\",\"new_status\":\"%s\",\"action\":\"update\"}", 
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
                auditLog.setDetails(String.format("{\"promotion_id\":%d,\"code\":\"%s\",\"discount_type\":\"%s\",\"discount_value\":%.2f,\"status\":\"%s\",\"action\":\"hard_delete\"}", 
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
    public UserPromotionResponseDTO claimPromotion(Long userId, String promotionCode) {
        // Kiểm tra user tồn tại
        Profile user = profileRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
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
        if (userPromotionRepository.existsByUserIdAndPromotionId(userId, promotion.getPromotionId())) {
            throw new RuntimeException("You have already claimed this promotion");
        }
        
        // Kiểm tra usage limit
        long usedCount = userPromotionRepository.countUsedByPromotionId(promotion.getPromotionId());
        if (usedCount >= promotion.getUsageLimit()) {
            throw new RuntimeException("Promotion usage limit reached");
        }
        
        // Claim promotion cho user
        UserPromotion userPromotion = new UserPromotion(user, promotion);
        UserPromotion saved = userPromotionRepository.save(userPromotion);

        // Audit log for promotion claim
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("CLAIM");
            auditLog.setTargetEntity("USER_PROMOTION");
            auditLog.setTargetId(promotion.getPromotionId()); // Use promotion ID as target
            auditLog.setDetails(String.format("{\"user_id\":%d,\"promotion_id\":%d,\"promotion_code\":\"%s\",\"discount_value\":%.2f,\"action\":\"claim_promotion\"}", 
                    userId, promotion.getPromotionId(), promotion.getCode(), promotion.getDiscountValue()));
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
            auditLog.setDetails(String.format("{\"user_id\":%d,\"promotion_code\":\"%s\",\"promotion_id\":%d,\"discount_value\":%.2f,\"action\":\"mark_as_used\"}", 
                    userId, promotionCode, userPromotion.getPromotion().getPromotionId(), userPromotion.getPromotion().getDiscountValue()));
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
            auditLog.setDetails(String.format("{\"user_id\":%d,\"promotion_code\":\"%s\",\"promotion_id\":%d,\"discount_value\":%.2f,\"action\":\"remove_mark_as_used\"}", 
                    userId, promotionCode, userPromotion.getPromotion().getPromotionId(), userPromotion.getPromotion().getDiscountValue()));
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
}