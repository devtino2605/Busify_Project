package com.busify.project.complaint.service;

import java.util.List;
import java.util.stream.Collectors;

import com.busify.project.common.utils.JwtUtils;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.complaint.dto.ComplaintAddCurrentUserDTO;
import com.busify.project.complaint.dto.ComplaintAddDTO;
import com.busify.project.complaint.dto.ComplaintUpdateDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDetailDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseListDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseGetDTO;
import com.busify.project.complaint.entity.Complaint;
import com.busify.project.complaint.mapper.ComplaintDTOMapper;
import com.busify.project.complaint.repository.ComplaintRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.complaint.exception.ComplaintNotFoundException;
import com.busify.project.complaint.exception.ComplaintCreationException;
import com.busify.project.complaint.exception.ComplaintUpdateException;
import com.busify.project.complaint.exception.ComplaintDeleteException;
import com.busify.project.complaint.enums.ComplaintStatus;
import com.busify.project.complaint.dto.response.ComplaintDailyStatsDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ComplaintServiceImpl extends ComplaintService {

        private final AuditLogService auditLogService;

        public ComplaintServiceImpl(ComplaintRepository complaintRepository, UserRepository userRepository,
                        BookingRepository bookingsRepository, JwtUtils jwtUtil, AuditLogService auditLogService) {
                super(complaintRepository, userRepository, bookingsRepository, jwtUtil);
                this.auditLogService = auditLogService;
        }

        public ComplaintResponseDTO addComplaint(ComplaintAddDTO complaintAddDTO) {
                User customer = userRepository.findById(complaintAddDTO.getCustomerId())
                                .orElseThrow(() -> ComplaintCreationException
                                                .customerNotFound(complaintAddDTO.getCustomerId()));
                Bookings booking = bookingsRepository.findById(complaintAddDTO.getBookingId())
                                .orElseThrow(() -> ComplaintCreationException
                                                .bookingNotFound(complaintAddDTO.getBookingId()));
                User assignedAgent = userRepository.findById(complaintAddDTO.getAssignedAgentId())
                                .orElseThrow(() -> ComplaintCreationException
                                                .agentNotFound(complaintAddDTO.getAssignedAgentId()));
                Complaint complaint = ComplaintDTOMapper.toEntity(complaintAddDTO, customer, booking, assignedAgent);
                complaintRepository.save(complaint);

                // Audit log for complaint creation
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("CREATE");
                auditLog.setTargetEntity("COMPLAINT");
                auditLog.setTargetId(complaint.getComplaintsId());
                auditLog.setDetails(String.format(
                                "{\"title\":\"%s\",\"customerId\":%d,\"bookingId\":%d,\"assignedAgentId\":%d}",
                                complaint.getTitle(), customer.getId(), booking.getId(), assignedAgent.getId()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);

                return ComplaintDTOMapper.toResponseAddDTO(complaint);
        }

        public ComplaintResponseDTO addComplaintByCurrentUser(ComplaintAddCurrentUserDTO complaintAddCurrentUserDTO) {

                // 1. Lấy email user hiện tại từ JWT context
                String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";

                User customer = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Customer not found"));

                // 2. Kiểm tra user có booking này không bằng cách sử dụng bookingCode và
                // customerId
                Bookings booking = bookingsRepository
                                .findByBookingCodeAndCustomerId(complaintAddCurrentUserDTO.getBookingCode(),
                                                customer.getId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Booking not found or does not belong to the current user"));

                Complaint complaint = ComplaintDTOMapper.toCurrentUserEntity(complaintAddCurrentUserDTO, customer,
                                booking);
                complaintRepository.save(complaint);

                // Audit log for complaint creation by current user
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("CREATE");
                auditLog.setTargetEntity("COMPLAINT");
                auditLog.setTargetId(complaint.getComplaintsId());
                auditLog.setDetails(String.format("{\"title\":\"%s\",\"bookingCode\":\"%s\",\"customerId\":%d}",
                                complaint.getTitle(), complaintAddCurrentUserDTO.getBookingCode(), customer.getId()));
                auditLog.setUser(customer); // Current user is the customer
                auditLogService.save(auditLog);

                return ComplaintDTOMapper.toResponseAddDTO(complaint);
        }

        public ComplaintResponseListDTO getAllComplaints() {
                List<Complaint> complaints = complaintRepository.findAll();
                List<ComplaintResponseGetDTO> responseList = complaints.stream()
                                .map(ComplaintDTOMapper::toResponseDTO)
                                .collect(Collectors.toList());
                return new ComplaintResponseListDTO(responseList);
        }

        public ComplaintResponseDetailDTO getComplaintById(Long id) {
                Complaint complaint = complaintRepository.findById(id)
                                .orElseThrow(() -> ComplaintNotFoundException.withId(id));
                return ComplaintDTOMapper.toDetailResponseDTO(complaint);
        }

        public ComplaintResponseListDTO getAllComplaintsByBooking(Long bookingId) {
                List<Complaint> complaints = complaintRepository.findAllByBookingId(bookingId);
                List<ComplaintResponseGetDTO> responseList = complaints.stream()
                                .map(ComplaintDTOMapper::toResponseDTO)
                                .collect(Collectors.toList());
                return new ComplaintResponseListDTO(responseList);
        }

        public ComplaintResponseListDTO getAllByBusOperatorId(Long busOperatorId) {
                List<Complaint> complaints = complaintRepository.findByBusOperatorComplaint(busOperatorId);
                List<ComplaintResponseGetDTO> responseList = complaints.stream()
                                .map(ComplaintDTOMapper::toResponseDTO)
                                .collect(Collectors.toList());
                return new ComplaintResponseListDTO(responseList);

        }

        public ComplaintResponseListDTO getAllComplaintsByTrip(Long tripId) {
                List<Complaint> complaints = complaintRepository.findByTripId(tripId);
                List<ComplaintResponseGetDTO> responseList = complaints.stream()
                                .map(ComplaintDTOMapper::toResponseDTO)
                                .collect(Collectors.toList());
                return new ComplaintResponseListDTO(responseList);
        }

        public ComplaintResponseListDTO getAllComplaintsByCustomer(Long customerId) {
                List<Complaint> complaints = complaintRepository.findAllByCustomerId(customerId);
                List<ComplaintResponseGetDTO> responseList = complaints.stream()
                                .map(ComplaintDTOMapper::toResponseDTO)
                                .collect(Collectors.toList());
                return new ComplaintResponseListDTO(responseList);
        }

        public ComplaintResponseListDTO getAllComplaintsByCurrentCustomer() {

                // 1. Lấy email user hiện tại từ JWT context
                String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";

                // 2. Lấy user từ DB dựa trên email
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                List<Complaint> complaints = complaintRepository.findAllByCustomerId(user.getId());
                List<ComplaintResponseGetDTO> responseList = complaints.stream()
                                .map(ComplaintDTOMapper::toResponseDTO)
                                .collect(Collectors.toList());
                return new ComplaintResponseListDTO(responseList);
        }

        public List<ComplaintResponseDetailDTO> getAllComplaintsByAgent(Long agentId) {
                List<Complaint> complaints = complaintRepository.findAllByAssignedAgent_Id(agentId);
                return complaints.stream()
                                .map(ComplaintDTOMapper::toDetailResponseDTO)
                                .collect(Collectors.toList());
        }

        public ComplaintResponseDetailDTO updateComplaint(Long id, ComplaintUpdateDTO complaintUpdateDTO) {
                Complaint complaint = complaintRepository.findById(id)
                                .orElseThrow(() -> ComplaintUpdateException.complaintNotFound(id));

                // Update title if provided
                complaint.setTitle(complaintUpdateDTO.getTitle() != null ? complaintUpdateDTO.getTitle()
                                : complaint.getTitle());

                // Update description if provided
                complaint.setDescription(
                                complaintUpdateDTO.getDescription() != null ? complaintUpdateDTO.getDescription()
                                                : complaint.getDescription());

                // Update status if provided
                complaint.setStatus(complaintUpdateDTO.getStatus() != null ? complaintUpdateDTO.getStatus()
                                : complaint.getStatus());

                // Update assigned agent if provided
                if (complaintUpdateDTO.getAssignedAgentId() != null) {
                        User assignedAgent = userRepository.findById(complaintUpdateDTO.getAssignedAgentId())
                                        .orElseThrow(() -> ComplaintUpdateException
                                                        .agentNotFound(complaintUpdateDTO.getAssignedAgentId()));
                        complaint.setAssignedAgent(assignedAgent);
                }

                complaintRepository.save(complaint);

                // Audit log for complaint update
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("UPDATE");
                auditLog.setTargetEntity("COMPLAINT");
                auditLog.setTargetId(complaint.getComplaintsId());
                auditLog.setDetails(String.format("{\"title\":\"%s\",\"status\":\"%s\",\"assignedAgentId\":%d}",
                                complaint.getTitle(), complaint.getStatus(),
                                complaint.getAssignedAgent() != null ? complaint.getAssignedAgent().getId() : null));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);

                return ComplaintDTOMapper.toDetailResponseDTO(complaint);
        }

        public void deleteComplaint(Long id) {
                Complaint complaint = complaintRepository.findById(id)
                                .orElseThrow(() -> ComplaintDeleteException.complaintNotFound(id));

                // Audit log for complaint deletion (before deletion)
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("DELETE");
                auditLog.setTargetEntity("COMPLAINT");
                auditLog.setTargetId(complaint.getComplaintsId());
                auditLog.setDetails(String.format("{\"title\":\"%s\",\"status\":\"%s\",\"action\":\"hard_delete\"}",
                                complaint.getTitle(), complaint.getStatus()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);

                complaintRepository.delete(complaint);
        }

        public List<ComplaintResponseDetailDTO> findAllByAssignedAgent() {
                // 1. Lấy email user hiện tại từ JWT context
                String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";

                // 2. Lấy user từ DB dựa trên email
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                List<Complaint> complaints = complaintRepository.findAllByAssignedAgent(user);
                return complaints.stream()
                                .map(ComplaintDTOMapper::toDetailResponseDTO)
                                .collect(Collectors.toList());
        }

        public List<ComplaintResponseDetailDTO> findInProgressByAssignedAgent() {
                String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                List<Complaint> complaints = complaintRepository.findAllByAssignedAgentAndStatus(
                                user, com.busify.project.complaint.enums.ComplaintStatus.in_progress);
                return complaints.stream()
                                .map(ComplaintDTOMapper::toDetailResponseDTO)
                                .collect(Collectors.toList());
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

        public ComplaintResponseDetailDTO updateComplaintStatus(Long id, ComplaintStatus status) {
                Complaint complaint = complaintRepository.findById(id)
                                .orElseThrow(() -> ComplaintUpdateException.complaintNotFound(id));
                complaint.setStatus(status);
                complaintRepository.save(complaint);
                return ComplaintDTOMapper.toDetailResponseDTO(complaint);
        }

        public ComplaintDailyStatsDTO getDailyComplaintStatsForCurrentAgent() {
                User currentAgent = getCurrentUser(); // Lấy agent hiện tại

                LocalDate today = LocalDate.now();
                LocalDateTime startOfDay = today.atStartOfDay();
                LocalDateTime endOfDay = today.atTime(23, 59, 59);

                long newCount = complaintRepository.countByAssignedAgent_IdAndStatusAndCreatedAtBetween(
                                currentAgent.getId(), ComplaintStatus.New, startOfDay, endOfDay);
                long pendingCount = complaintRepository.countByAssignedAgent_IdAndStatusAndCreatedAtBetween(
                                currentAgent.getId(), ComplaintStatus.pending, startOfDay, endOfDay);
                long inProgressCount = complaintRepository.countByAssignedAgent_IdAndStatusAndCreatedAtBetween(
                                currentAgent.getId(), ComplaintStatus.in_progress, startOfDay, endOfDay);
                long resolvedCount = complaintRepository.countByAssignedAgent_IdAndStatusAndCreatedAtBetween(
                                currentAgent.getId(), ComplaintStatus.resolved, startOfDay, endOfDay);
                long rejectedCount = complaintRepository.countByAssignedAgent_IdAndStatusAndCreatedAtBetween(
                                currentAgent.getId(), ComplaintStatus.rejected, startOfDay, endOfDay);

                return new ComplaintDailyStatsDTO(newCount, pendingCount, inProgressCount, resolvedCount,
                                rejectedCount);
        }

}
