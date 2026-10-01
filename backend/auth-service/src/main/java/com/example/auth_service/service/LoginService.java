package com.example.auth_service.service;

import com.example.auth_service.dto.request.LoginRequestDto;
import com.example.auth_service.dto.response.LoginResponseDto;
import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepo;
import com.example.auth_service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class LoginService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<LoginResponseDto> login(LoginRequestDto request) {

        System.out.println("login request called " + request );
        LoginResponseDto response = new LoginResponseDto();

        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            response.setMessage("Email and password are required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = userRepo.findByEmail(request.getEmail());

        if (user == null) {
            response.setMessage("User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.setMessage("Invalid password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        userRepo.updateLastLogin(user.getId(), LocalDate.now());

        String token = jwtService.generateToken(user.getEmail(), user.getId());
        response.setToken(token);
        response.setMessage("Login successful");
        response.setEmail(user.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
