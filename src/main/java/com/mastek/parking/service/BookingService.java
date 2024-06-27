package com.mastek.parking.service;

import com.mastek.parking.dto.BookingDto;
import com.mastek.parking.dto.UpdateBookingDto;
import com.mastek.parking.model.Booking;
import com.mastek.parking.model.Parking;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    @Transactional()
    List<Parking> checkAvailability(String location);

    @Transactional
    String bookParkingSlot(BookingDto bookingDto);

    @Transactional
    boolean cancelBooking(String bookingId);


    @Transactional
    String updateBooking(UpdateBookingDto updateBookingDto);
}