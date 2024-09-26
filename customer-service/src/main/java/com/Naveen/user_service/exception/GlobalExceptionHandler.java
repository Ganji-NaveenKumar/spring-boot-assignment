package com.Naveen.user_service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomExceptionClass> handleTypeMismatch(MethodArgumentTypeMismatchException exc) {
        CustomExceptionClass custom = new CustomExceptionClass();
        custom.setMessage("Invalid value for parameter: " + exc.getName() + ". Expected type: " + Objects.requireNonNull(exc.getRequiredType()).getSimpleName());
        custom.setStatus(HttpStatus.BAD_REQUEST.value());
        custom.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(custom, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomExceptionClass> handleException(UserNotFoundException exc){
        CustomExceptionClass custom=new CustomExceptionClass();
        custom.setMessage(exc.getMessage());
        custom.setStatus(HttpStatus.NOT_FOUND.value());
        custom.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(custom,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<CustomExceptionClass> handleException(InvalidRequestException exc){
        CustomExceptionClass custom=new CustomExceptionClass();
        custom.setTimeStamp(System.currentTimeMillis());
        custom.setMessage("Invalid Url");
        custom.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(custom,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionClass> handleException(Exception exc){
        CustomExceptionClass custom=new CustomExceptionClass();
        custom.setStatus(HttpStatus.BAD_REQUEST.value());
        custom.setMessage("Invalid Url");
        custom.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(custom,HttpStatus.BAD_REQUEST);
    }
}
