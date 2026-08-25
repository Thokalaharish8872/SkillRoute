package com.example.career_path.career_path.client;

import com.example.career_path.career_path.dto.AiServiceResponse;
import com.example.career_path.career_path.dto.GenerateCareerPathRequest;
import com.example.career_path.career_path.dto.GenerateRoadMapRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AIClientFallback implements AIClient {

    @Override
    public AiServiceResponse generateRecommendations(GenerateCareerPathRequest request) throws Exception {
        System.err.println("Fallback triggered for AIClient.generateRecommendations");
        return new AiServiceResponse(new ArrayList<>());
    }

    @Override
    public Object generateSkills(List<String> skills) throws Exception {
        System.err.println("Fallback triggered for AIClient.generateSkills");
        return new ArrayList<>();
    }

    @Override
    public Object generateRoadMap(GenerateRoadMapRequest request) throws Exception {
        System.err.println("Fallback triggered for AIClient.generateRoadMap");
        return null;
    }
}
