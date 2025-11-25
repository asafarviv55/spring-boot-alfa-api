package com.example.alfa.exceptions;

public class UserExistsException   extends RuntimeException {


    public UserExistsException(Long id) {
        super("user allready exists  : id - "+ id );
    }



    public UserExistsException(String message) {
        super(message);
    }


}

