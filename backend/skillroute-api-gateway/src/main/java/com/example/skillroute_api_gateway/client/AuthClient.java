package com.example.skillroute_api_gateway.client;

import com.example.skillroute_api_gateway.config.FeignConfiguration;
import com.example.skillroute_api_gateway.dto.request.LoginRequestDto;
import com.example.skillroute_api_gateway.dto.request.RegisterRequestDto;
import com.example.skillroute_api_gateway.dto.response.LoginResponseDto;
import com.example.skillroute_api_gateway.dto.response.RegisterResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth-service", configuration = FeignConfiguration.class)
public interface AuthClient {

    @PostMapping("/auth/register")
    ResponseEntity<RegisterResponseDto> register(RegisterRequestDto registerRequestDto);

    @PostMapping("/auth/login")
    ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto);
}
