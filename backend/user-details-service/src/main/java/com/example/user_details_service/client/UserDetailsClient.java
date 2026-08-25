package com.example.user_details_service.client;

import com.example.user_details_service.dto.UserDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-details-service")
public interface UserDetailsClient {

    @GetMapping("/user-details/{userId}")
    UserDetailsResponse getSkillsAndCareerPaths(
            @PathVariable int userId);

    @PutMapping("/user-details/career-paths/{userId}")
    void updateCareerPaths(
            @PathVariable int userId,
            @RequestBody List<Integer> careerPaths);
}
