package com.mastek.parking.exception;

public class DuplicateParkingSlotException extends RuntimeException {
    public DuplicateParkingSlotException(String message) {
        super(message);
    }
}
