package com.mastek.parking.exception;

public class InvalidOfficialEmailException extends RuntimeException {
    public InvalidOfficialEmailException(String message) {
        super(message);
    }
}
