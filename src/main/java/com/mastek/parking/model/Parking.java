package com.mastek.parking.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "\"parking_slot_details\"")
@Data
public class Parking {

    @Column(name = "location")
    private String location;

    @Column(name = "is_occupied")
    private Boolean isOccupied;

    @Id
    @Column(name = "slot_number")
    private Long slotNumber;

    @Column(name = "created_by")
    private String createdBy;


}
