package com.example.activity_service.controllers;

import com.example.activity_service.models.Activity;
import com.example.activity_service.models.Response.ActivityResponse;
import com.example.activity_service.models.Response.UserActivityResponse;
import com.example.activity_service.models.UserActivity;
import com.example.activity_service.models.requests.UserActivityRequest;
import com.example.activity_service.services.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/activity")
public class UserActivityController {

    @Autowired
    UserActivityService service;

    @PostMapping("/update_activity")
    public ActivityResponse updateActivity(@RequestBody UserActivityRequest request){
        return service.updateActivity(request);
    }

    @GetMapping("/get_recent_activity")
    public UserActivityResponse getRecentActivity(@RequestParam int userId){
        return service.getRecentActivity(userId);
    }
}

