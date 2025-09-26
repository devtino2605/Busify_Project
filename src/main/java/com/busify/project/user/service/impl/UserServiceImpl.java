package com.busify.project.user.service.impl;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.dto.request.ChangePasswordRequestDTO;
import com.busify.project.user.dto.request.UserManagementFilterDTO;
import com.busify.project.user.dto.request.UserManagerUpdateOrCreateDTO;
import com.busify.project.user.dto.response.UserManagementDTO;
import com.busify.project.user.dto.response.UserManagementPageDTO;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.enums.UserStatus;
import com.busify.project.user.exception.UserEmailAlreadyExistsException;
import com.busify.project.user.exception.UserNotProfileException;
import com.busify.project.user.exception.UserProfileNotFoundException;
import com.busify.project.user.exception.UserRoleNotFoundException;
import com.busify.project.user.mapper.UserMapper;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.user.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    private final JwtUtils utils;
    @PersistenceContext
    private EntityManager entityManager;

    @Cacheable(value = "allUsers")
    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAllWithRoles();
        return users
                .stream()
                .filter(user -> user instanceof Profile)
                .map(user -> (Profile) user)
                .filter(profile -> profile.getStatus() == UserStatus.active) // Only active users
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "userById", key = "#userId")
    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserProfileNotFoundException.notFound());
        if (!(user instanceof Profile)) {
            throw UserNotProfileException.notProfile();
        }
        return UserMapper.toDTO((Profile) user);
    }

    @Caching(evict = {
            @CacheEvict(value = "userById", key = "#id"),
            @CacheEvict(value = "userProfile", key = "#id"),
            @CacheEvict(value = "allUsers", allEntries = true)
    })
    @Override
    public UserDTO updateUserProfile(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> UserProfileNotFoundException.notFound());
        if (!(user instanceof Profile)) {
            throw UserNotProfileException.notProfile();
        }
        Profile profile = (Profile) user;
        profile.setFullName(userDTO.getFullName());
        profile.setEmail(userDTO.getEmail());
        profile.setPhoneNumber(userDTO.getPhoneNumber());
        profile.setAddress(userDTO.getAddress());
        userRepository.save(user);

        // Audit log for user profile update
        User currentUser = getCurrentUser();
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("UPDATE");
        auditLog.setTargetEntity("USER_PROFILE");
        auditLog.setTargetId(id);
        auditLog.setDetails(
                String.format("{\"email\":\"%s\",\"fullName\":\"%s\"}", profile.getEmail(), profile.getFullName()));
        auditLog.setUser(currentUser);
        auditLogService.save(auditLog);

        return UserMapper.toDTO(profile);
    }

    @Override
    public UserDTO getUserProfile() {
        String email = utils.getCurrentUserLogin().isPresent() ? utils.getCurrentUserLogin().get() : "";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        if (!(user instanceof Profile)) {
            throw new RuntimeException("User is not a Profile with email: " + email);
        }
        return cacheUserProfile(user.getId(), (Profile) user);
    }

    @Cacheable(value = "userProfile", key = "#id")
    public UserDTO cacheUserProfile(Long id, Profile profile) {
        return UserMapper.toDTO(profile);
    }

    @CacheEvict(value = { "userById", "userProfile", "allUsers" }, allEntries = true)
    @Override
    public void changePassword(ChangePasswordRequestDTO request) {
        // Lấy email user đang đăng nhập
        String currentEmail = utils.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User not logged in"));

        // Tìm user
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + currentEmail));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Kiểm tra newPassword == confirmPassword
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        // Cập nhật mật khẩu
        currentUser.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);

        // Audit log for password change
        User auditUser = getCurrentUser();
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("CHANGE_PASSWORD");
        auditLog.setTargetEntity("USER");
        auditLog.setTargetId(currentUser.getId());
        auditLog.setDetails("{\"email\":\"" + currentUser.getEmail() + "\"}");
        auditLog.setUser(auditUser);
        auditLogService.save(auditLog);
    }

    @Override
    public UserDTO updateCurrentUserProfile(UserDTO userDTO) {
        // Lấy email user đang đăng nhập
        String currentEmail = utils.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User not logged in"));

        // Tìm user
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + currentEmail));

        if (!(user instanceof Profile)) {
            throw new RuntimeException("User is not a Profile with email: " + currentEmail);
        }

        Profile profile = (Profile) user;

        // Cập nhật thông tin
        profile.setFullName(userDTO.getFullName());
        profile.setEmail(userDTO.getEmail());
        profile.setPhoneNumber(userDTO.getPhoneNumber());
        profile.setAddress(userDTO.getAddress());

        userRepository.save(user);

        // Audit log for user profile update
        User currentUser = getCurrentUser();
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("UPDATE");
        auditLog.setTargetEntity("USER_PROFILE");
        auditLog.setTargetId(profile.getId());
        auditLog.setDetails(
                String.format("{\"email\":\"%s\",\"fullName\":\"%s\"}", profile.getEmail(), profile.getFullName()));
        auditLog.setUser(currentUser);
        auditLogService.save(auditLog);

        return UserMapper.toDTO(profile);
    }

    @Override
    public User getUserCurrentlyLoggedIn() {
        String email = utils.getCurrentUserLogin().isPresent() ? utils.getCurrentUserLogin().get() : "";
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public UserDTO updateUserById(Long id, UserManagerUpdateOrCreateDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (!(user instanceof Profile)) {
            throw new RuntimeException("User is not a Profile with id: " + id);
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + userDTO.getRoleId()));

        Profile profile = (Profile) user;

        profile.setFullName(userDTO.getFullName());
        profile.setEmail(userDTO.getEmail());
        profile.setPhoneNumber(userDTO.getPhoneNumber());
        profile.setAddress(userDTO.getAddress());
        profile.setEmailVerified(userDTO.isEmailVerified());
        profile.setRole(role);

        userRepository.save(profile);

        // Audit log for user update by admin
        User currentUser = getCurrentUser();
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("UPDATE");
        auditLog.setTargetEntity("USER");
        auditLog.setTargetId(id);
        auditLog.setDetails(String.format("{\"email\":\"%s\",\"fullName\":\"%s\",\"roleId\":%d}",
                profile.getEmail(), profile.getFullName(), userDTO.getRoleId()));
        auditLog.setUser(currentUser);
        auditLogService.save(auditLog);

        return UserMapper.toDTO(profile);
    }

    @Caching(evict = {
            @CacheEvict(value = "userById", key = "#id"),
            @CacheEvict(value = "userProfile", key = "#id"),
            @CacheEvict(value = "allUsers", allEntries = true)
    })
    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (!(user instanceof Profile)) {
            throw new RuntimeException("User is not a Profile with id: " + id);
        }

        Profile profile = (Profile) user;

        // Soft delete: set status to inactive instead of deleting from database
        profile.setStatus(UserStatus.inactive);
        userRepository.save(profile);

        // Audit log for user soft delete
        User currentUser = getCurrentUser();
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("DELETE");
        auditLog.setTargetEntity("USER");
        auditLog.setTargetId(id);
        auditLog.setDetails(String.format("{\"email\":\"%s\",\"action\":\"soft_delete\"}", profile.getEmail()));
        auditLog.setUser(currentUser);
        auditLogService.save(auditLog);
    }

    @Override
    public List<UserManagementDTO> getAllUsersForManagement() {
        List<User> users = userRepository.findAllWithRoles();
        return users
                .stream()
                .filter(user -> user instanceof Profile)
                .map(user -> (Profile) user)
                .map(UserMapper::toManagementDTO)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "allUsers", allEntries = true)
    @Override
    public UserDTO createUser(UserManagerUpdateOrCreateDTO userDTO) {
        User existingUser = userRepository.findByEmail(userDTO.getEmail())
                .orElse(null);
        if (existingUser != null) {
            throw UserEmailAlreadyExistsException.alreadyExists();
        }
        Profile newUser = new Profile();
        newUser.setFullName(userDTO.getFullName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setAddress(userDTO.getAddress());
        newUser.setEmailVerified(userDTO.isEmailVerified());

        newUser.setPasswordHash(passwordEncoder.encode("123456@Aabc"));
        newUser.setAuthProvider(AuthProvider.LOCAL);

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> UserRoleNotFoundException.notFound());
        newUser.setRole(role);

        // Set default status to active
        newUser.setStatus(UserStatus.active);

        userRepository.save(newUser);

        // Audit log for user creation
        User currentUser = getCurrentUser();
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("CREATE");
        auditLog.setTargetEntity("USER");
        auditLog.setTargetId(newUser.getId());
        auditLog.setDetails(String.format("{\"email\":\"%s\",\"fullName\":\"%s\",\"roleId\":%d}",
                newUser.getEmail(), newUser.getFullName(), userDTO.getRoleId()));
        auditLog.setUser(currentUser);
        auditLogService.save(auditLog);

        return UserMapper.toDTO(newUser);
    }

    @Override
    public UserManagementPageDTO getUsersForManagementWithFilter(UserManagementFilterDTO filterDTO) {
        // Create Pageable with sorting
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(filterDTO.getSortDirection())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                filterDTO.getSortBy());
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);

        // Parse date filters
        Instant createdFrom = null;
        Instant createdTo = null;

        if (filterDTO.getCreatedFromDate() != null && !filterDTO.getCreatedFromDate().isEmpty()) {
            createdFrom = LocalDate.parse(filterDTO.getCreatedFromDate()).atStartOfDay().toInstant(ZoneOffset.UTC);
        }

        if (filterDTO.getCreatedToDate() != null && !filterDTO.getCreatedToDate().isEmpty()) {
            createdTo = LocalDate.parse(filterDTO.getCreatedToDate()).atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        }

        // Apply search filter - null if empty or blank
        String searchFilter = (filterDTO.getSearch() != null && !filterDTO.getSearch().trim().isEmpty())
                ? filterDTO.getSearch().trim()
                : null;

        // Query with filters
        Page<Profile> usersPage = userRepository.findUsersForManagement(
                searchFilter,
                filterDTO.getStatus(),
                filterDTO.getAuthProvider(),
                filterDTO.getRoleName(),
                filterDTO.getEmailVerified(),
                createdFrom,
                createdTo,
                pageable);

        // Convert to DTOs
        List<UserManagementDTO> userDTOs = usersPage.getContent()
                .stream()
                .map(UserMapper::toManagementDTO)
                .collect(Collectors.toList());

        // Build filter summary
        UserManagementPageDTO.FilterSummaryDTO filterSummary = UserManagementPageDTO.FilterSummaryDTO.builder()
                .totalActive(userRepository.countByStatus(UserStatus.active))
                .totalInactive(userRepository.countByStatus(UserStatus.inactive))
                .totalSuspended(userRepository.countByStatus(UserStatus.suspended))
                .totalEmailVerified(userRepository.countByEmailVerified())
                .totalGoogleAuth(userRepository.countByAuthProvider(AuthProvider.GOOGLE))
                .totalLocalAuth(userRepository.countByAuthProvider(AuthProvider.LOCAL))
                .build();

        // Build response
        return UserManagementPageDTO.builder()
                .users(userDTOs)
                .totalElements(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .currentPage(usersPage.getNumber())
                .pageSize(usersPage.getSize())
                .hasNext(usersPage.hasNext())
                .hasPrevious(usersPage.hasPrevious())
                .isFirst(usersPage.isFirst())
                .isLast(usersPage.isLast())
                .filterSummary(filterSummary)
                .build();
    }

    @Override
    public UserDTO findUserByEmail() {
        throw new UnsupportedOperationException("Unimplemented method 'findUserByEmail'");
    }

    /**
     * Helper method to get current user for audit logging
     */
    private User getCurrentUser() {
        try {
            String currentUserEmail = utils.getCurrentUserLogin().orElse(null);
            if (currentUserEmail != null) {
                return userRepository.findByEmail(currentUserEmail).orElse(null);
            }
            return null;
        } catch (Exception e) {
            // Return null if unable to get current user (e.g., system operations)
            return null;
        }
    }
}