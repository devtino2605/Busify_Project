package com.busify.project.ticket.repository;

import com.busify.project.ticket.entity.Tickets;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Tickets, Long> {
    Optional<Tickets> findByTicketCode(String ticketCode);
    List<Tickets> findByPassengerName(String name);
    List<Tickets> findByPassengerPhone(String phone);
}
