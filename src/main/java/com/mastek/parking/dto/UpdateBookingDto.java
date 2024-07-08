package com.mastek.parking.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateBookingDto {

    String bookingId;
    Date newFromTime;
    Date newToTime;
    String newComment;
    Date ActualLeaveTime;
}
