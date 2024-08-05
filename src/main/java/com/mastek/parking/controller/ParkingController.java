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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mastek.parking.service.UserDetailService;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;


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


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<User>> getUserByEmail(@Valid @RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null) {
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Email is required", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.getUserByEmail(email);
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.OK.value(), "User details found", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (UserNotFoundException e) {
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to retrieve user details", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<ApiResponse<User>> addUser(@Valid @RequestBody UserDto userDto) {
        try {
            User newUser = userService.addUser(userDto);
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.CREATED.value(), "User added successfully", newUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidOfficialEmailException e) {
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),  e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (ExistingUserException e) {
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }catch (Exception e) {
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to add user", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/updateUser")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @RequestParam String email,
            @Valid @RequestBody UserUpdateDto userUpdateDto) {
        System.out.println("Email for update : " + email);
        System.out.println("Request Body for update: " + userUpdateDto);
        try {
            User updatedUser = userService.updateUser(email, userUpdateDto);
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.OK.value(), "User details updated successfully.", updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            ApiResponse<User> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update user details.", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            String token = userService.login(loginRequest);
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "Login successful", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }catch (Exception ex) {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "failed to login" + ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        try {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "Logout successful", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (AuthenticationException e) {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to Logout", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody LoginRequestDto forgotPassword) {
        try {
            String resetLink = userService.forgotPassword(forgotPassword.getEmail());
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "Password reset email sent", resetLink);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @RequestParam String token, @RequestBody LoginRequestDto newPassword) {
        try {
            userService.resetPassword(token, newPassword.getPassword());
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Password reset successful", null));
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid token", null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }

    @PostMapping("/addParkingSlot")
    public ResponseEntity<ApiResponse<Parking>> addParkingSlot(@Valid @RequestBody ParkingDto parkingDto) {
        try {
            Parking savedParking = parkingService.addParkingSlot(parkingDto);
            ApiResponse<Parking> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Parking slot added successfully", savedParking);
            return new ResponseEntity<>(response, HttpStatus.OK);
           // return new ResponseEntity<>(savedParking+ " Parking added successfully", HttpStatus.CREATED);
        } catch (DuplicateParkingSlotException e) {
            ApiResponse<Parking> response = new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getParkingSlots")
    public ResponseEntity<ApiResponse<List<Parking>>> getParking() {
        try {
            List<Parking> parking = parkingService.getParking();
            ApiResponse<List<Parking>> response = new ApiResponse<>(HttpStatus.OK.value(), "Parking slots retrieved successfully", parking);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ParkingNotFoundException ex) {
            ApiResponse<List<Parking>> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }  catch (ParkingSlotUnavailableException ex) {
            ApiResponse<List<Parking>> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ApiResponse<List<Parking>> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/deleteParking")
    public ResponseEntity<ApiResponse> deleteParkingSlot(@RequestParam Long parkingSlotNumber) {
        try {
            parkingService.deleteParkingSlot(parkingSlotNumber);
            ApiResponse response = new ApiResponse<>(HttpStatus.OK.value(), "Parking slot deleted successfully", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (ParkingNotFoundException e) {
            ApiResponse response = new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }catch (Exception e) {
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while deleting the parking slot", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        try {
            Booking newBooking = bookingService.bookParkingSlot(bookingRequest);
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Booking created successfully with ID: "+newBooking.getBookingId(), newBooking);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidBookingDateException e) {
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//"User not found. Cannot book a slot.
        } catch (UserNotActiveException e) {
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//"User is not active. Cannot book a slot."
        } catch (ParkingSlotUnavailableException e) {
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//"User is not active. Cannot book a slot."
        }catch (ParkingNotFoundException e) {
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//"User is not active. Cannot book a slot."
        } catch (DuplicateBookingException e) {
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.CONTINUE.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);//"User is not active. Cannot book a slot."
        } catch (Exception e) {
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to book parking slot. Please try again later.", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/booking")
    public ResponseEntity<Booking> getBookingById(@RequestParam String bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/cancel-booking")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(@Valid @RequestBody CancelDto cancelDto) {

        try {
            bookingService.cancelBooking(cancelDto.getBookingId());
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK.value(), cancelDto.getBookingId()+ " : cancelled successfully.", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }  catch (InvalidBookingException e) {
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Booking Id is Invalid", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to cancel booking", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update-booking")
    public ResponseEntity<ApiResponse<Booking>> updateBooking(@Valid
            @RequestBody UpdateBookingDto updateBookingDto) {

        try {
            Booking updatedBooking = bookingService.updateBooking(updateBookingDto);
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.OK.value(), "Booking updated successfully", updatedBooking);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidBookingDateException e) {
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<Booking> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update booking", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}