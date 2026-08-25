package com.example.auth_service.service;

import com.example.auth_service.dto.request.RegisterRequestDto;
import com.example.auth_service.dto.response.RegisterResponseDto;
import com.example.auth_service.entity.User;
import com.example.auth_service.mapper.UserMapper;
import com.example.auth_service.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class RegisterService {

    @Autowired
    UserRepo repo;

    @Autowired
    UserMapper mapper;

    @Autowired
    JwtService jwtService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<RegisterResponseDto> createAccount(RegisterRequestDto request){

        RegisterResponseDto response = new RegisterResponseDto();

        if(repo.findByEmail(request.getEmail()) != null){
            response.setMessage("User Already Exist");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        User user = mapper.toEntity(request);
        user.setPassword(encoder.encode(request.getPassword()));
        repo.save(user);

        System.out.println("created");

        String token = jwtService.generateToken(user.getEmail());
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setMessage("Account Created Successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
