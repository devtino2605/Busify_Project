package com.busify.project.location.dto.response;

import com.busify.project.location.enums.LocationRegion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private Long id;
    private String name;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private LocationRegion region;
    
    // Constructor rút gọn cho dropdown
    public LocationDTO(Long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }
}