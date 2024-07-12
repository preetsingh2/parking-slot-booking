package com.mastek.parking.service;

import com.mastek.parking.dto.BookingDto;
import com.mastek.parking.dto.UpdateBookingDto;
import com.mastek.parking.exception.*;
import com.mastek.parking.model.Booking;
import com.mastek.parking.model.Parking;
import com.mastek.parking.model.User;
import com.mastek.parking.repository.BookingRepository;
import com.mastek.parking.repository.ParkingRepository;
import com.mastek.parking.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Transactional()
    public List<Parking> checkAvailability(String location) {
        return parkingRepository.findAvailableParkingSlots(location);
    }

    @Override
    @Transactional
    public Booking bookParkingSlot(BookingDto bookingDto) {

        //Validate past booking date
        validateBookingDate(bookingDto.getBookingStartDateTime());
        validateBookingDate(bookingDto.getBookingEndDateTime());

        // Check availability
        Parking parkingSlot = parkingRepository.findById(bookingDto.getParkingSlotNumber())
                .orElseThrow(() ->  new ParkingNotFoundException("Parking slot not found"));

        // Validate user before proceeding with booking
        validateUserForBooking(bookingDto);


        List<Parking> availableSlots = parkingRepository.findAvailableParkingSlots(parkingSlot.getLocation());
        if (availableSlots.isEmpty()) {
            throw new ParkingSlotUnavailableException("Slot not available");
        }
        Optional<Booking> existingBooking = bookingRepository.findByParkingSlotNumberAndBookingStatus(bookingDto.getParkingSlotNumber());
        if (existingBooking.isPresent()) {
            throw new DuplicateBookingException("Booking already exists for parking slot number: " + bookingDto.getParkingSlotNumber());
        }

        // Update parking slot
        parkingSlot.setIsOccupied(true);

        parkingRepository.save(parkingSlot);

        // Insert booking record
        Booking booking = new Booking();
        booking.setUsername(bookingDto.getUsername());
        booking.setUserEmail(bookingDto.getUserEmail());
        booking.setParkingSlotNumber(bookingDto.getParkingSlotNumber());
        booking.setBookingStartDateTime(bookingDto.getBookingStartDateTime());
        booking.setBookingEndDateTime(bookingDto.getBookingEndDateTime());
        booking.setComment(bookingDto.getComment());
        booking.setBookingStatus("Active");
        bookingRepository.save(booking);

        //send booking confirmation email notification
        sendBookingNotification("Booking Confirmation","Your booking is confirmed:", booking);

        return booking;
    }

    @Override
    @Transactional
    public void cancelBooking(String bookingId) {
        // Find booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new InvalidBookingException("Booking not found"));

        // Update parking slot
        Parking parkingSlot = parkingRepository.findById(booking.getParkingSlotNumber())
                .orElseThrow(() -> new ParkingNotFoundException("Parking slot not found"));

        parkingSlot.setIsOccupied(false);
        parkingRepository.save(parkingSlot);

        booking.setBookingStatus("Cancelled");
        booking.setComment("No reason");
        bookingRepository.save(booking);

        //send cancellation email notification
        sendBookingNotification(
                "Booking Cancellation Notification", "Your booking has been cancelled. Here are the details:", booking);
    }

    @Override
    @Transactional
    public Booking updateBooking(UpdateBookingDto updateBookingDto) {
        // Find booking
        Booking booking = bookingRepository.findById(updateBookingDto.getBookingId())
                .orElseThrow(() -> new InvalidBookingException("Booking not found"));

            // Validate new booking times
            validateBookingDate(updateBookingDto);

            // Update booking details
            booking.setBookingStartDateTime(updateBookingDto.getNewFromTime());
            booking.setBookingEndDateTime(updateBookingDto.getNewToTime());
            booking.setComment(updateBookingDto.getNewComment());
            booking.setBookingStatus("Modified");

        bookingRepository.save(booking);

        //send booking update email notification
        sendBookingNotification(
                "Booking Update Notification", "Your booking has been updated. Here are the details:", booking);

        return booking;
    }

    private void validateBookingDate(Date bookingDate) {
        if (bookingDate.before(new Date())) {
            throw new InvalidBookingDateException("Booking date cannot be in the past.");
        }

    }

    private void validateBookingDate(UpdateBookingDto updateBookingDto) {
        Date currentDate = new Date();

        if (updateBookingDto.getNewFromTime().before(currentDate)) {
            throw new InvalidBookingDateException("New 'from time' cannot be in the past.");
        }

        if (updateBookingDto.getNewToTime().before(currentDate)) {
            throw new InvalidBookingDateException("New 'to time' cannot be in the past.");
        }

        if (updateBookingDto.getNewFromTime().after(updateBookingDto.getNewToTime())) {
            throw new InvalidBookingDateException("'From time' cannot be after 'to time'.");
        }

    }
    public void validateUserForBooking(BookingDto bookingDto) {

        // Check if user exists in the database
        Optional<User> user=userRepository.findByEmail(bookingDto.getUserEmail());
        if(!user.isPresent())
        throw new  UserNotFoundException("Invalid User");

         if(!user.get().getStatus().equalsIgnoreCase("Active")) {
            throw new UserNotActiveException("User is not active and cannot book a slot.");
         }
    }

    public void updateBookingForEarlyExit(UpdateBookingDto updateBookingDto) {
        Booking booking = bookingRepository.findById(updateBookingDto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setActualLeaveTime(updateBookingDto.getActualLeaveTime());
        booking.setBookingStatus("Completed");

        Parking parkingSlot = parkingRepository.findById(booking.getParkingSlotNumber())
                .orElseThrow(() -> new ParkingNotFoundException("Parking slot not found"));

        parkingSlot.setIsOccupied(false);
        parkingRepository.save(parkingSlot);

        bookingRepository.save(booking);
    }

     public void sendBookingNotification(String subject, String context, Booking booking) {
         String body = "Dear " + booking.getUsername() + ",\n\n" +
                context +"\n\n" +
                "Booking ID: " + booking.getBookingId() + "\n" +
                "Start Time: " + booking.getBookingStartDateTime() + "\n" +
                 "End Time: " + booking.getBookingEndDateTime() + "\n\n" +
                "Thank you for using our service." +"\n\n"+
                "-Admin";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(booking.getUserEmail());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}

