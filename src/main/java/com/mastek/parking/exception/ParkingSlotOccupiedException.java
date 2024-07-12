package com.mastek.parking.exception;

public class ParkingSlotOccupiedException extends RuntimeException{
    public ParkingSlotOccupiedException(String message) {
        super(message);
    }
}
