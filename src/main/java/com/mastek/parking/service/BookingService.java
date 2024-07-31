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
    Booking bookParkingSlot(BookingDto bookingDto);

    @Transactional
    public Booking getBookingById(String id);

    @Transactional
    void cancelBooking(String bookingId);


    @Transactional
    Booking updateBooking(UpdateBookingDto updateBookingDto);


}
