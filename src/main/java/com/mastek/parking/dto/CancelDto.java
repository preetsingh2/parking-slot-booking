package com.mastek.parking.dto;

import lombok.Data;

@Data
public class CancelDto {
    private String bookingId;
    private String reason;
}
