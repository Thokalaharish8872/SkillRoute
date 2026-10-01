package com.example.skillroute_api_gateway.client;

import com.example.skillroute_api_gateway.config.FeignConfiguration;
import com.example.skillroute_api_gateway.dto.CareerPath;
import com.example.skillroute_api_gateway.dto.Module;
import com.example.skillroute_api_gateway.dto.ModuleResponse;
import com.example.skillroute_api_gateway.dto.Phases;
import com.example.skillroute_api_gateway.dto.RoadMapResponse;
import com.example.skillroute_api_gateway.dto.Skills;
import com.example.skillroute_api_gateway.dto.request.RoadMapRequest;
import com.example.skillroute_api_gateway.dto.response.CareerPathResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "career-path-service", configuration = FeignConfiguration.class)
public interface CareerPathClient {

    // Career Path Endpoints
    @PostMapping("/api/career_path/add_career_path")
    CareerPath addCareerPath(@RequestBody CareerPath request);

    @PostMapping("/api/career_path/add_career_paths")
    void addCareerPaths(@RequestBody List<CareerPath> request);

    @GetMapping("/api/career_path/recommendations")
    CareerPathResponse getRecommendations(@RequestParam(value = "userId", required = false) Integer userId, @RequestParam(value = "refresh", required = false, defaultValue = "false") boolean refresh) throws Exception;

    @GetMapping("/api/career_path/regenerate")
    CareerPathResponse regenerateRecommendations(@RequestParam(value = "userId", required = false) Integer userId) throws Exception;

    @GetMapping("/api/career_path/search")
    List<CareerPath> searchCareerPaths(@RequestParam("keyword") String keyword);

    // RoadMap Endpoints
    @PostMapping("/api/roadmap/add_roadmap")
    CareerPath addRoadMap(@RequestBody CareerPath request);

    @PostMapping("/api/roadmap/get_roadmap")
    Object getRoadMap(@RequestBody String roleTitle);

    // Module Endpoints
    @GetMapping("/api/modules/get_module")
    ModuleResponse getModule(@RequestParam("moduleId") int moduleId);

    @GetMapping("/api/modules/search")
    List<Module> searchModules(@RequestParam("keyword") String keyword);

    // Skills Endpoints
    @PostMapping("/api/skills/generate")
    List<Skills> generateSkills(@RequestBody List<String> skillNames);

    // Phases Endpoints
    @GetMapping("/api/phases/get_phase")
    Phases getPhase(@RequestParam("moduleId") int moduleId);
}
