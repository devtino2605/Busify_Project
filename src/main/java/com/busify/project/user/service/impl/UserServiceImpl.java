package com.busify.project.user.service.impl;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.dto.request.UserManagementFilterDTO;
import com.busify.project.user.dto.request.UserManagerUpdateOrCreateDTO;
import com.busify.project.user.dto.response.UserManagementDTO;
import com.busify.project.user.dto.response.UserManagementPageDTO;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.enums.UserStatus;
import com.busify.project.user.mapper.UserMapper;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.user.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    private final JwtUtils jwtUtil;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

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

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        if (!(user instanceof Profile)) {
            throw new RuntimeException("User is not a Profile with id: " + userId);
        }
        return UserMapper.toDTO((Profile) user);
    }

    @Override
    public UserDTO updateUserProfile(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (!(user instanceof Profile)) {
            throw new RuntimeException("User is not a Profile with id: " + id);
        }
        Profile profile = (Profile) user;
        profile.setFullName(userDTO.getFullName());
        profile.setEmail(userDTO.getEmail());
        profile.setPhoneNumber(userDTO.getPhoneNumber());
        profile.setAddress(userDTO.getAddress());
        userRepository.save(user);
        return UserMapper.toDTO(profile);
    }

    @Override
    public UserDTO findUserByEmail() {
        String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        if (!(user instanceof Profile)) {
            throw new RuntimeException("User is not a Profile with email: " + email);
        }
        return UserMapper.toDTO((Profile) user);
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
        return UserMapper.toDTO(profile);
    }

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

    @Override
    public UserDTO createUser(UserManagerUpdateOrCreateDTO userDTO) {
        User existingUser = userRepository.findByEmail(userDTO.getEmail())
                .orElse(null);
        if (existingUser != null) {
            throw new RuntimeException("Người dùng đã tồn tại với email: " + userDTO.getEmail());
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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò với id: " + userDTO.getRoleId()));
        newUser.setRole(role);

        // Set default status to active
        newUser.setStatus(UserStatus.active);

        userRepository.save(newUser);
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
}