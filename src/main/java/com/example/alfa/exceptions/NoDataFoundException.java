package com.example.alfa.exceptions;

public class NoDataFoundException  extends RuntimeException{

    public NoDataFoundException(Long id, String message) {
        super("No data found  : id - "+ id +" "+ message);
    }

    public NoDataFoundException() {
        super(" No data found" );
    }

    public NoDataFoundException(String message) {
        super("No data found : " + message);
    }
}
