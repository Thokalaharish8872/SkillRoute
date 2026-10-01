package com.example.user_details_service.controller;

import com.example.user_details_service.entity.Profile;
import com.example.user_details_service.entity.Response.ProfileResponse;
import com.example.user_details_service.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/profile")
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    ProfileService service;

    @Autowired
    com.example.user_details_service.security.JwtService jwtService;

    @GetMapping("/get_profile")
    public ProfileResponse getProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer userId) throws Exception {
        if (userId == null && authHeader != null) {
            userId = jwtService.extractUserId(authHeader);
        }
        if (userId == null) {
            userId = 1;
        }
        logger.info("received get_profile request from userId : {}", userId);
        return service.getProfile(userId);
    }

    @PostMapping("/create_profile")
    public ProfileResponse createProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Profile profile) throws Exception {
        if ((profile.getUserId() == null || profile.getUserId() == 0) && authHeader != null) {
            Integer extracted = jwtService.extractUserId(authHeader);
            if (extracted != null) {
                profile.setUserId(extracted);
            }
        }
        logger.info("received create_profile request for userId : {}", profile.getUserId());
        return service.createProfile(profile);
    }

    @PostMapping("/update_profile")
    public ProfileResponse updateProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Profile profile) throws Exception {
        if ((profile.getUserId() == null || profile.getUserId() == 0) && authHeader != null) {
            Integer extracted = jwtService.extractUserId(authHeader);
            if (extracted != null) {
                profile.setUserId(extracted);
            }
        }
        logger.info("received update_profile request from userId : {}", profile.getUserId());
        return service.updateProfile(profile);
    }
}



