package com.example.skillroute_api_gateway.controller;

import com.example.skillroute_api_gateway.client.AuthClient;
import com.example.skillroute_api_gateway.dto.request.LoginRequestDto;
import com.example.skillroute_api_gateway.dto.request.RegisterRequestDto;
import com.example.skillroute_api_gateway.dto.response.LoginResponseDto;
import com.example.skillroute_api_gateway.dto.response.RegisterResponseDto;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    final AuthClient client;

    public AuthController(AuthClient client){
        this.client = client;
    }

    @PostMapping("/register")
    private ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto request){
        logger.info("Received register request for email: {}", request.getEmail());
        try{
            return client.register(request);
        }
        catch (FeignException.Conflict e){

            RegisterResponseDto response = new RegisterResponseDto();
            response.setMessage("User Already Exist");

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(response);
        }
    }

    @PostMapping("/login")
    private ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        logger.info("Received login request for email: {}", request.getEmail());
        try {
            return client.login(request);
        }
        catch (FeignException.Conflict e){
            LoginResponseDto response = new LoginResponseDto();
            response.setMessage("An Error Occurred, please Try Again");

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(response);
        }
    }
}
