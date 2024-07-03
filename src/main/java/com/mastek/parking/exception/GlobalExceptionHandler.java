package com.mastek.parking.exception;

import com.mastek.parking.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidBookingDateException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidBookingDateException(InvalidBookingDateException ex, WebRequest request) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateBookingException.class)
    public ResponseEntity<ApiResponse> handleDuplicateBookingException(DuplicateBookingException ex) {
        ApiResponse response = new ApiResponse(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleAllExceptions(Exception ex, WebRequest request) {
        ApiResponse<String> response = new ApiResponse<>(false, "An error occurred: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
