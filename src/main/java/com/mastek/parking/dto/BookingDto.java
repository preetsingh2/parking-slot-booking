package com.mastek.parking.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.UUID;

@Data
public class BookingDto {

  //  @Id
  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String username;
    private String userEmail;
    private String bookingId;
    private Long parkingSlotNumber;

   // @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bookingDate;   //date

  //  @DateTimeFormat(pattern = "HH:mm:ss")
    private Date bookingStartDateTime;   //time

   // @DateTimeFormat(pattern = "HH:mm:ss")
    private Date bookingEndDateTime;    //time

    private Date created_at;  //timestamp
    private Date updated_at;  //timestamp
    private String comment;
    private String bookingStatus;

    @ManyToOne
    private ParkingDto ParkingDto;

    /*public static String generateId() {
        return "Booking_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }*/

}
