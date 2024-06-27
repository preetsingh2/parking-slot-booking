package com.mastek.parking.exception;

public class DuplicateBookingException extends RuntimeException{
    public DuplicateBookingException(String message) {
        super(message);
    }
}
