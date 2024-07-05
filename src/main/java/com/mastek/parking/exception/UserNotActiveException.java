package com.mastek.parking.exception;

public class UserNotActiveException extends RuntimeException{

    public UserNotActiveException(String message) {
        super(message);
    }
}
