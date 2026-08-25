package com.example.auth_service.controller;

import com.example.auth_service.dto.request.RegisterRequestDto;
import com.example.auth_service.dto.response.RegisterResponseDto;
import com.example.auth_service.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class RegisterController {

    @Autowired
    RegisterService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto request){
        return service.createAccount(request);
    }
}
