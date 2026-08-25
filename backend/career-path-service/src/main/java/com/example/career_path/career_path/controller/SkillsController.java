package com.example.career_path.career_path.controller;

import com.example.career_path.career_path.entity.Skills;
import com.example.career_path.career_path.service.SkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/skills")
public class SkillsController {

    @Autowired
    private SkillsService service;

    @GetMapping("/search")
    public List<Skills> searchSkills(@RequestParam String keyword) {
        return service.searchSkills(keyword);
    }
}

