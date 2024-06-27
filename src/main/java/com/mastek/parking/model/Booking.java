package com.mastek.parking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mastek.parking.exception.TimeDeserializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "\"booking_details\"") // Specify the exact table name with quotes
@Data
public class Booking {

    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", strategy = "com.mastek.parking.common.CustomIdGenerator")
    @Column(name = "booking_Id")
    private String bookingId;

    @Column(name = "user_name")
    private String username;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "parking_slot_number")
    private Long parkingSlotNumber;

    @NotNull(message = "date is mandatory")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "booking_date")
    private Date bookingDate;

    @Temporal(TemporalType.TIMESTAMP)
   // @JsonFormat(pattern = "HH:mm:ss")
  //  @DateTimeFormat(pattern = "HH:mm:ss")
    //@JsonDeserialize(using = TimeDeserializer.class)
    @Column(name = "booking_start_date_time")
    private Date BookingStartDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    //@JsonFormat(pattern = "HH:mm:ss")
 //   @DateTimeFormat(pattern = "HH:mm:ss")
    //@JsonDeserialize(using = TimeDeserializer.class)
    @Column(name = "booking_end_date_time")
    private Date BookingEndDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "HH:mm:ss")
    @Column(name = "updated_at")
    private Date updatedAt;


    @Column(name = "comment")
    private String comment;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
