package com.Naveen.user_service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<CustomExceptionClass> handleException(UserNotFoundException exc){
        CustomExceptionClass custom=new CustomExceptionClass();
        custom.setMessage(exc.getMessage());
        custom.setStatus(HttpStatus.NOT_FOUND.value());
        custom.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(custom,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CustomExceptionClass> handleException(Exception exc){
        CustomExceptionClass custom=new CustomExceptionClass();
        custom.setStatus(HttpStatus.BAD_REQUEST.value());
        custom.setMessage(exc.getMessage());
        custom.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(custom,HttpStatus.BAD_REQUEST);
    }
}
