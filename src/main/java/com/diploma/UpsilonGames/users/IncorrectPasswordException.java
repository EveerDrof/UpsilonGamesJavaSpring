package com.diploma.UpsilonGames.users;

public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException(String errorMessage, Throwable err){
        super(errorMessage,err);
    }
}
