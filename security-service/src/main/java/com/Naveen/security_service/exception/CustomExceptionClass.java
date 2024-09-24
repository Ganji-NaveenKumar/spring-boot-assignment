package com.Naveen.security_service.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomExceptionClass {
    private int status;
    private String message;

    private String timestamp;
}
