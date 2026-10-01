package com.example.skillroute_api_gateway.controller;

import com.example.skillroute_api_gateway.dto.CareerPath;
import com.example.skillroute_api_gateway.dto.Module;
import com.example.skillroute_api_gateway.dto.ModuleResponse;
import com.example.skillroute_api_gateway.dto.Phases;
import com.example.skillroute_api_gateway.dto.RoadMapResponse;
import com.example.skillroute_api_gateway.dto.request.RoadMapRequest;
import com.example.skillroute_api_gateway.dto.response.CareerPathResponse;
import com.example.skillroute_api_gateway.client.CareerPathClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CareerPathGatewayController {

    private static final Logger logger = LoggerFactory.getLogger(CareerPathGatewayController.class);

    @Autowired
    private CareerPathClient careerPathClient;

    @PostMapping("/api/career_path/add_career_path")
    public CareerPath addCareerPath(@RequestBody CareerPath request) {
        logger.info("Received add_career_path request");
        return careerPathClient.addCareerPath(request);
    }

    @PostMapping("/api/career_path/add_career_paths")
    public void addCareerPaths(@RequestBody List<CareerPath> request) {
        logger.info("Received add_career_paths request");
        careerPathClient.addCareerPaths(request);
    }

    @GetMapping("/api/career_path/recommendations")
    public CareerPathResponse getRecommendations(@RequestParam(required = false) Integer userId, @RequestParam(required = false, defaultValue = "false") boolean refresh) throws Exception{
        logger.info("Received get_recommendations request for userId: {}, refresh: {}", userId, refresh);
        return careerPathClient.getRecommendations(userId, refresh);
    }

    @GetMapping("/api/career_path/regenerate")
    public CareerPathResponse regenerateRecommendations(@RequestParam(required = false) Integer userId) throws Exception{
        logger.info("Received regenerate_recommendations request for userId: {}", userId);
        return careerPathClient.regenerateRecommendations(userId);
    }

    @GetMapping("/api/career_path/search")
    public List<CareerPath> searchCareerPaths(@RequestParam String keyword) {
        logger.info("Received search_career_paths request for keyword: {}", keyword);
        return careerPathClient.searchCareerPaths(keyword);
    }

    // RoadMap Endpoints
    @PostMapping("/api/roadmap/add_roadmap")
    public CareerPath addRoadMap(@RequestBody CareerPath request) {
        logger.info("Received add_roadmap request");
        return careerPathClient.addRoadMap(request);
    }

    @PostMapping("/api/roadmap/get_roadmap")
    public Object getRoadMap(@RequestBody RoadMapRequest request) {
        logger.info("Received get_roadmap request for roleTitle: {}", request.getRoleTitle());
        Object response = careerPathClient.getRoadMap(request.getRoleTitle());
        logger.info("response : {}", response);
        return response;
    }

    // Module Endpoints
    @GetMapping("/api/modules/get_module")
    public ModuleResponse getModule(@RequestParam int moduleId) {
        logger.info("Received get_module request for moduleId: {}", moduleId);
        ModuleResponse response = careerPathClient.getModule(moduleId);
        logger.info("returning response for get_module : {}", response);
        return response;
    }

    @GetMapping("/api/modules/search")
    public List<Module> searchModules(@RequestParam String keyword) {
        logger.info("Received search_modules request for keyword: {}", keyword);
        return careerPathClient.searchModules(keyword);
    }

    @GetMapping("/api/phases/get_phase")
    public Phases getPhase(@RequestParam int moduleId) {
        logger.info("Received get_phase request for moduleId: {}", moduleId);
        return careerPathClient.getPhase(moduleId);
    }
}
