package com.mastek.parking.exception;

public class InvalidBookingDateException extends RuntimeException{

    public InvalidBookingDateException(String message) {
        super(message);
    }
}
