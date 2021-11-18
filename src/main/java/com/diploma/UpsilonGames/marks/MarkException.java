package com.diploma.UpsilonGames.marks;

import org.springframework.http.HttpStatus;

public class MarkException extends Exception{
    private HttpStatus status;
    public MarkException(String errorText, HttpStatus status){
        super(errorText);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
