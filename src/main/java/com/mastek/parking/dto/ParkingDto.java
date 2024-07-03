package com.mastek.parking.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Date;


@Data
public class ParkingDto {

    //private Long id;
    private Long slotNumber;
    private String location;
    private Boolean is_occupied;
   // private Long availableSpots;
    private String created_by;
}
