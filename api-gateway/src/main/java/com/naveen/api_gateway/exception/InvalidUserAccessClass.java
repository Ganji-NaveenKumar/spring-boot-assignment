package com.naveen.api_gateway.exception;

public class InvalidUserAccessClass extends RuntimeException{
    public InvalidUserAccessClass(String message){
        super(message);
    }
}
