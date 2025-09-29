package com.busify.project.ticket.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBySeat {
  private Long  ticketId;
  private String passengerName;
  private String passengerPhone;
  private BigDecimal price;
  private String seatNumber;
  private String status;
  private String ticketCode;
  private Long bookingId;
  private String sellerName;
}
