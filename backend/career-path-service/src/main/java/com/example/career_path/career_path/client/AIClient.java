package com.example.career_path.career_path.client;

import com.example.career_path.career_path.dto.AiServiceResponse;
import com.example.career_path.career_path.dto.GenerateCareerPathRequest;
import com.example.career_path.career_path.dto.GenerateRoadMapRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ai-service", fallback = AIClientFallback.class)
public interface AIClient {

    @PostMapping("/ai/career-paths")
    AiServiceResponse generateRecommendations(@RequestBody GenerateCareerPathRequest request) throws Exception;

    @PostMapping("/ai/skills")
    Object generateSkills(@RequestBody List<String> skills) throws Exception;

    @PostMapping("/ai/roadmap")
    Object generateRoadMap(@RequestBody GenerateRoadMapRequest request) throws Exception;
}
