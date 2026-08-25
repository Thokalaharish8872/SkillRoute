package com.example.user_details_service.controller;

import com.example.user_details_service.dto.UserDetailsServiceResponse;
import com.example.user_details_service.service.UserSkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserDetailsController {

    @Autowired
    private UserSkillsService service;

    @GetMapping("/get_skills_and_career_paths")
    public UserDetailsServiceResponse getSkillsAndCareerPaths(@RequestParam int userId) {
        return service.getSkillsAndCareerPaths(userId);
    }

    @PostMapping("/update_career_paths")
    public void updateCareerPaths(@RequestParam int userId, @RequestBody List<Integer> careerPaths) throws Exception {
        service.updateCareerPaths(userId, careerPaths);
    }
}
