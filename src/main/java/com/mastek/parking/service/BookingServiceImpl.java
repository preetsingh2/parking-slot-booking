package com.mastek.parking.service;

import com.mastek.parking.dto.BookingDto;
import com.mastek.parking.dto.UpdateBookingDto;
import com.mastek.parking.exception.DuplicateBookingException;
import com.mastek.parking.model.Booking;
import com.mastek.parking.model.Parking;
import com.mastek.parking.repository.BookingRepository;
import com.mastek.parking.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Override
    @Transactional()
    public List<Parking> checkAvailability(String location) {
        return parkingRepository.findAvailableParkingSlots(location);
    }

    @Override
    @Transactional
    public String bookParkingSlot(BookingDto bookingDto) {
        // Check availability
        Parking parkingSlot = parkingRepository.findById(bookingDto.getParkingSlotNumber()).orElseThrow(() -> new RuntimeException("Parking slot not found"));
        List<Parking> availableSlots = parkingRepository.findAvailableParkingSlots(parkingSlot.getLocation());
        if (availableSlots.isEmpty()) {
            return null; // Slot not available
        }
        Optional<Booking> existingBooking = bookingRepository.findByParkingSlotNumberAndBookingDate(bookingDto.getParkingSlotNumber());
        if (existingBooking.isPresent()) {
            throw new DuplicateBookingException("Booking already exists for parking slot number: " + bookingDto.getParkingSlotNumber());
        }

        // Update parking slot
        parkingSlot.setIsOccupied(true);
      //  parkingSlot.setAvailableSpots(parkingSlot.getAvailableSpots() - 1);
        parkingRepository.save(parkingSlot);

        // Insert booking record
        Booking booking = new Booking();
        booking.setUsername(bookingDto.getUsername());
        booking.setUserEmail(bookingDto.getUserEmail());
        booking.setParkingSlotNumber(bookingDto.getParkingSlotNumber());
        booking.setBookingDate(bookingDto.getBookingDate());
        booking.setBookingStartDateTime(bookingDto.getBookingStartDateTime());
        booking.setBookingEndDateTime(bookingDto.getBookingEndDateTime());
        booking.setComment(bookingDto.getComment());
        bookingRepository.save(booking);

        return booking.getBookingId();
    }

    @Override
    @Transactional
    public boolean cancelBooking(String bookingId) {
        // Find booking
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));

        // Update parking slot
        Parking parkingSlot = parkingRepository.findById(booking.getParkingSlotNumber()).orElseThrow(() -> new RuntimeException("Parking slot not found"));
        parkingSlot.setIsOccupied(false);
       // parkingSlot.setAvailableSpots(parkingSlot.getAvailableSpots() + 1);
        parkingRepository.save(parkingSlot);

        // Delete booking record
        bookingRepository.delete(booking);

        return true;
    }

    @Override
    @Transactional
    public String updateBooking(UpdateBookingDto updateBookingDto) {
        // Find booking
        Booking booking = bookingRepository.findById(updateBookingDto.getBookingId()).orElseThrow(() -> new RuntimeException("Booking not found"));

        // Update booking details
        booking.setBookingStartDateTime(updateBookingDto.getNewFromTime());
        booking.setBookingEndDateTime(updateBookingDto.getNewToTime());
        booking.setComment(updateBookingDto.getNewComment());
        bookingRepository.save(booking);

        return booking.getBookingId();
    }


}
