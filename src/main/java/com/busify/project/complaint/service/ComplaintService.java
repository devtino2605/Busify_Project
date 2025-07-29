package com.busify.project.complaint.service;

import org.springframework.stereotype.Service;

import com.busify.project.booking.repository.BookingsRepository;
import com.busify.project.complaint.repository.ComplaintRepository;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public abstract class ComplaintService {

    protected final ComplaintRepository complaintRepository;
    protected final UserRepository userRepository;
    protected final BookingsRepository bookingsRepository;
}
