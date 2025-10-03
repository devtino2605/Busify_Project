package com.busify.project.contract.service.impl;

import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.bus_operator.enums.OperatorStatus;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.auth.service.EmailService;
import com.busify.project.contract.entity.Contract;
import com.busify.project.contract.exception.ContractUserCreationException;
import com.busify.project.contract.service.ContractUserService;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.enums.UserStatus;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ContractUserServiceImpl implements ContractUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BusOperatorRepository busOperatorRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    @Override
    public Profile processAcceptedContract(Contract contract) {
        log.info("Processing accepted contract for email: {}", contract.getEmail());

        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(contract.getEmail());

        Profile profile;
        boolean isNewUser = false;

        if (existingUser.isPresent() && existingUser.get() instanceof Profile) {
            // User exists, update role if needed
            profile = (Profile) existingUser.get();
            profile = updateExistingUserRole(profile, contract);
            log.info("Updated existing user role for email: {}", contract.getEmail());
        } else {
            // Create new user
            profile = createNewUserFromContract(contract);
            isNewUser = true;
            log.info("Created new user for email: {}", contract.getEmail());
        }

        // Create or update bus operator
        BusOperator busOperator = createOrUpdateBusOperator(contract, profile);
        log.info("Created/updated bus operator with ID: {}", busOperator.getId());

        // Send appropriate email
        if (isNewUser) {
            sendWelcomeEmailForNewUser(profile, contract);
        } else {
            sendContractAcceptedEmail(profile, contract);
        }

        // Audit log for contract processing
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("PROCESS");
            auditLog.setTargetEntity("CONTRACT_USER");
            auditLog.setTargetId(contract.getId());
            auditLog.setDetails(String.format("{\"contract_email\":\"%s\",\"profile_id\":%d,\"bus_operator_id\":%d,\"is_new_user\":%b,\"action\":\"process_accepted_contract\"}", 
                    contract.getEmail(), profile.getId(), busOperator.getId(), isNewUser));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to create audit log for contract processing: {}", e.getMessage());
        }

        return profile;
    }

    private Profile updateExistingUserRole(Profile profile, Contract contract) {
        Role busOperatorRole = getBusOperatorRole();

        // Only update role if not already BUS_OPERATOR
        if (!busOperatorRole.getName().equals(profile.getRole().getName())) {
            profile.setRole(busOperatorRole);
            log.info("Updated role to BUS_OPERATOR for user: {}", profile.getEmail());
        }

        // Update profile information from contract if needed
        updateProfileFromContract(profile, contract);

        return userRepository.save(profile);
    }

    private Profile createNewUserFromContract(Contract contract) {
        Role busOperatorRole = getBusOperatorRole();
        String temporaryPassword = generateTemporaryPassword();

        Profile profile = new Profile();
        profile.setEmail(contract.getEmail());
        profile.setPasswordHash(passwordEncoder.encode(temporaryPassword));
        profile.setRole(busOperatorRole);
        profile.setEmailVerified(true);
        profile.setStatus(UserStatus.active);

        // Set profile information from contract
        updateProfileFromContract(profile, contract);

        // Store temporary password for email (in real app, you might want to use a more
        // secure approach)
        profile.setRefreshToken("TEMP_PASSWORD:" + temporaryPassword); // Temporary storage

        return userRepository.save(profile);
    }

    private void updateProfileFromContract(Profile profile, Contract contract) {
        // Extract company name from VAT code or use email prefix as fallback
        String companyName = extractCompanyNameFromContract(contract);

        if (profile.getFullName() == null || profile.getFullName().isEmpty()) {
            profile.setFullName(companyName);
        }

        if (profile.getPhoneNumber() == null || profile.getPhoneNumber().isEmpty()) {
            profile.setPhoneNumber(contract.getPhone());
        }

        if (profile.getAddress() == null || profile.getAddress().isEmpty()) {
            profile.setAddress(contract.getAddress());
        }
    }

    private String extractCompanyNameFromContract(Contract contract) {
        // You can implement logic to extract company name from VAT code or other fields
        // For now, use email prefix as fallback
        String emailPrefix = contract.getEmail().split("@")[0];
        return emailPrefix.replace(".", " ").replace("_", " ").toUpperCase() + " COMPANY";
    }

    private BusOperator createOrUpdateBusOperator(Contract contract, Profile profile) {
        // Check if bus operator already exists for this email
        Optional<BusOperator> existingOperator = busOperatorRepository
                .findByEmailAndIsDeletedFalse(contract.getEmail());

        BusOperator busOperator;
        if (existingOperator.isPresent()) {
            busOperator = existingOperator.get();
            log.info("Updating existing bus operator for email: {}", contract.getEmail());
        } else {
            busOperator = new BusOperator();
            log.info("Creating new bus operator for email: {}", contract.getEmail());
        }

        // Update bus operator information from contract
        busOperator.setName(extractCompanyNameFromContract(contract));
        busOperator.setOwner(profile);
        busOperator.setAddress(contract.getAddress());
        busOperator.setHotline(contract.getPhone());
        busOperator.setEmail(contract.getEmail());
        busOperator.setDescription("Bus operator created from contract: " + contract.getVATCode());
        busOperator.setLicensePath(contract.getLicenseUrl());
        busOperator.setStatus(OperatorStatus.active);
        busOperator.setDeleted(false);

        return busOperatorRepository.save(busOperator);
    }

    private Role getBusOperatorRole() {
        return roleRepository.findByName("OPERATOR")
                .orElseThrow(() -> ContractUserCreationException.roleAssignmentFailed(
                        null, "OPERATOR",
                        new RuntimeException("OPERATOR role not found. Please create this role in the database.")));
    }

    private String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    private void sendWelcomeEmailForNewUser(Profile profile, Contract contract) {
        try {
            String temporaryPassword = null;
            if (profile.getRefreshToken() != null && profile.getRefreshToken().startsWith("TEMP_PASSWORD:")) {
                temporaryPassword = profile.getRefreshToken().substring("TEMP_PASSWORD:".length());
                // Clear the temporary password from refresh token
                profile.setRefreshToken(null);
                userRepository.save(profile);
            }

            String subject = "Tài khoản Bus Operator đã được tạo - Busify";
            String emailBody = buildWelcomeEmailBody(profile, contract, temporaryPassword);

            emailService.sendSimpleEmail(profile.getEmail(), subject, emailBody);
            log.info("Welcome email sent to new user: {}", profile.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", profile.getEmail(), e);
        }
    }

    private void sendContractAcceptedEmail(Profile profile, Contract contract) {
        try {
            String subject = "Hợp đồng đã được chấp nhận - Busify";
            String emailBody = buildContractAcceptedEmailBody(profile, contract);

            emailService.sendSimpleEmail(profile.getEmail(), subject, emailBody);
            log.info("Contract accepted email sent to: {}", profile.getEmail());
        } catch (Exception e) {
            log.error("Failed to send contract accepted email to: {}", profile.getEmail(), e);
        }
    }

    private String buildWelcomeEmailBody(Profile profile, Contract contract, String temporaryPassword) {
        return String.format("""
                Xin chào %s,

                Chúc mừng! Hợp đồng của bạn đã được chấp nhận và tài khoản Bus Operator đã được tạo thành công.

                Thông tin đăng nhập:
                - Email: %s
                - Mật khẩu tạm thời: %s

                QUAN TRỌNG: Vui lòng đăng nhập và đổi mật khẩu ngay sau khi nhận được email này.

                Thông tin hợp đồng:
                - Mã số thuế: %s
                - Khu vực hoạt động: %s
                - Thời gian hiệu lực: %s đến %s

                Bạn có thể đăng nhập vào hệ thống tại: [URL_WEBSITE]

                Trân trọng,
                Đội ngũ Busify
                """,
                profile.getFullName(),
                profile.getEmail(),
                temporaryPassword,
                contract.getVATCode(),
                contract.getOperationArea(),
                contract.getStartDate(),
                contract.getEndDate());
    }

    private String buildContractAcceptedEmailBody(Profile profile, Contract contract) {
        return String.format("""
                Xin chào %s,

                Chúc mừng! Hợp đồng của bạn đã được chấp nhận.

                Tài khoản của bạn đã được cập nhật với quyền Bus Operator.

                Thông tin hợp đồng:
                - Mã số thuế: %s
                - Khu vực hoạt động: %s
                - Thời gian hiệu lực: %s đến %s

                Bạn có thể đăng nhập vào hệ thống để quản lý các tuyến xe bus của mình.

                Trân trọng,
                Đội ngũ Busify
                """,
                profile.getFullName(),
                contract.getVATCode(),
                contract.getOperationArea(),
                contract.getStartDate(),
                contract.getEndDate());
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
