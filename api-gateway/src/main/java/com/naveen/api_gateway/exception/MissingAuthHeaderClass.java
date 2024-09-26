package com.naveen.api_gateway.exception;

public class MissingAuthHeaderClass extends RuntimeException{
    public MissingAuthHeaderClass(String message){
        super(message);
    }
}
