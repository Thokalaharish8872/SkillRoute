package com.example.auth_service.service;

import com.example.auth_service.dto.request.RegisterRequestDto;
import com.example.auth_service.dto.response.RegisterResponseDto;
import com.example.auth_service.entity.CodingProfiles;
import com.example.auth_service.entity.Profile;
import com.example.auth_service.entity.User;
import com.example.auth_service.mapper.UserMapper;
import com.example.auth_service.repository.ProfileRepo;
import com.example.auth_service.repository.UserRepo;
import com.example.auth_service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@Transactional
public class RegisterService {

    @Autowired
    UserRepo repo;

    @Autowired
    ProfileRepo profileRepo;

    @Autowired
    UserMapper mapper;

    @Autowired
    JwtService jwtService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<RegisterResponseDto> createAccount(RegisterRequestDto request){

        RegisterResponseDto response = new RegisterResponseDto();

        if(request.getEmail() == null || request.getEmail().isBlank()) {
            response.setMessage("Email is required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(request.getPassword() == null || request.getPassword().isBlank()) {
            response.setMessage("Password is required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(repo.findByEmail(request.getEmail()) != null){
            response.setMessage("User Already Exist");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        User user = mapper.toEntity(request);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setJoinDate(LocalDate.now());
        user.setLastLogin(LocalDate.now());
        user.setStreak(0);
        user.setRoadmapProgress(0);
        user.setSkillsLearned(0);
        user.setAssessmentCompleted(0);
        user.setCareerPaths(new ArrayList<>());
        user = repo.save(user);

        // Create initial Profile for new user
        Profile profile = new Profile();
        profile.setUserId(user.getId());
        profile.setUserName(user.getUsername() != null ? user.getUsername() : request.getUsername());
        profile.setEmail(user.getEmail());
        profile.setRole("Developer");
        profile.setLocation("Remote");

        CodingProfiles codingProfiles = new CodingProfiles();
        codingProfiles.setLeetcode("");
        codingProfiles.setGithub("");
        codingProfiles.setCodechef("");
        codingProfiles.setCodeforces("");
        profile.setCodingProfiles(codingProfiles);

        Profile savedProfile = profileRepo.save(profile);
        user.setProfile(savedProfile);
        repo.save(user);

        System.out.println("User and Profile created successfully");

        String token = jwtService.generateToken(user.getEmail(), user.getId());
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setMessage("Account Created Successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
