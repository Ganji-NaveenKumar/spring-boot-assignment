package com.naveen.api_gateway.exception;

public class UnAuthorizedUserClass extends RuntimeException{
    public UnAuthorizedUserClass(String message){
        super(message);
    }
}
