package com.busify.project.complaint.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.repository.BookingsRepository;
import com.busify.project.complaint.dto.ComplaintAddDTO;
import com.busify.project.complaint.dto.ComplaintUpdateDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseListDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseGetDTO;
import com.busify.project.complaint.entity.Complaint;
import com.busify.project.complaint.enums.ComplaintStatus;
import com.busify.project.complaint.mapper.ComplaintDTOMapper;
import com.busify.project.complaint.repository.ComplaintRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

@Service
public class ComplaintServiceImpl extends ComplaintService {

        public ComplaintServiceImpl(ComplaintRepository complaintRepository, UserRepository userRepository,
                        BookingsRepository bookingsRepository) {
                super(complaintRepository, userRepository, bookingsRepository);
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

        public ComplaintResponseDTO getComplaintById(Long id) {
                Complaint complaint = complaintRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Complaint not found"));
                return ComplaintDTOMapper.toResponseDTO(complaint);
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

        public ComplaintResponseDTO updateComplaint(Long id, ComplaintUpdateDTO complaintUpdateDTO) {
                Complaint complaint = complaintRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Complaint not found"));
                complaint
                                .setTitle(complaintUpdateDTO.getTitle() != null ? complaintUpdateDTO.getTitle()
                                                : complaint.getTitle());
                complaint.setDescription(
                                complaintUpdateDTO.getDescription() != null ? complaintUpdateDTO.getDescription()
                                                : complaint.getDescription());
                complaint.setStatus(ComplaintStatus.New);
                complaintRepository.save(complaint);
                return ComplaintDTOMapper.toResponseDTO(complaint);
        }

        public void deleteComplaint(Long id) {
                Complaint complaint = complaintRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Complaint not found"));
                complaintRepository.delete(complaint);
        }
}
