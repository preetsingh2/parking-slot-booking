package com.mastek.parking.controller;

import com.mastek.parking.dto.BookingDto;
import com.mastek.parking.dto.ParkingDto;
import com.mastek.parking.dto.UpdateBookingDto;
import com.mastek.parking.dto.UserDto;
import com.mastek.parking.exception.DuplicateBookingException;
import com.mastek.parking.exception.DuplicateParkingSlotException;
import com.mastek.parking.model.Booking;
import com.mastek.parking.model.Parking;
import com.mastek.parking.model.User;
import com.mastek.parking.service.BookingService;
import com.mastek.parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import com.mastek.parking.service.UserDetailService;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.time.LocalTime;

@RestController
@RequestMapping("/api")
//@Api(tags = "Parking API")
public class ParkingController {

    @Autowired
    private UserDetailService userService;

   @Autowired
   private ParkingService parkingService;

    @Autowired
    private BookingService bookingService;

    /*@GetMapping("/availability")
    @ApiOperation(value = "Get parking availability", notes = "Returns a simple message", response = String.class)
    public String getParkingAvailability() {
        return "hello";
    }*/

    @GetMapping("/users")
   // @ApiOperation(value = "Get all users from the System", authorizations = {@Authorization(value = "Bearer")})
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    /*@ApiOperation(value = "Add user to the System", authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User added successfully"),
            @ApiResponse(code = 400, message = "Invalid input or validation error")
    })*/
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDto userDto) {
        //return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
        String result = userService.addUser(userDto);
        if (result.equals("User added successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    }

    @PostMapping("/addParkingSlot")
    public ResponseEntity<String> addParkingSlot(@Valid @RequestBody ParkingDto parkingDto) {
        try {
            Parking savedParking = parkingService.addParkingSlot(parkingDto);
            return new ResponseEntity<>(savedParking+ " Parking added successfully", HttpStatus.CREATED);
        } catch (DuplicateParkingSlotException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getParkingSlots")
    public ResponseEntity<List<Parking>> getParking() {
        List<Parking> parking = parkingService.getParking();
        return new ResponseEntity<>(parking, HttpStatus.OK);
    }


    @GetMapping("/check-availability")
    public ResponseEntity<List<Parking>> checkAvailability(
            @RequestParam String location
           /* @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date*/) {
        List<Parking> availableSlots = bookingService.checkAvailability(location);
        return ResponseEntity.ok(availableSlots);
    }

    @PostMapping("/book-slot")
    public ResponseEntity<String> bookParkingSlot(
            @RequestBody BookingDto bookingRequest) {
       // boolean success = bookingService.bookParkingSlot(bookingRequest);
        try {
        String bookingId = bookingService.bookParkingSlot(bookingRequest);
        if (bookingId != null) {
            return ResponseEntity.ok("Parking slot booked successfully. Your Booking ID is : "+ bookingId);
        } else {
            return ResponseEntity.badRequest().body("Failed to book parking slot."); // Or any suitable error indicator
        }
        } catch (DuplicateBookingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }



    @PostMapping("/cancel-booking")
    public ResponseEntity<String> cancelBooking(@RequestParam String bookingId) {
        boolean success = bookingService.cancelBooking(bookingId);
        if (success) {
            return ResponseEntity.ok("Booking canceled successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to cancel booking.");
        }
    }

    @PostMapping("/update-booking")
    public ResponseEntity<String> updateBooking(
            @RequestBody UpdateBookingDto updateBookingDto) {
        //boolean success = bookingService.updateBooking(updateBookingDto);
        String bookingId = bookingService.updateBooking(updateBookingDto);
        if (bookingId != null) {
            return ResponseEntity.ok("Your Booking for booking ID " + bookingId +" updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update booking."); // Or any suitable error indicator
        }

    }

    /*@GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(@RequestParam String location, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        boolean isAvailable = bookingService.checkAvailability(location, date);
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping("/bookSlot")
    public ResponseEntity<Booking> bookParkingSlot(@RequestBody Booking booking) {
        Booking savedBooking = bookingService.bookParkingSlot(booking);
        return ResponseEntity.ok(savedBooking);
    }

    @DeleteMapping("/bookingSlot/cancel/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        boolean isCancelled = bookingService.cancelBooking(id);
        if (isCancelled) {
            return ResponseEntity.ok("Booking cancelled successfully");
        } else {
            return ResponseEntity.status(404).body("Booking not found");
        }
    }

    @PutMapping("/bookingSlot/update/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        Optional<Booking> updatedBooking = bookingService.updateBooking(id, booking);
        if (updatedBooking.isPresent()) {
            return ResponseEntity.ok(updatedBooking.get());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }*/

}