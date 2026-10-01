package com.example.dashboard_service.client;

import com.example.dashboard_service.config.FeignConfiguration;
import com.example.dashboard_service.dto.CareerPathResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "career-path-service", configuration = FeignConfiguration.class)
public interface CareerPathClient {

    @GetMapping("/api/career_path/recommendations")
    CareerPathResponseDto getRecommendations(@RequestParam(value = "userId", required = false) Integer userId, @RequestParam(value = "refresh", required = false) boolean refresh);
}
