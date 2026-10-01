package com.example.activity_service.controllers;

import com.example.activity_service.services.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/streak/")
@CrossOrigin
public class StreakController {

    @Autowired
    StreakService service;

    @Autowired
    com.example.activity_service.security.JwtService jwtService;

    @PostMapping("/update_streak")
    public void updateSteak(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer userId){
        if (userId == null && authHeader != null) {
            userId = jwtService.extractUserId(authHeader);
        }
        if (userId == null) {
            userId = 1;
        }
        service.updateStreak(userId);
    }

    @GetMapping("/get_streak")
    public int getStreak(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer userId){
        if (userId == null && authHeader != null) {
            userId = jwtService.extractUserId(authHeader);
        }
        if (userId == null) {
            userId = 1;
        }
        return service.getStreak(userId);
    }
}

