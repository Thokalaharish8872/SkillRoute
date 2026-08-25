package com.example.ai_service.controller;

import com.example.ai_service.dto.AiServiceResponse;
import com.example.ai_service.dto.GenerateCareerPathRequest;
import com.example.ai_service.dto.GenerateRoadMapRequest;
import com.example.ai_service.dto.GenerateSkillsRequest;
import com.example.ai_service.service.GeminiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final GeminiService geminiService;

    public AIController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/career-paths")
    public AiServiceResponse generateCareerPaths(
            @RequestBody GenerateCareerPathRequest request) throws Exception {

        System.out.println("started");

        //        System.out.println(response.getGeneratedCareerPaths());
        return geminiService.generateRecommendations(request.getSkills());
    }

    @PostMapping("/roadmap")
    public AiServiceResponse generateRoadMap(
            @RequestBody GenerateRoadMapRequest request) throws Exception {

        return geminiService.generateRoadMap(request.getRoleTitle());
    }

    @PostMapping("/skills")
    public Object generateSkills(
            @RequestBody GenerateSkillsRequest request) throws Exception {
        return geminiService.generateSkillResource(request.getSkills());
    }
}