package com.busify.project.complaint.service;

import java.util.List;
import java.util.stream.Collectors;

import com.busify.project.common.utils.JwtUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.repository.BookingRepository;
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

@Service
public class ComplaintServiceImpl extends ComplaintService {

        public ComplaintServiceImpl(ComplaintRepository complaintRepository, UserRepository userRepository,
                        BookingRepository bookingsRepository, JwtUtils jwtUtil) {
                super(complaintRepository, userRepository, bookingsRepository, jwtUtil);
        }

        public ComplaintResponseDTO addComplaint(ComplaintAddDTO complaintAddDTO) {
                User customer = userRepository.findById(complaintAddDTO.getCustomerId())
                                .orElseThrow(() -> new RuntimeException("Customer not found"));
                Bookings booking = bookingsRepository.findById(complaintAddDTO.getBookingId())
                                .orElseThrow(() -> new RuntimeException("Booking not found"));
                User assignedAgent = userRepository.findById(complaintAddDTO.getAssignedAgentId())
                                .orElseThrow(() -> new RuntimeException("Assigned agent not found"));
                Complaint complaint = ComplaintDTOMapper.toEntity(complaintAddDTO, customer, booking, assignedAgent);
                complaintRepository.save(complaint);
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
                                .orElseThrow(() -> new RuntimeException("Complaint not found"));
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

        public List<ComplaintResponseDetailDTO> getAllComplaintsByAgent(Long agentId) {
                List<Complaint> complaints = complaintRepository.findAllByAssignedAgent_Id(agentId);
                return complaints.stream()
                                .map(ComplaintDTOMapper::toDetailResponseDTO)
                                .collect(Collectors.toList());
        }

        public ComplaintResponseDetailDTO updateComplaint(Long id, ComplaintUpdateDTO complaintUpdateDTO) {
                Complaint complaint = complaintRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Complaint not found"));

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
                                        .orElseThrow(() -> new RuntimeException("Assigned agent not found"));
                        complaint.setAssignedAgent(assignedAgent);
                }

                complaintRepository.save(complaint);
                return ComplaintDTOMapper.toDetailResponseDTO(complaint);
        }

        public void deleteComplaint(Long id) {
                Complaint complaint = complaintRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Complaint not found"));
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
                user, com.busify.project.complaint.enums.ComplaintStatus.in_progress
            );
            return complaints.stream()
                    .map(ComplaintDTOMapper::toDetailResponseDTO)
                    .collect(Collectors.toList());
        }


}
