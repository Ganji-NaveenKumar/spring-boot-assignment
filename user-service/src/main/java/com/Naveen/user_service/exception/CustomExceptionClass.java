package com.Naveen.user_service.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public  @Data class CustomExceptionClass {
    private int status;
    private String message;
    private long timeStamp;
}
