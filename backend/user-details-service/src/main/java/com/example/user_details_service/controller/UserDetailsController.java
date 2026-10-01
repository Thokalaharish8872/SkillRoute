package com.example.user_details_service.controller;

import com.example.user_details_service.dto.UserDetailsServiceResponse;
import com.example.user_details_service.service.UserSkillsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserDetailsController {

    @Autowired
    private UserSkillsService service;

    @Autowired
    private com.example.user_details_service.security.JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsController.class);

    @GetMapping("/get_skills_and_career_paths")
    public UserDetailsServiceResponse getSkillsAndCareerPaths(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer userId) {
        if (userId == null && authHeader != null) {
            userId = jwtService.extractUserId(authHeader);
        }
        if (userId == null) {
            userId = 1;
        }
        logger.info("Received get_user_skills_and_career_paths request for userId: {}", userId);
        return service.getSkillsAndCareerPaths(userId);
    }

    @PostMapping("/update_career_paths")
    public void updateCareerPaths(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer userId,
            @RequestBody List<Integer> careerPaths) throws Exception {
        if (userId == null && authHeader != null) {
            userId = jwtService.extractUserId(authHeader);
        }
        if (userId == null) {
            userId = 1;
        }
        service.updateCareerPaths(userId, careerPaths);
    }
}
