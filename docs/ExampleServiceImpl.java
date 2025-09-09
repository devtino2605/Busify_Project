package com.busify.project.common.template;

/**
 * TEMPLATE SERVICE CLASS - HƯỚNG DẪN THÊM AUDIT_LOG VÀO SERVICE
 * 
 * Thực hiện theo các bước sau để thêm audit_log vào bất kỳ service nào:
 * 
 * BƯỚC 1: THÊM IMPORTS
 */

import com.busify.project.common.utils.AuditLogHelper;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

/**
 * BƯỚC 2: THÊM DEPENDENCIES VÀO CONSTRUCTOR
 */
@Service
@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService {
    
    // Existing dependencies
    private final ExampleRepository exampleRepository;
    
    // NEW: Thêm các dependencies cho audit_log
    private final AuditLogHelper auditLogHelper;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

/**
 * BƯỚC 3: THÊM AUDIT LOG VÀO CÁC METHOD CREATE, UPDATE, DELETE
 */

    @Override
    public ExampleDTO createExample(ExampleRequestDTO request) {
        // Business logic...
        Example entity = new Example();
        // set properties...
        Example savedEntity = exampleRepository.save(entity);
        
        // AUDIT LOG FOR CREATE
        User currentUser = getCurrentUser();
        auditLogHelper.logCreate("EXAMPLE", savedEntity.getId(), 
            String.format("{\"name\":\"%s\",\"status\":\"%s\"}", 
                savedEntity.getName(), savedEntity.getStatus()),
            currentUser);
        
        return ExampleMapper.toDTO(savedEntity);
    }

    @Override
    public ExampleDTO updateExample(Long id, ExampleRequestDTO request) {
        Example entity = exampleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Example not found"));
            
        // Store old values for audit
        String oldName = entity.getName();
        String oldStatus = entity.getStatus().toString();
        
        // Update entity...
        entity.setName(request.getName());
        entity.setStatus(request.getStatus());
        Example updatedEntity = exampleRepository.save(entity);
        
        // AUDIT LOG FOR UPDATE
        User currentUser = getCurrentUser();
        auditLogHelper.logUpdate("EXAMPLE", id, 
            String.format("{\"oldName\":\"%s\",\"newName\":\"%s\",\"oldStatus\":\"%s\",\"newStatus\":\"%s\"}", 
                oldName, updatedEntity.getName(), oldStatus, updatedEntity.getStatus()),
            currentUser);
            
        return ExampleMapper.toDTO(updatedEntity);
    }

    @Override
    public void deleteExample(Long id) {
        Example entity = exampleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Example not found"));
            
        // AUDIT LOG FOR DELETE (before deletion)
        User currentUser = getCurrentUser();
        auditLogHelper.logDelete("EXAMPLE", id, 
            String.format("{\"name\":\"%s\",\"status\":\"%s\"}", 
                entity.getName(), entity.getStatus()),
            currentUser);
            
        exampleRepository.deleteById(id);
    }

/**
 * BƯỚC 4: THÊM HELPER METHOD ĐỂ LẤY CURRENT USER
 */
    
    /**
     * Helper method to get current user for audit logging
     */
    private User getCurrentUser() {
        try {
            String currentUserEmail = jwtUtils.getCurrentUserLogin().orElse(null);
            if (currentUserEmail != null) {
                return userRepository.findByEmail(currentUserEmail).orElse(null);
            }
            return null;
        } catch (Exception e) {
            // Return null if unable to get current user (e.g., system operations)
            return null;
        }
    }

/**
 * BƯỚC 5: AUDIT LOG CHO CÁC ACTION ĐẶC BIỆT
 */

    @Override
    public void performSpecialAction(Long id, String action) {
        // Business logic...
        
        // AUDIT LOG FOR CUSTOM ACTION
        User currentUser = getCurrentUser();
        auditLogHelper.logCustomAction("SPECIAL_ACTION", "EXAMPLE", id, 
            String.format("{\"action\":\"%s\",\"timestamp\":\"%s\"}", 
                action, LocalDateTime.now()),
            currentUser);
    }

    // SYSTEM ACTIONS (automated operations without user context)
    @Scheduled(fixedRate = 60000)
    public void systemScheduledTask() {
        // Automated business logic...
        
        // AUDIT LOG FOR SYSTEM ACTION (no user)
        auditLogHelper.logSystemAction("SYSTEM_SCHEDULED", "EXAMPLE", null, 
            String.format("{\"task\":\"cleanup\",\"timestamp\":\"%s\"}", 
                LocalDateTime.now()));
    }
}

/**
 * CHÚ THÍCH QUAN TRỌNG:
 * 
 * 1. TARGET_ENTITY: Tên entity chính (VD: "USER", "TRIP", "BOOKING", "PROMOTION")
 * 2. DETAILS: JSON string chứa thông tin quan trọng về action
 * 3. USER: Current user thực hiện action (có thể null cho system actions)
 * 4. ACTION: CREATE, UPDATE, DELETE hoặc custom action name
 * 
 * 5. Luôn bọc audit log trong try-catch để không làm gián đoạn business logic
 * 6. Với system operations (scheduled tasks), sử dụng logSystemAction
 * 7. Với user operations, luôn cố gắng lấy current user
 * 
 * CÁC SERVICE ĐÃ CÓ AUDIT_LOG (KHÔNG CẦN THÊM):
 * - BookingServiceImpl ✅
 * - TicketServiceImpl ✅
 * - UserServiceImpl ✅ (vừa thêm)
 * - TripServiceImpl ✅ (vừa thêm)
 * 
 * CÁC SERVICE CẦN THÊM AUDIT_LOG:
 * - PromotionServiceImpl
 * - BusServiceImpl
 * - BusOperatorServiceImpl
 * - RouteServiceImpl
 * - ReviewServiceImpl
 * - ContractServiceImpl
 * - EmployeeServiceImpl
 * - PaymentServiceImpl
 * - ComplaintServiceImpl
 * - ... và các service khác có CRUD operations
 */
