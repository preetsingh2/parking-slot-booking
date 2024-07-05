package com.mastek.parking.exception;

public class ParkingSlotUnavailableException extends RuntimeException{

    public ParkingSlotUnavailableException(String message) {
        super(message);
    }
}
