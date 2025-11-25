package com.example.alfa.exceptions;

public class UserNotFoundException  extends RuntimeException {

    public UserNotFoundException(Long id) {
        super(String.format("User with Id %d not found", id));
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long id, String message) {
        super(message + " id : " + id);
    }

}
