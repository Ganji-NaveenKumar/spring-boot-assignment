package com.Naveen.activity_service.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class CustomExceptionClass {
    private int status;
    private String message;
    private long timeStamp;
}
