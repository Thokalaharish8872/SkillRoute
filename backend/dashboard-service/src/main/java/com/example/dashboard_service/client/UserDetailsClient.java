package com.example.dashboard_service.client;

import com.example.dashboard_service.config.FeignConfiguration;
import com.example.dashboard_service.dto.GetSkillsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-details-service", configuration = FeignConfiguration.class)
public interface UserDetailsClient {

    @GetMapping("/api/user_skills/get_user_skills")
    GetSkillsResponseDto getUserSkills(@RequestParam(value = "userId", required = false) Integer userId);
}
