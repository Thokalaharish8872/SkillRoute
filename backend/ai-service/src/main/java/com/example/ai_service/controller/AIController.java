package com.example.ai_service.controller;

import com.example.ai_service.dto.AiServiceResponse;
import com.example.ai_service.dto.GenerateCareerPathRequest;
import com.example.ai_service.dto.GenerateRoadMapRequest;
import com.example.ai_service.dto.GenerateSkillsRequest;
import com.example.ai_service.dto.ModuleResponse;
import com.example.ai_service.dto.RoadMapDto;
import com.example.ai_service.service.GeminiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class AIController {

    private static final Logger logger = LoggerFactory.getLogger(AIController.class);
    private final GeminiService geminiService;

    public AIController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/career-paths")
    public AiServiceResponse generateCareerPaths(
            @RequestBody GenerateCareerPathRequest request) throws Exception {

        logger.info("received request for generating career-paths for skills : {}", request.getSkills());
        return geminiService.generateRecommendations(request.getSkills());
    }

    @PostMapping("/roadmap")
    public RoadMapDto generateRoadMap(
            @RequestBody GenerateRoadMapRequest request) throws Exception {

        return geminiService.generateRoadMap(request.getRoleTitle());
    }

    @PostMapping("/skills")
    public List<ModuleResponse> generateSkills(
            @RequestBody List<String> skills) throws Exception {
        logger.info("received request to generate skill_resource : {}", skills);
        return geminiService.generateSkillResource(skills);
    }
}