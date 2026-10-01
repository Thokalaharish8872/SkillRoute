package com.example.career_path.career_path.controller;

import com.example.career_path.career_path.dto.CareerPathResponse;
import com.example.career_path.career_path.entity.CareerPath;
import com.example.career_path.career_path.service.CareerPathService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/career_path")
public class CareerPathController {
    private static final Logger logger = LoggerFactory.getLogger(CareerPathController.class);

    @Autowired
    private CareerPathService service;

    @Autowired
    private com.example.career_path.career_path.security.JwtService jwtService;

    @GetMapping("/recommendations")
    public CareerPathResponse getRecommendations(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false, defaultValue = "false") boolean refresh) throws Exception {
        if ((userId == null || userId == 0) && authHeader != null) {
            Integer extracted = jwtService.extractUserId(authHeader);
            if (extracted != null) {
                userId = extracted;
            }
        }
        if (userId == null) {
            userId = 1;
        }
        logger.info("Received recommendations request for userId: {}, refresh: {}", userId, refresh);
        if (refresh) {
            return service.regenerateRecommendations(userId);
        }
        return service.getRecommendations(userId);
    }

    @GetMapping("/regenerate")
    public CareerPathResponse regenerateRecommendations(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) Integer userId) throws Exception {
        if ((userId == null || userId == 0) && authHeader != null) {
            Integer extracted = jwtService.extractUserId(authHeader);
            if (extracted != null) {
                userId = extracted;
            }
        }
        if (userId == null) {
            userId = 1;
        }
        logger.info("Received regenerateRecommendations request for userId: {}", userId);
        return service.regenerateRecommendations(userId);
    }

    @GetMapping("/search")
    public List<CareerPath> searchCareerPaths(@RequestParam String keyword) {
        return service.searchCareerPaths(keyword);
    }
}
