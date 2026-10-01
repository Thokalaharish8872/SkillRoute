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

    /**
     * Accepts a list of skill names, fetches from DB or generates via AI for missing ones,
     * and returns full skill details (docs, videos, difficulty, etc.)
     */
    @PostMapping("/generate")
    public List<Skills> generateSkills(@RequestBody List<String> skillNames) throws Exception {
        return service.identifyMissingSkillsAndGenerate(skillNames);
    }
}


