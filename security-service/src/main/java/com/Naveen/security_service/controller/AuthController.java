package com.Naveen.security_service.controller;


import com.Naveen.security_service.dto.AuthRequest;
import com.Naveen.security_service.entity.UserCredential;
import com.Naveen.security_service.exception.InvalidTokenException;
import com.Naveen.security_service.exception.InvalidUserDetails;
import com.Naveen.security_service.exception.InvalidUserException;
import com.Naveen.security_service.service.AuthService;
import com.Naveen.security_service.utils.Constant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(Constant.REGISTER_URL)
    public String addNewUser(@Valid @RequestBody UserCredential userCredential, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:---> ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append("-->  ").append(error.getDefaultMessage()).append(";   ")
            );
            throw new InvalidUserDetails(errorMessage.toString());
        }
        return authService.saveUser(userCredential);
    }

    @PostMapping(Constant.TOKEN)
    public String generateUser(@RequestBody AuthRequest authRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            return authService.generateToken(authRequest.getUsername());

        } catch (AuthenticationException e) {
            throw new InvalidUserException("Invalid User Credentials");
        }
    }

    @GetMapping(Constant.VALIDATE)
    public String validateToken(@RequestParam("token") String token){
        try{
            authService.validateToken(token);
            return "Valid User";
        }
        catch (Exception e){
            throw new InvalidTokenException("Invalid Token");
        }
    }
}
