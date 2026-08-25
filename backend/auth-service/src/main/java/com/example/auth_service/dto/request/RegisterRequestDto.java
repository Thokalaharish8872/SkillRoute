package com.example.auth_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequestDto {

    private String username;
    private String email;
    private String password;
}
