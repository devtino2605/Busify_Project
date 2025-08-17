package com.busify.project.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateRequestDTO {
    private String guestFullName;
    private String guestPhone;
    private String guestEmail;
    private String guestAddress;
}
