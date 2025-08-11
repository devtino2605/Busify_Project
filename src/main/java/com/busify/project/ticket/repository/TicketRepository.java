package com.busify.project.ticket.repository;

import com.busify.project.ticket.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Tickets, Long> {
}
