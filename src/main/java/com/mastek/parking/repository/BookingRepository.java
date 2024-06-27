package com.mastek.parking.repository;

import com.mastek.parking.dto.BookingDto;
import com.mastek.parking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    //@Query("SELECT b FROM Booking b WHERE b.parkingSlotNumber = :parkingSlotNumber AND b.bookingDate = :bookingDate")
    @Query("SELECT b FROM Booking b WHERE b.parkingSlotNumber = :parkingSlotNumber")
    Optional<Booking> findByParkingSlotNumberAndBookingDate(@Param("parkingSlotNumber") Long parkingSlotNumber);


}