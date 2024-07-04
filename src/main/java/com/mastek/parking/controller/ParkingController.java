package com.mastek.parking.controller;

import com.mastek.parking.common.ApiResponse;
import com.mastek.parking.dto.*;
import com.mastek.parking.exception.*;
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

    @PostMapping("/addUser")
    /*@ApiOperation(value = "Add user to the System", authorizations = {@Authorization(value = "Bearer")})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User added successfully"),
            @ApiResponse(code = 400, message = "Invalid input or validation error")
    })*/
    public ResponseEntity<ApiResponse<User>> addUser(@Valid @RequestBody UserDto userDto) {
        //return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
        try {
            User newUser = userService.addUser(userDto);
            ApiResponse<User> response = new ApiResponse<>(true, "User added successfully.", newUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ExistingUserException e) {
            ApiResponse<User> response = new ApiResponse<>(false, "Existing User.", null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }catch (Exception e) {
            ApiResponse<User> response = new ApiResponse<>(false, "Failed to add user.", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
       /* if (result.equals("User added successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }*/

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
    public ResponseEntity<ApiResponse<Booking>> bookParkingSlot(
            @RequestBody BookingDto bookingRequest) {
        // boolean success = bookingService.bookParkingSlot(bookingRequest);
       /* try {
        String bookingId = bookingService.bookParkingSlot(bookingRequest);
        if (bookingId != null) {
            return ResponseEntity.ok("Parking slot booked successfully. Your Booking ID is : "+ bookingId);
        } else {
            return ResponseEntity.badRequest().body("Failed to book parking slot."); // Or any suitable error indicator
        }
        } catch (DuplicateBookingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }*/

        try {
            Booking newBooking = bookingService.bookParkingSlot(bookingRequest);
            ApiResponse<Booking> response = new ApiResponse<>(true, "Parking slot booked successfully.", newBooking);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidBookingDateException e) {
            ApiResponse<Booking> response = new ApiResponse<>(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<Booking> response = new ApiResponse<>(false, "Parking slot unavailable", null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }



    @PostMapping("/cancel-booking")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(@RequestBody CancelDto cancelDto) {
        // boolean success = bookingService.cancelBooking(bookingId);
        /*if (success) {
            return ResponseEntity.ok("Booking canceled successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to cancel booking.");
        }*/
        try {
            bookingService.cancelBooking(cancelDto.getBookingId());
            ApiResponse<Void> response = new ApiResponse<>(true, "Booking "+cancelDto.getBookingId()+ " canceled successfully.", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }  catch (InvalidBookingException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Booking Id is Invalid.", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Failed to cancel booking.", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-booking")
    public ResponseEntity<ApiResponse<Booking>> updateBooking(
            @RequestBody UpdateBookingDto updateBookingDto) {
        //boolean success = bookingService.updateBooking(updateBookingDto);
        /*String bookingId = bookingService.updateBooking(updateBookingDto);
        if (bookingId != null) {
            return ResponseEntity.ok("Your Booking for booking ID " + bookingId +" updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update booking."); // Or any suitable error indicator
        }*/
        try {
            Booking updatedBooking = bookingService.updateBooking(updateBookingDto);
            ApiResponse<Booking> response = new ApiResponse<>(true, "Booking updated successfully.", updatedBooking);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidBookingDateException e) {
            ApiResponse<Booking> response = new ApiResponse<>(false, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<Booking> response = new ApiResponse<>(false, "Failed to update booking.", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}