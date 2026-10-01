package com.example.dashboard_service.client;

import com.example.dashboard_service.config.FeignConfiguration;
import com.example.dashboard_service.dto.UserActivityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "activity-service", configuration = FeignConfiguration.class)
public interface ActivityClient {

    @GetMapping("/api/streak/get_streak")
    Map<String, Object> getStreak(@RequestParam(value = "userId", required = false) Integer userId);

    @GetMapping("/api/activity/get_recent_activity")
    UserActivityDto getRecentActivity(@RequestParam(value = "userId", required = false) Integer userId);
}
