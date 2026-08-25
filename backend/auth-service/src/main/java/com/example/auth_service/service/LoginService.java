package com.example.auth_service.service;

import com.example.auth_service.dto.request.LoginRequestDto;
import com.example.auth_service.dto.response.LoginResponseDto;
import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto request) {

        System.out.println("login request called " + request );
        LoginResponseDto response = new LoginResponseDto();

        User user = userRepo.findByEmail(request.getEmail());

        System.out.println(user);
        if (user == null) {
            response.setMessage("User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.setMessage("Invalid password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user.getEmail());
        response.setToken(token);
        response.setMessage("Login successful");
        response.setEmail(user.getEmail());

        System.out.println("success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
