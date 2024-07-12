package com.mastek.parking.exception;

public class InvalidBookingTimeFormatException extends RuntimeException{
    public InvalidBookingTimeFormatException(String message) {
        super(message);
    }
}
