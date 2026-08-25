package com.example.skillroute_api_gateway.controller;

import com.example.skillroute_api_gateway.client.ActivityClient;
import com.example.skillroute_api_gateway.dto.UserActivity;
import com.example.skillroute_api_gateway.dto.request.ProgressUpdateRequest;
import com.example.skillroute_api_gateway.dto.request.UserActivityRequest;
import com.example.skillroute_api_gateway.dto.response.ActivityResponse;
import com.example.skillroute_api_gateway.dto.response.ProgressResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ActivityGatewayController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityGatewayController.class);

    @Autowired
    private ActivityClient activityClient;

    @PostMapping("/api/progress/update_active_time")
    public void updateActiveTime(@RequestBody ProgressUpdateRequest request) {
        logger.info("Received update_active_time request for userId: {}", request.getUserId());
        activityClient.updateActiveTime(request);
    }

    @GetMapping("/api/progress/get_progress")
    public ProgressResponse getProgress(@RequestParam int userId) {
        logger.info("Received get_progress request for userId: {}", userId);
        return activityClient.getProgress(userId);
    }

    @PostMapping("/api/streak/update_streak")
    public void updateStreak(@RequestParam int userId) {
        logger.info("Received update_streak request for userId: {}", userId);
        activityClient.updateStreak(userId);
    }

    @GetMapping("/api/streak/get_streak")
    public int getStreak(@RequestParam int userId) {
        logger.info("Received get_streak request for userId: {}", userId);
        return activityClient.getStreak(userId);
    }

    @PostMapping("/api/activity/update_activity")
    public ActivityResponse updateActivity(@RequestBody UserActivityRequest request) {
        logger.info("Received update_activity request for userId: {}", request.getUserId());
        return activityClient.updateActivity(request);
    }

    @GetMapping("/api/activity/get_recent_activity")
    public UserActivity getRecentActivity(@RequestParam int userId) {
        logger.info("Received get_recent_activity request for userId: {}", userId);
        return activityClient.getRecentActivity(userId);
    }
}
