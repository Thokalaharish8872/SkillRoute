package com.example.auth_service.controller;

import com.example.auth_service.dto.request.LoginRequestDto;
import com.example.auth_service.dto.response.LoginResponseDto;
import com.example.auth_service.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        System.out.println("came login request");
        return loginService.login(request);
    }
}
