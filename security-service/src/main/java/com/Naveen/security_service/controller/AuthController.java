package com.Naveen.security_service.controller;


import com.Naveen.security_service.dto.AuthRequest;
import com.Naveen.security_service.entity.UserCredential;
import com.Naveen.security_service.exception.CustomExceptionClass;
import com.Naveen.security_service.exception.InvalidUserException;
import com.Naveen.security_service.service.AuthService;
import com.Naveen.security_service.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(Constant.REGISTER_URL)
    public String addNewUser(@RequestBody UserCredential userCredential){
        return authService.saveUser(userCredential);
    }

    @PostMapping(Constant.TOKEN)
    public String generateUser(@RequestBody AuthRequest authRequest){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                ( authRequest.getUsername(),authRequest.getPassword()));
        if(authentication.isAuthenticated())  return  authService.generateToken(authRequest.getUsername());
        else throw  new InvalidUserException("UnAuthorized User");
    }

    @GetMapping(Constant.VALIDATE)
    public String validateToken(@RequestParam("token") String token){
        authService.validateToken(token);
        return "Valid User";
    }
}
