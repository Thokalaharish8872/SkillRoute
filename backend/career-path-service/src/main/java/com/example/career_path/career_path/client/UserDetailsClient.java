package com.example.career_path.career_path.client;

import com.example.career_path.career_path.dto.UserDetailsServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-details-service", fallback = UserDetailsClientFallback.class)
public interface UserDetailsClient {

    @GetMapping("/get_skills_and_career_paths")
    UserDetailsServiceResponse getSkillsAndCareerPaths(@RequestParam("userId") int userId) throws Exception;

    @PostMapping("/update_career_paths")
    void updateCareerPaths(@RequestParam("userId") int userId, @RequestBody List<Integer> careerPaths) throws Exception;

    @GetMapping("/health")
    String health();
}
