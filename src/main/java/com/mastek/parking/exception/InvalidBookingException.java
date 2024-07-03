package com.mastek.parking.exception;

public class InvalidBookingException extends RuntimeException{

    public InvalidBookingException(String message) {
        super(message);
    }

}
