package com.example.skillroute_api_gateway.client;

import com.example.skillroute_api_gateway.config.FeignConfiguration;
import com.example.skillroute_api_gateway.dto.UserActivity;
import com.example.skillroute_api_gateway.dto.request.ProgressUpdateRequest;
import com.example.skillroute_api_gateway.dto.request.UserActivityRequest;
import com.example.skillroute_api_gateway.dto.response.ActivityResponse;
import com.example.skillroute_api_gateway.dto.response.ProgressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "activity-service", configuration = FeignConfiguration.class)
public interface ActivityClient {

    @PostMapping("/api/progress/update_active_time")
    void updateActiveTime(@RequestBody ProgressUpdateRequest request);

    @GetMapping("/api/progress/get_progress")
    ProgressResponse getProgress(@RequestParam(value = "userId", required = false) Integer userId) throws Exception;

    @PostMapping("/api/streak/update_streak")
    void updateStreak(@RequestParam(value = "userId", required = false) Integer userId);

    @GetMapping("/api/streak/get_streak")
    int getStreak(@RequestParam(value = "userId", required = false) Integer userId);

    @PostMapping("/api/activity/update_activity")
    ActivityResponse updateActivity(@RequestBody UserActivityRequest request);

    @GetMapping("/api/activity/get_recent_activity")
    UserActivity getRecentActivity(@RequestParam(value = "userId", required = false) Integer userId);
}
