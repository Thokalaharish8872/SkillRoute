package com.example.career_path.career_path.controller;

import com.example.career_path.career_path.dto.CareerPathResponse;
import com.example.career_path.career_path.entity.CareerPath;
import com.example.career_path.career_path.service.CareerPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/career_path")
public class CareerPathController {

    @Autowired
    private CareerPathService service;

    @GetMapping("/recommendations")
    public CareerPathResponse getRecommendations(
            @RequestParam int userId,
            @RequestParam(required = false, defaultValue = "false") boolean refresh) throws Exception {
        if (refresh) {
            return service.regenerateRecommendations(userId);
        }
        return service.getRecommendations(userId);
    }

    @GetMapping("/regenerate")
    public CareerPathResponse regenerateRecommendations(@RequestParam int userId) throws Exception {
        return service.regenerateRecommendations(userId);
    }

    @GetMapping("/search")
    public List<CareerPath> searchCareerPaths(@RequestParam String keyword) {
        return service.searchCareerPaths(keyword);
    }
}
