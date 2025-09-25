package com.busify.project.seat_layout.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatLayoutFilterTripResponse implements Serializable {
    private Integer id;
    private String name;
}
