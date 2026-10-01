package com.example.activity_service.controllers;

import com.example.activity_service.models.Activity;
import com.example.activity_service.models.Response.ActivityResponse;
import com.example.activity_service.models.Response.UserActivityResponse;
import com.example.activity_service.models.UserActivity;
import com.example.activity_service.models.requests.UserActivityRequest;
import com.example.activity_service.services.UserActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/activity")
public class UserActivityController {

    private static final Logger logger = LoggerFactory.getLogger(UserActivityController.class);

    @Autowired
    UserActivityService service;

    @Autowired
    com.example.activity_service.security.JwtService jwtService;

    @PostMapping("/update_activity")
    public ActivityResponse updateActivity(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody UserActivityRequest request){
        if (request.getUserId() == 0 && authHeader != null) {
            Integer extracted = jwtService.extractUserId(authHeader);
            if (extracted != null) {
                request.setUserId(extracted);
            }
        }
        logger.info("received update_activity for user : {}", request.getUserId());

        return service.updateActivity(request);
    }

    @GetMapping("/get_recent_activity")
    public UserActivityResponse getRecentActivity(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer userId){
        if (userId == null && authHeader != null) {
            userId = jwtService.extractUserId(authHeader);
        }
        if (userId == null) {
            userId = 1;
        }
        logger.info("received get_recent_activity for user : {}", userId);

        return service.getRecentActivity(userId);
    }
}

